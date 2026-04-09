import React, { useEffect, useMemo, useRef, useState } from 'react';
import {
  FlatList,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

export type MealType = 'breakfast' | 'lunch' | 'dinner' | 'snack';

export type FoodItem = {
  id: string;
  name: string;
  caloriesPer100g: number;
  proteinPer100g?: number;
  carbsPer100g?: number;
  fatPer100g?: number;
  defaultGrams?: number;
  isFavorite?: boolean;
};

export type CustomFoodDraft = {
  name: string;
  caloriesPer100g: number;
  proteinPer100g?: number;
  carbsPer100g?: number;
  fatPer100g?: number;
};

type Props = {
  foods: FoodItem[];
  onSave: (payload: {
    food: FoodItem;
    grams: number;
    mealType: MealType;
    preview: NutritionPreview;
  }) => void;
  onCreateCustomFood: (draft: CustomFoodDraft) => void;
  defaultMealType?: MealType;
};

type NutritionPreview = {
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
};

const MEAL_TYPES: MealType[] = ['breakfast', 'lunch', 'dinner', 'snack'];
const SEARCH_DEBOUNCE_MS = 120;

const round = (value: number, decimals = 1) => {
  const factor = 10 ** decimals;
  return Math.round(value * factor) / factor;
};

const getSmartDefaultGrams = (food?: FoodItem) => {
  if (!food) {
    return '100';
  }

  if (food.defaultGrams && food.defaultGrams > 0) {
    return String(food.defaultGrams);
  }

  if (food.caloriesPer100g >= 400) {
    return '40';
  }

  if (food.caloriesPer100g >= 250) {
    return '60';
  }

  if (food.caloriesPer100g >= 120) {
    return '100';
  }

  return '150';
};

const calculateNutrition = (food: FoodItem, grams: number): NutritionPreview => {
  const ratio = grams / 100;
  return {
    calories: round(food.caloriesPer100g * ratio, 0),
    protein: round((food.proteinPer100g ?? 0) * ratio),
    carbs: round((food.carbsPer100g ?? 0) * ratio),
    fat: round((food.fatPer100g ?? 0) * ratio),
  };
};

export const AddMealScreen = ({
  foods,
  onSave,
  onCreateCustomFood,
  defaultMealType = 'lunch',
}: Props) => {
  const [query, setQuery] = useState('');
  const [debouncedQuery, setDebouncedQuery] = useState('');
  const [selectedFoodId, setSelectedFoodId] = useState<string | null>(null);
  const [gramsText, setGramsText] = useState('');
  const [mealType, setMealType] = useState<MealType>(defaultMealType);
  const [showCustomFood, setShowCustomFood] = useState(false);
  const [customName, setCustomName] = useState('');
  const [customCalories, setCustomCalories] = useState('');
  const [validationError, setValidationError] = useState<string | null>(null);

  const gramsInputRef = useRef<TextInput>(null);
  const searchInputRef = useRef<TextInput>(null);

  useEffect(() => {
    const timeout = setTimeout(() => setDebouncedQuery(query.trim().toLowerCase()), SEARCH_DEBOUNCE_MS);
    return () => clearTimeout(timeout);
  }, [query]);

  const selectedFood = useMemo(
    () => foods.find((food) => food.id === selectedFoodId) ?? null,
    [foods, selectedFoodId],
  );

  useEffect(() => {
    if (!selectedFood) {
      return;
    }

    setGramsText(getSmartDefaultGrams(selectedFood));
    setValidationError(null);

    requestAnimationFrame(() => {
      gramsInputRef.current?.focus();
      gramsInputRef.current?.setNativeProps({ selection: { start: 0, end: 4 } });
    });
  }, [selectedFood?.id]);

  const sortedFoods = useMemo(() => {
    const queryText = debouncedQuery;

    const filtered = queryText
      ? foods.filter((food) => {
          const haystack = `${food.name}`.toLowerCase();
          return haystack.includes(queryText);
        })
      : foods;

    return [...filtered].sort((a, b) => {
      if (!!a.isFavorite !== !!b.isFavorite) {
        return a.isFavorite ? -1 : 1;
      }

      return a.name.localeCompare(b.name);
    });
  }, [foods, debouncedQuery]);

  const gramsValue = Number.parseFloat(gramsText);
  const validGrams = Number.isFinite(gramsValue) && gramsValue > 0 && gramsValue <= 2500;

  const nutritionPreview = useMemo(() => {
    if (!selectedFood || !validGrams) {
      return null;
    }

    return calculateNutrition(selectedFood, gramsValue);
  }, [selectedFood, validGrams, gramsValue]);

  const saveDisabled = !selectedFood || !validGrams;

  const handleSave = () => {
    if (!selectedFood) {
      setValidationError('Select a food first.');
      return;
    }

    if (!validGrams || !nutritionPreview) {
      setValidationError('Enter grams between 1 and 2500.');
      return;
    }

    onSave({
      food: selectedFood,
      grams: gramsValue,
      mealType,
      preview: nutritionPreview,
    });
  };

  const handleCreateCustomFood = () => {
    const calories = Number.parseFloat(customCalories);

    if (!customName.trim()) {
      setValidationError('Custom food needs a name.');
      return;
    }

    if (!Number.isFinite(calories) || calories <= 0) {
      setValidationError('Calories per 100g must be greater than 0.');
      return;
    }

    onCreateCustomFood({
      name: customName.trim(),
      caloriesPer100g: calories,
    });

    setCustomName('');
    setCustomCalories('');
    setShowCustomFood(false);
    setValidationError(null);
    searchInputRef.current?.focus();
  };

  return (
    <KeyboardAvoidingView
      style={styles.page}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      keyboardVerticalOffset={Platform.OS === 'ios' ? 80 : 0}
    >
      <View style={styles.container}>
        <Text style={styles.title}>Add meal</Text>

        <TextInput
          ref={searchInputRef}
          placeholder="Search foods"
          value={query}
          onChangeText={setQuery}
          style={styles.searchInput}
          returnKeyType="search"
          autoCorrect={false}
          autoCapitalize="none"
        />

        <View style={styles.mealTypeRow}>
          {MEAL_TYPES.map((item) => {
            const active = mealType === item;
            return (
              <Pressable
                key={item}
                onPress={() => setMealType(item)}
                style={[styles.mealTypeChip, active && styles.mealTypeChipActive]}
              >
                <Text style={[styles.mealTypeChipText, active && styles.mealTypeChipTextActive]}>{item}</Text>
              </Pressable>
            );
          })}
        </View>

        <FlatList
          data={sortedFoods}
          keyboardShouldPersistTaps="handled"
          keyExtractor={(item) => item.id}
          style={styles.foodList}
          contentContainerStyle={styles.foodListContent}
          renderItem={({ item }) => {
            const selected = item.id === selectedFoodId;
            return (
              <Pressable
                onPress={() => setSelectedFoodId(item.id)}
                style={[styles.foodRow, selected && styles.foodRowSelected]}
              >
                <View style={styles.foodInfo}>
                  <Text style={styles.foodName}>
                    {item.isFavorite ? '★ ' : ''}
                    {item.name}
                  </Text>
                  <Text style={styles.foodMeta}>{item.caloriesPer100g} kcal / 100g</Text>
                </View>
              </Pressable>
            );
          }}
          ListEmptyComponent={<Text style={styles.emptyText}>No foods match your search.</Text>}
        />

        <View style={styles.inputRow}>
          <Text style={styles.label}>Grams</Text>
          <TextInput
            ref={gramsInputRef}
            style={styles.gramsInput}
            value={gramsText}
            onChangeText={(value) => {
              setGramsText(value.replace(/[^0-9.]/g, ''));
              if (validationError) {
                setValidationError(null);
              }
            }}
            keyboardType="decimal-pad"
            returnKeyType="done"
          />
        </View>

        {nutritionPreview ? (
          <View style={styles.previewCard}>
            <Text style={styles.previewTitle}>Instant nutrition preview</Text>
            <Text style={styles.previewText}>Calories: {nutritionPreview.calories} kcal</Text>
            <Text style={styles.previewText}>Protein: {nutritionPreview.protein} g</Text>
            <Text style={styles.previewText}>Carbs: {nutritionPreview.carbs} g</Text>
            <Text style={styles.previewText}>Fat: {nutritionPreview.fat} g</Text>
          </View>
        ) : (
          <Text style={styles.helperText}>Select a food and valid grams to preview nutrition.</Text>
        )}

        <Pressable onPress={() => setShowCustomFood((current) => !current)}>
          <Text style={styles.customToggle}>{showCustomFood ? 'Hide custom food' : '+ Create custom food'}</Text>
        </Pressable>

        {showCustomFood && (
          <View style={styles.customCard}>
            <TextInput
              style={styles.customInput}
              value={customName}
              onChangeText={setCustomName}
              placeholder="Food name"
            />
            <TextInput
              style={styles.customInput}
              value={customCalories}
              onChangeText={(value) => setCustomCalories(value.replace(/[^0-9.]/g, ''))}
              keyboardType="decimal-pad"
              placeholder="Calories per 100g"
            />
            <Pressable style={styles.customCreateButton} onPress={handleCreateCustomFood}>
              <Text style={styles.customCreateButtonText}>Create food</Text>
            </Pressable>
          </View>
        )}

        {!!validationError && <Text style={styles.errorText}>{validationError}</Text>}
      </View>

      <View style={styles.saveBar}>
        <Pressable
          style={[styles.saveButton, saveDisabled && styles.saveButtonDisabled]}
          disabled={saveDisabled}
          onPress={handleSave}
        >
          <Text style={styles.saveButtonText}>Save meal</Text>
        </Pressable>
      </View>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  page: { flex: 1, backgroundColor: '#F8FAFC' },
  container: { flex: 1, paddingHorizontal: 16, paddingTop: 16, gap: 12 },
  title: { fontSize: 24, fontWeight: '700', color: '#0F172A' },
  searchInput: {
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#CBD5E1',
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 10,
  },
  mealTypeRow: { flexDirection: 'row', gap: 8 },
  mealTypeChip: {
    borderWidth: 1,
    borderColor: '#94A3B8',
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 6,
  },
  mealTypeChipActive: { backgroundColor: '#2563EB', borderColor: '#2563EB' },
  mealTypeChipText: { color: '#334155', fontWeight: '600', textTransform: 'capitalize' },
  mealTypeChipTextActive: { color: '#fff' },
  foodList: { flex: 1, minHeight: 180 },
  foodListContent: { gap: 8, paddingBottom: 6 },
  foodRow: {
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#E2E8F0',
    borderRadius: 10,
    padding: 12,
  },
  foodRowSelected: { borderColor: '#2563EB', borderWidth: 2 },
  foodInfo: { gap: 4 },
  foodName: { fontSize: 16, color: '#0F172A', fontWeight: '600' },
  foodMeta: { color: '#64748B' },
  emptyText: { color: '#64748B', textAlign: 'center', marginTop: 20 },
  inputRow: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' },
  label: { fontSize: 15, color: '#0F172A', fontWeight: '600' },
  gramsInput: {
    width: 120,
    textAlign: 'right',
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#CBD5E1',
    borderRadius: 8,
    paddingHorizontal: 10,
    paddingVertical: 8,
  },
  previewCard: {
    backgroundColor: '#EFF6FF',
    borderColor: '#BFDBFE',
    borderWidth: 1,
    borderRadius: 10,
    padding: 12,
    gap: 3,
  },
  previewTitle: { color: '#1E3A8A', fontWeight: '700', marginBottom: 4 },
  previewText: { color: '#1E3A8A' },
  helperText: { color: '#64748B' },
  customToggle: { color: '#2563EB', fontWeight: '600' },
  customCard: { backgroundColor: '#fff', borderRadius: 10, padding: 12, gap: 8, borderWidth: 1, borderColor: '#E2E8F0' },
  customInput: {
    borderWidth: 1,
    borderColor: '#CBD5E1',
    borderRadius: 8,
    paddingHorizontal: 10,
    paddingVertical: 8,
  },
  customCreateButton: {
    backgroundColor: '#0EA5E9',
    borderRadius: 8,
    alignItems: 'center',
    paddingVertical: 10,
  },
  customCreateButtonText: { color: '#fff', fontWeight: '700' },
  errorText: { color: '#B91C1C', fontWeight: '600' },
  saveBar: {
    borderTopColor: '#E2E8F0',
    borderTopWidth: 1,
    backgroundColor: '#fff',
    paddingHorizontal: 16,
    paddingTop: 10,
    paddingBottom: Platform.OS === 'ios' ? 28 : 14,
  },
  saveButton: {
    backgroundColor: '#16A34A',
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 14,
  },
  saveButtonDisabled: {
    backgroundColor: '#94A3B8',
  },
  saveButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '800',
  },
});

export default AddMealScreen;
