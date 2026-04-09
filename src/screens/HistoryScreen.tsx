import React, { memo, useCallback, useMemo, useState } from 'react';
import {
  Platform,
  Pressable,
  SectionList,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import DateTimePicker, {
  DateTimePickerEvent,
} from '@react-native-community/datetimepicker';

export type MealType =
  | 'breakfast'
  | 'lunch'
  | 'dinner'
  | 'snack'
  | 'other';

export type MealEntry = {
  id: string;
  title: string;
  mealType: MealType;
  eatenAt: string; // ISO timestamp
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
};

export type HistoryScreenProps = {
  entries: MealEntry[];
  onEditEntry?: (entryId: string) => void;
  onDeleteEntry?: (entryId: string) => void;
};

type MealSection = {
  title: string;
  mealType: MealType;
  data: MealEntry[];
};

type MacroTotals = {
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
};

const MEAL_ORDER: MealType[] = ['breakfast', 'lunch', 'dinner', 'snack', 'other'];
const MEAL_LABEL: Record<MealType, string> = {
  breakfast: 'Breakfast',
  lunch: 'Lunch',
  dinner: 'Dinner',
  snack: 'Snack',
  other: 'Other',
};

const dateKey = (date: Date): string => date.toISOString().slice(0, 10);

const getTotals = (entries: MealEntry[]): MacroTotals => {
  return entries.reduce(
    (acc, entry) => ({
      calories: acc.calories + entry.calories,
      protein: acc.protein + entry.protein,
      carbs: acc.carbs + entry.carbs,
      fat: acc.fat + entry.fat,
    }),
    { calories: 0, protein: 0, carbs: 0, fat: 0 },
  );
};

const formatMacro = (label: string, value: number): string => `${label} ${Math.round(value)}g`;

const entryTimeFormatter = new Intl.DateTimeFormat(undefined, {
  hour: 'numeric',
  minute: '2-digit',
});

const dayHeaderFormatter = new Intl.DateTimeFormat(undefined, {
  weekday: 'short',
  month: 'short',
  day: 'numeric',
  year: 'numeric',
});

const shiftDate = (source: Date, days: number): Date => {
  const next = new Date(source);
  next.setDate(source.getDate() + days);
  return next;
};

const WeeklyAverageCard = memo(
  ({ entriesByDay, selectedDate }: { entriesByDay: Map<string, MealEntry[]>; selectedDate: Date }) => {
    const weeklyAverage = useMemo(() => {
      const selected = new Date(selectedDate);
      selected.setHours(0, 0, 0, 0);

      let caloriesTotal = 0;
      let daysWithData = 0;

      for (let i = 0; i < 7; i += 1) {
        const day = shiftDate(selected, -i);
        const dayEntries = entriesByDay.get(dateKey(day)) ?? [];

        if (dayEntries.length) {
          caloriesTotal += getTotals(dayEntries).calories;
          daysWithData += 1;
        }
      }

      if (!daysWithData) {
        return 0;
      }

      return Math.round(caloriesTotal / daysWithData);
    }, [entriesByDay, selectedDate]);

    return (
      <View style={styles.weeklyAvgCard}>
        <Text style={styles.weeklyAvgTitle}>7-day average</Text>
        <Text style={styles.weeklyAvgValue}>{weeklyAverage} kcal</Text>
      </View>
    );
  },
);

const HistoryScreen = ({ entries, onEditEntry, onDeleteEntry }: HistoryScreenProps) => {
  const [selectedDate, setSelectedDate] = useState(() => {
    const newestEntry = [...entries].sort(
      (a, b) => new Date(b.eatenAt).getTime() - new Date(a.eatenAt).getTime(),
    )[0];

    return newestEntry ? new Date(newestEntry.eatenAt) : new Date();
  });
  const [showDatePicker, setShowDatePicker] = useState(false);

  const entriesByDay = useMemo(() => {
    const map = new Map<string, MealEntry[]>();

    for (const entry of entries) {
      const day = dateKey(new Date(entry.eatenAt));
      const dayEntries = map.get(day);

      if (dayEntries) {
        dayEntries.push(entry);
      } else {
        map.set(day, [entry]);
      }
    }

    for (const [, dayEntries] of map) {
      dayEntries.sort((a, b) => new Date(a.eatenAt).getTime() - new Date(b.eatenAt).getTime());
    }

    return map;
  }, [entries]);

  const selectedDayKey = dateKey(selectedDate);
  const selectedDayEntries = entriesByDay.get(selectedDayKey) ?? [];

  const sections = useMemo<MealSection[]>(() => {
    const grouped = new Map<MealType, MealEntry[]>();

    for (const meal of MEAL_ORDER) {
      grouped.set(meal, []);
    }

    for (const entry of selectedDayEntries) {
      grouped.get(entry.mealType)?.push(entry);
    }

    return MEAL_ORDER.map((mealType) => ({
      mealType,
      title: MEAL_LABEL[mealType],
      data: grouped.get(mealType) ?? [],
    })).filter((section) => section.data.length > 0);
  }, [selectedDayEntries]);

  const totals = useMemo(() => getTotals(selectedDayEntries), [selectedDayEntries]);

  const onDateChange = useCallback((event: DateTimePickerEvent, date?: Date) => {
    if (Platform.OS !== 'ios') {
      setShowDatePicker(false);
    }

    if (event.type === 'set' && date) {
      setSelectedDate(date);
    }
  }, []);

  const renderMeal = useCallback(
    ({ item }: { item: MealEntry }) => (
      <View style={styles.mealRow}>
        <View style={styles.mealContent}>
          <Text style={styles.mealTitle}>{item.title}</Text>
          <Text style={styles.mealMeta}>
            {entryTimeFormatter.format(new Date(item.eatenAt))} • {item.calories} kcal
          </Text>
          <Text style={styles.macroText}>
            {formatMacro('P', item.protein)} · {formatMacro('C', item.carbs)} · {formatMacro('F', item.fat)}
          </Text>
        </View>
        <View style={styles.rowActions}>
          <Pressable onPress={() => onEditEntry?.(item.id)} style={styles.actionBtn}>
            <Text style={styles.actionText}>Edit</Text>
          </Pressable>
          <Pressable onPress={() => onDeleteEntry?.(item.id)} style={styles.actionBtn}>
            <Text style={[styles.actionText, styles.deleteText]}>Delete</Text>
          </Pressable>
        </View>
      </View>
    ),
    [onDeleteEntry, onEditEntry],
  );

  return (
    <View style={styles.container}>
      <View style={styles.topBar}>
        <Pressable onPress={() => setSelectedDate((prev) => shiftDate(prev, -1))} style={styles.dayNavBtn}>
          <Text style={styles.dayNavText}>‹</Text>
        </Pressable>

        <Pressable onPress={() => setShowDatePicker(true)} style={styles.datePickerBtn}>
          <Text style={styles.dateLabel}>{dayHeaderFormatter.format(selectedDate)}</Text>
          <Text style={styles.dateHint}>Tap to choose date</Text>
        </Pressable>

        <Pressable onPress={() => setSelectedDate((prev) => shiftDate(prev, 1))} style={styles.dayNavBtn}>
          <Text style={styles.dayNavText}>›</Text>
        </Pressable>
      </View>

      {showDatePicker && (
        <DateTimePicker mode="date" display="default" value={selectedDate} onChange={onDateChange} />
      )}

      <View style={styles.totalsCard}>
        <Text style={styles.totalsTitle}>Daily total</Text>
        <Text style={styles.totalCalories}>{totals.calories} kcal</Text>
        <Text style={styles.totalMacros}>
          {formatMacro('Protein', totals.protein)} · {formatMacro('Carbs', totals.carbs)} ·{' '}
          {formatMacro('Fat', totals.fat)}
        </Text>
      </View>

      <WeeklyAverageCard entriesByDay={entriesByDay} selectedDate={selectedDate} />

      {sections.length === 0 ? (
        <View style={styles.emptyState}>
          <Text style={styles.emptyStateEmoji}>🍽️</Text>
          <Text style={styles.emptyStateTitle}>No meals for this day</Text>
          <Text style={styles.emptyStateHint}>Pick another date or add your first meal entry.</Text>
        </View>
      ) : (
        <SectionList
          sections={sections}
          keyExtractor={(item) => item.id}
          renderItem={renderMeal}
          renderSectionHeader={({ section }) => <Text style={styles.sectionHeader}>{section.title}</Text>}
          stickySectionHeadersEnabled
          showsVerticalScrollIndicator={false}
          contentContainerStyle={styles.listContent}
          initialNumToRender={12}
          maxToRenderPerBatch={16}
          updateCellsBatchingPeriod={16}
          windowSize={7}
          removeClippedSubviews
        />
      )}
    </View>
  );
};

export default HistoryScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F8F9FB',
    paddingHorizontal: 16,
    paddingTop: 14,
  },
  topBar: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
    gap: 8,
  },
  dayNavBtn: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: '#FFFFFF',
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: '#E4E7EE',
  },
  dayNavText: {
    fontSize: 20,
    color: '#344054',
    fontWeight: '700',
  },
  datePickerBtn: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    paddingVertical: 8,
    paddingHorizontal: 12,
    borderColor: '#E4E7EE',
    borderWidth: 1,
  },
  dateLabel: {
    color: '#111827',
    fontWeight: '700',
    fontSize: 16,
  },
  dateHint: {
    color: '#6B7280',
    fontSize: 12,
    marginTop: 2,
  },
  totalsCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    padding: 14,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#E4E7EE',
  },
  totalsTitle: {
    color: '#6B7280',
    fontWeight: '600',
  },
  totalCalories: {
    color: '#111827',
    fontSize: 26,
    fontWeight: '800',
    marginTop: 2,
  },
  totalMacros: {
    color: '#4B5563',
    marginTop: 2,
    fontSize: 13,
  },
  weeklyAvgCard: {
    backgroundColor: '#EEF4FF',
    borderRadius: 12,
    paddingHorizontal: 14,
    paddingVertical: 10,
    marginBottom: 10,
  },
  weeklyAvgTitle: {
    color: '#3559A8',
    fontSize: 12,
    fontWeight: '600',
  },
  weeklyAvgValue: {
    color: '#1D4ED8',
    marginTop: 2,
    fontSize: 18,
    fontWeight: '800',
  },
  listContent: {
    paddingBottom: 28,
  },
  sectionHeader: {
    fontSize: 14,
    color: '#374151',
    fontWeight: '700',
    backgroundColor: '#F8F9FB',
    paddingVertical: 8,
  },
  mealRow: {
    backgroundColor: '#FFFFFF',
    borderRadius: 12,
    padding: 12,
    marginBottom: 8,
    borderColor: '#E4E7EE',
    borderWidth: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  mealContent: {
    flex: 1,
    paddingRight: 10,
  },
  mealTitle: {
    color: '#111827',
    fontSize: 15,
    fontWeight: '700',
  },
  mealMeta: {
    color: '#4B5563',
    marginTop: 3,
    fontSize: 12,
  },
  macroText: {
    color: '#6B7280',
    marginTop: 2,
    fontSize: 12,
  },
  rowActions: {
    flexDirection: 'row',
    gap: 8,
  },
  actionBtn: {
    paddingVertical: 5,
    paddingHorizontal: 8,
    borderRadius: 8,
    backgroundColor: '#F3F4F6',
  },
  actionText: {
    color: '#374151',
    fontWeight: '700',
    fontSize: 12,
  },
  deleteText: {
    color: '#B42318',
  },
  emptyState: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingBottom: 70,
  },
  emptyStateEmoji: {
    fontSize: 44,
  },
  emptyStateTitle: {
    marginTop: 8,
    fontSize: 17,
    color: '#111827',
    fontWeight: '700',
  },
  emptyStateHint: {
    marginTop: 4,
    fontSize: 13,
    color: '#6B7280',
    textAlign: 'center',
  },
});
