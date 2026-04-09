import React, { useMemo, useState } from 'react';
import {
  FlatList,
  Pressable,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

type MealType = 'breakfast' | 'lunch' | 'dinner' | 'snack';

type FoodEntry = {
  id: string;
  meal: MealType;
  name: string;
  calories: number;
  date: string; // YYYY-MM-DD
};

type FavoriteFood = {
  id: string;
  name: string;
  calories: number;
  meal: MealType;
};

const DAILY_TARGET = 2200;

const MEAL_ORDER: MealType[] = ['breakfast', 'lunch', 'dinner', 'snack'];

const MEAL_LABEL: Record<MealType, string> = {
  breakfast: 'Breakfast',
  lunch: 'Lunch',
  dinner: 'Dinner',
  snack: 'Snack',
};

const STARTER_FAVORITES: FavoriteFood[] = [
  { id: 'fav-1', name: 'Greek Yogurt', calories: 130, meal: 'breakfast' },
  { id: 'fav-2', name: 'Chicken Salad', calories: 360, meal: 'lunch' },
  { id: 'fav-3', name: 'Grilled Salmon', calories: 430, meal: 'dinner' },
  { id: 'fav-4', name: 'Protein Bar', calories: 190, meal: 'snack' },
];

const toDateKey = (date: Date) => {
  const y = date.getFullYear();
  const m = `${date.getMonth() + 1}`.padStart(2, '0');
  const d = `${date.getDate()}`.padStart(2, '0');
  return `${y}-${m}-${d}`;
};

const prettyDate = (dateKey: string) => {
  const [y, m, d] = dateKey.split('-').map(Number);
  const date = new Date(y, m - 1, d);
  return date.toLocaleDateString(undefined, {
    weekday: 'short',
    month: 'short',
    day: 'numeric',
  });
};

const newId = () => `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`;

export default function HomeScreen() {
  const [selectedDate, setSelectedDate] = useState<string>(toDateKey(new Date()));
  const [entries, setEntries] = useState<FoodEntry[]>([]);
  const [favorites] = useState<FavoriteFood[]>(STARTER_FAVORITES);
  const [isComposerOpen, setIsComposerOpen] = useState(false);
  const [draftName, setDraftName] = useState('');
  const [draftCalories, setDraftCalories] = useState('');
  const [draftMeal, setDraftMeal] = useState<MealType>('breakfast');

  const dayEntries = useMemo(
    () => entries.filter((entry) => entry.date === selectedDate),
    [entries, selectedDate],
  );

  const mealBuckets = useMemo(() => {
    const map = {
      breakfast: [] as FoodEntry[],
      lunch: [] as FoodEntry[],
      dinner: [] as FoodEntry[],
      snack: [] as FoodEntry[],
    };
    for (const entry of dayEntries) {
      map[entry.meal].push(entry);
    }
    return map;
  }, [dayEntries]);

  const totalCalories = useMemo(
    () => dayEntries.reduce((sum, entry) => sum + entry.calories, 0),
    [dayEntries],
  );

  const remainingCalories = DAILY_TARGET - totalCalories;

  const jumpDate = (deltaDays: number) => {
    const [y, m, d] = selectedDate.split('-').map(Number);
    const next = new Date(y, m - 1, d);
    next.setDate(next.getDate() + deltaDays);
    setSelectedDate(toDateKey(next));
  };

  const addFavorite = (favorite: FavoriteFood) => {
    setEntries((prev) => [
      {
        id: newId(),
        meal: favorite.meal,
        name: favorite.name,
        calories: favorite.calories,
        date: selectedDate,
      },
      ...prev,
    ]);
  };

  const addFromComposer = () => {
    const calories = Number(draftCalories);
    if (!draftName.trim() || !Number.isFinite(calories) || calories <= 0) {
      return;
    }

    setEntries((prev) => [
      {
        id: newId(),
        meal: draftMeal,
        name: draftName.trim(),
        calories,
        date: selectedDate,
      },
      ...prev,
    ]);

    setDraftName('');
    setDraftCalories('');
    setDraftMeal('breakfast');
    setIsComposerOpen(false);
  };

  const deleteEntry = (id: string) => {
    setEntries((prev) => prev.filter((entry) => entry.id !== id));
  };

  const editEntry = (id: string) => {
    const target = entries.find((entry) => entry.id === id);
    if (!target) return;

    setDraftName(target.name);
    setDraftCalories(String(target.calories));
    setDraftMeal(target.meal);
    setIsComposerOpen(true);

    setEntries((prev) => prev.filter((entry) => entry.id !== id));
  };

  return (
    <SafeAreaView style={styles.page}>
      <ScrollView contentContainerStyle={styles.content}>
        <View style={styles.dateRow}>
          <Pressable style={styles.dateButton} onPress={() => jumpDate(-1)}>
            <Text style={styles.dateButtonText}>‹</Text>
          </Pressable>
          <Text style={styles.dateTitle}>{prettyDate(selectedDate)}</Text>
          <Pressable style={styles.dateButton} onPress={() => jumpDate(1)}>
            <Text style={styles.dateButtonText}>›</Text>
          </Pressable>
        </View>

        <View style={styles.summaryCard}>
          <Metric label="Total" value={totalCalories} />
          <Metric label="Target" value={DAILY_TARGET} />
          <Metric label="Remaining" value={remainingCalories} />
        </View>

        <View>
          <Text style={styles.sectionTitle}>Quick Add</Text>
          <FlatList
            horizontal
            data={favorites}
            keyExtractor={(item) => item.id}
            contentContainerStyle={styles.favoriteList}
            ItemSeparatorComponent={() => <View style={{ width: 8 }} />}
            renderItem={({ item }) => (
              <Pressable style={styles.favoriteChip} onPress={() => addFavorite(item)}>
                <Text style={styles.favoriteText}>{item.name}</Text>
                <Text style={styles.favoriteMeta}>{item.calories} cal</Text>
              </Pressable>
            )}
            showsHorizontalScrollIndicator={false}
          />
        </View>

        {MEAL_ORDER.map((meal) => {
          const mealEntries = mealBuckets[meal];
          const subtotal = mealEntries.reduce((sum, entry) => sum + entry.calories, 0);

          return (
            <View key={meal} style={styles.mealCard}>
              <View style={styles.mealHeader}>
                <Text style={styles.mealTitle}>{MEAL_LABEL[meal]}</Text>
                <Text style={styles.mealSubtotal}>{subtotal} cal</Text>
              </View>

              {mealEntries.length === 0 ? (
                <View style={styles.emptyState}>
                  <Text style={styles.emptyStateText}>No entries yet.</Text>
                </View>
              ) : (
                mealEntries.map((entry) => (
                  <View key={entry.id} style={styles.entryRow}>
                    <View>
                      <Text style={styles.entryName}>{entry.name}</Text>
                      <Text style={styles.entryCalories}>{entry.calories} cal</Text>
                    </View>
                    <View style={styles.entryActions}>
                      <Pressable onPress={() => editEntry(entry.id)} style={styles.textAction}>
                        <Text style={styles.editAction}>Edit</Text>
                      </Pressable>
                      <Pressable onPress={() => deleteEntry(entry.id)} style={styles.textAction}>
                        <Text style={styles.deleteAction}>Delete</Text>
                      </Pressable>
                    </View>
                  </View>
                ))
              )}
            </View>
          );
        })}
      </ScrollView>

      {isComposerOpen && (
        <View style={styles.composer}>
          <Text style={styles.composerTitle}>Add meal entry</Text>
          <TextInput
            placeholder="Food name"
            value={draftName}
            onChangeText={setDraftName}
            style={styles.input}
          />
          <TextInput
            placeholder="Calories"
            value={draftCalories}
            onChangeText={setDraftCalories}
            keyboardType="numeric"
            style={styles.input}
          />
          <View style={styles.mealSwitchRow}>
            {MEAL_ORDER.map((meal) => (
              <Pressable
                key={meal}
                onPress={() => setDraftMeal(meal)}
                style={[styles.mealSwitch, draftMeal === meal && styles.mealSwitchActive]}
              >
                <Text
                  style={[styles.mealSwitchText, draftMeal === meal && styles.mealSwitchTextActive]}
                >
                  {MEAL_LABEL[meal]}
                </Text>
              </Pressable>
            ))}
          </View>
          <View style={styles.composerActions}>
            <Pressable style={styles.ghostButton} onPress={() => setIsComposerOpen(false)}>
              <Text style={styles.ghostButtonText}>Cancel</Text>
            </Pressable>
            <Pressable style={styles.primaryButton} onPress={addFromComposer}>
              <Text style={styles.primaryButtonText}>Save</Text>
            </Pressable>
          </View>
        </View>
      )}

      <Pressable style={styles.fab} onPress={() => setIsComposerOpen((v) => !v)}>
        <Text style={styles.fabText}>＋</Text>
      </Pressable>
    </SafeAreaView>
  );
}

function Metric({ label, value }: { label: string; value: number }) {
  return (
    <View style={styles.metric}>
      <Text style={styles.metricValue}>{value}</Text>
      <Text style={styles.metricLabel}>{label}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  page: {
    flex: 1,
    backgroundColor: '#f8fafc',
  },
  content: {
    padding: 16,
    paddingBottom: 96,
    gap: 16,
  },
  dateRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  dateButton: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: '#e2e8f0',
    alignItems: 'center',
    justifyContent: 'center',
  },
  dateButtonText: {
    fontSize: 22,
    color: '#0f172a',
    lineHeight: 22,
  },
  dateTitle: {
    fontSize: 17,
    fontWeight: '600',
    color: '#0f172a',
  },
  summaryCard: {
    backgroundColor: '#0f172a',
    borderRadius: 16,
    padding: 16,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  metric: {
    alignItems: 'center',
  },
  metricValue: {
    color: '#f8fafc',
    fontSize: 20,
    fontWeight: '700',
  },
  metricLabel: {
    color: '#cbd5e1',
    fontSize: 12,
    marginTop: 2,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#0f172a',
    marginBottom: 8,
  },
  favoriteList: {
    paddingRight: 8,
  },
  favoriteChip: {
    backgroundColor: '#e2e8f0',
    borderRadius: 12,
    paddingVertical: 10,
    paddingHorizontal: 12,
    minWidth: 122,
  },
  favoriteText: {
    fontSize: 14,
    fontWeight: '600',
    color: '#0f172a',
  },
  favoriteMeta: {
    marginTop: 4,
    color: '#475569',
    fontSize: 12,
  },
  mealCard: {
    backgroundColor: '#ffffff',
    borderRadius: 14,
    padding: 12,
    gap: 8,
    borderWidth: 1,
    borderColor: '#e2e8f0',
  },
  mealHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  mealTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: '#0f172a',
  },
  mealSubtotal: {
    color: '#334155',
    fontWeight: '600',
  },
  emptyState: {
    borderRadius: 10,
    backgroundColor: '#f8fafc',
    paddingVertical: 12,
    alignItems: 'center',
  },
  emptyStateText: {
    color: '#64748b',
  },
  entryRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 8,
    borderTopWidth: 1,
    borderTopColor: '#f1f5f9',
  },
  entryName: {
    fontSize: 15,
    color: '#0f172a',
    fontWeight: '500',
  },
  entryCalories: {
    marginTop: 2,
    color: '#64748b',
    fontSize: 12,
  },
  entryActions: {
    flexDirection: 'row',
    gap: 8,
  },
  textAction: {
    paddingVertical: 4,
    paddingHorizontal: 6,
  },
  editAction: {
    color: '#2563eb',
    fontWeight: '600',
  },
  deleteAction: {
    color: '#dc2626',
    fontWeight: '600',
  },
  fab: {
    position: 'absolute',
    right: 18,
    bottom: 24,
    width: 58,
    height: 58,
    borderRadius: 29,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#0f172a',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.2,
    shadowRadius: 8,
    elevation: 3,
  },
  fabText: {
    color: '#ffffff',
    fontSize: 30,
    lineHeight: 30,
    marginTop: -2,
  },
  composer: {
    position: 'absolute',
    left: 12,
    right: 12,
    bottom: 96,
    backgroundColor: '#ffffff',
    borderRadius: 14,
    padding: 12,
    borderWidth: 1,
    borderColor: '#e2e8f0',
    gap: 8,
  },
  composerTitle: {
    fontWeight: '700',
    fontSize: 16,
    color: '#0f172a',
  },
  input: {
    borderWidth: 1,
    borderColor: '#cbd5e1',
    borderRadius: 10,
    paddingVertical: 10,
    paddingHorizontal: 12,
    fontSize: 14,
    color: '#0f172a',
    backgroundColor: '#fff',
  },
  mealSwitchRow: {
    flexDirection: 'row',
    gap: 6,
    flexWrap: 'wrap',
  },
  mealSwitch: {
    borderWidth: 1,
    borderColor: '#cbd5e1',
    borderRadius: 999,
    paddingVertical: 6,
    paddingHorizontal: 10,
  },
  mealSwitchActive: {
    borderColor: '#0f172a',
    backgroundColor: '#0f172a',
  },
  mealSwitchText: {
    fontSize: 12,
    color: '#334155',
    fontWeight: '600',
  },
  mealSwitchTextActive: {
    color: '#f8fafc',
  },
  composerActions: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    gap: 8,
    marginTop: 4,
  },
  ghostButton: {
    paddingHorizontal: 12,
    paddingVertical: 10,
    borderRadius: 10,
    backgroundColor: '#f1f5f9',
  },
  ghostButtonText: {
    color: '#0f172a',
    fontWeight: '600',
  },
  primaryButton: {
    paddingHorizontal: 14,
    paddingVertical: 10,
    borderRadius: 10,
    backgroundColor: '#0f172a',
  },
  primaryButtonText: {
    color: '#fff',
    fontWeight: '700',
  },
});
