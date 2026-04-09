import React, { useMemo, useState } from 'react';
import {
  Alert,
  FlatList,
  Pressable,
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

type WeightEntry = {
  id: string;
  date: string; // YYYY-MM-DD
  weight: number;
};

const DEFAULT_ENTRIES: WeightEntry[] = [
  { id: '1', date: '2026-04-03', weight: 181.4 },
  { id: '2', date: '2026-04-04', weight: 181.1 },
  { id: '3', date: '2026-04-05', weight: 180.8 },
  { id: '4', date: '2026-04-06', weight: 180.6 },
  { id: '5', date: '2026-04-07', weight: 180.5 },
  { id: '6', date: '2026-04-08', weight: 180.7 },
  { id: '7', date: '2026-04-09', weight: 180.3 },
];

const DATE_REGEX = /^\d{4}-\d{2}-\d{2}$/;

const toUtcTimestamp = (date: string) => new Date(`${date}T00:00:00Z`).getTime();

const formatWeight = (value: number) => `${value.toFixed(1)} lb`;

const describeTrend = (entries: WeightEntry[]) => {
  if (entries.length < 2) return 'stable';

  const sorted = [...entries].sort((a, b) => toUtcTimestamp(a.date) - toUtcTimestamp(b.date));
  const latest = sorted[sorted.length - 1].weight;
  const previous = sorted[sorted.length - 2].weight;
  const delta = latest - previous;

  if (delta > 0.2) return 'up';
  if (delta < -0.2) return 'down';
  return 'stable';
};

const toSparkline = (entries: WeightEntry[]) => {
  if (entries.length < 2) return '–';

  const bars = ['▁', '▂', '▃', '▄', '▅', '▆', '▇'];
  const sorted = [...entries].sort((a, b) => toUtcTimestamp(a.date) - toUtcTimestamp(b.date));
  const values = sorted.map((entry) => entry.weight);
  const min = Math.min(...values);
  const max = Math.max(...values);

  if (max === min) return '▅'.repeat(values.length);

  return values
    .map((weight) => {
      const normalized = (weight - min) / (max - min);
      const barIndex = Math.min(bars.length - 1, Math.floor(normalized * bars.length));
      return bars[barIndex];
    })
    .join('');
};

export default function WeightScreen() {
  const [entries, setEntries] = useState<WeightEntry[]>(DEFAULT_ENTRIES);
  const [dateInput, setDateInput] = useState('');
  const [weightInput, setWeightInput] = useState('');
  const [editingId, setEditingId] = useState<string | null>(null);

  const sortedEntries = useMemo(
    () => [...entries].sort((a, b) => toUtcTimestamp(b.date) - toUtcTimestamp(a.date)),
    [entries],
  );

  const latestWeight = sortedEntries[0]?.weight ?? null;

  const sevenDayAverage = useMemo(() => {
    if (sortedEntries.length === 0) return null;

    const latestDay = toUtcTimestamp(sortedEntries[0].date);
    const sevenDaysAgo = latestDay - 6 * 24 * 60 * 60 * 1000;
    const inWindow = sortedEntries.filter((entry) => toUtcTimestamp(entry.date) >= sevenDaysAgo);

    if (inWindow.length === 0) return null;

    const total = inWindow.reduce((sum, entry) => sum + entry.weight, 0);
    return total / inWindow.length;
  }, [sortedEntries]);

  const trend = useMemo(() => describeTrend(sortedEntries), [sortedEntries]);
  const sparkline = useMemo(() => toSparkline(sortedEntries), [sortedEntries]);

  const resetForm = () => {
    setDateInput('');
    setWeightInput('');
    setEditingId(null);
  };

  const validateInputs = () => {
    if (!DATE_REGEX.test(dateInput)) {
      Alert.alert('Invalid date', 'Please use YYYY-MM-DD format.');
      return false;
    }

    const weight = Number.parseFloat(weightInput);
    if (Number.isNaN(weight) || weight <= 0) {
      Alert.alert('Invalid weight', 'Please enter a valid weight (e.g. 180.5).');
      return false;
    }

    return true;
  };

  const onSave = () => {
    if (!validateInputs()) return;

    const parsedWeight = Number.parseFloat(weightInput);

    if (editingId) {
      setEntries((prev) =>
        prev.map((entry) =>
          entry.id === editingId ? { ...entry, date: dateInput, weight: parsedWeight } : entry,
        ),
      );
      resetForm();
      return;
    }

    const existingSameDate = entries.find((entry) => entry.date === dateInput);
    if (existingSameDate) {
      Alert.alert('Date already exists', 'Edit the existing entry or choose another date.');
      return;
    }

    const newEntry: WeightEntry = {
      id: `${Date.now()}`,
      date: dateInput,
      weight: parsedWeight,
    };

    setEntries((prev) => [...prev, newEntry]);
    resetForm();
  };

  const onEdit = (entry: WeightEntry) => {
    setDateInput(entry.date);
    setWeightInput(String(entry.weight));
    setEditingId(entry.id);
  };

  const onDelete = (entryId: string) => {
    Alert.alert('Delete entry?', 'This cannot be undone.', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Delete',
        style: 'destructive',
        onPress: () => {
          setEntries((prev) => prev.filter((entry) => entry.id !== entryId));
          if (editingId === entryId) resetForm();
        },
      },
    ]);
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Weight</Text>

      <View style={styles.summaryCard}>
        <Text style={styles.summaryLabel}>Latest</Text>
        <Text style={styles.summaryValue}>{latestWeight ? formatWeight(latestWeight) : 'No data'}</Text>

        <Text style={styles.summaryLabel}>7-day average</Text>
        <Text style={styles.summaryValue}>
          {sevenDayAverage ? formatWeight(sevenDayAverage) : 'No data'}
        </Text>

        <Text style={styles.summaryLabel}>Trend</Text>
        <Text style={styles.summaryValue}>{trend}</Text>

        <Text style={styles.sparkline}>{sparkline}</Text>
      </View>

      <View style={styles.formRow}>
        <TextInput
          value={dateInput}
          onChangeText={setDateInput}
          style={styles.input}
          placeholder="YYYY-MM-DD"
          autoCapitalize="none"
        />
        <TextInput
          value={weightInput}
          onChangeText={setWeightInput}
          style={styles.input}
          placeholder="Weight (lb)"
          keyboardType="decimal-pad"
        />
      </View>

      <View style={styles.actionsRow}>
        <Pressable style={styles.primaryButton} onPress={onSave}>
          <Text style={styles.primaryButtonText}>{editingId ? 'Update entry' : 'Add entry'}</Text>
        </Pressable>

        {editingId ? (
          <Pressable style={styles.secondaryButton} onPress={resetForm}>
            <Text style={styles.secondaryButtonText}>Cancel</Text>
          </Pressable>
        ) : null}
      </View>

      <Text style={styles.sectionTitle}>History</Text>

      <FlatList
        data={sortedEntries}
        keyExtractor={(item) => item.id}
        contentContainerStyle={styles.listContent}
        ListEmptyComponent={<Text style={styles.emptyText}>No entries yet.</Text>}
        renderItem={({ item }) => (
          <View style={styles.row}>
            <View>
              <Text style={styles.rowDate}>{item.date}</Text>
              <Text style={styles.rowWeight}>{formatWeight(item.weight)}</Text>
            </View>

            <View style={styles.rowActions}>
              <Pressable onPress={() => onEdit(item)}>
                <Text style={styles.link}>Edit</Text>
              </Pressable>
              <Pressable onPress={() => onDelete(item.id)}>
                <Text style={[styles.link, styles.deleteLink]}>Delete</Text>
              </Pressable>
            </View>
          </View>
        )}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F7F8FA',
    paddingHorizontal: 16,
    paddingTop: 16,
  },
  title: {
    fontSize: 28,
    fontWeight: '700',
    marginBottom: 12,
    color: '#111827',
  },
  summaryCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 12,
    padding: 12,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },
  summaryLabel: {
    fontSize: 12,
    color: '#6B7280',
    marginTop: 6,
  },
  summaryValue: {
    fontSize: 18,
    fontWeight: '600',
    color: '#111827',
  },
  sparkline: {
    marginTop: 10,
    fontSize: 18,
    letterSpacing: 1,
    color: '#374151',
  },
  formRow: {
    flexDirection: 'row',
    gap: 8,
  },
  input: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#D1D5DB',
    paddingHorizontal: 10,
    paddingVertical: 10,
  },
  actionsRow: {
    flexDirection: 'row',
    marginTop: 10,
    gap: 8,
    marginBottom: 16,
  },
  primaryButton: {
    backgroundColor: '#2563EB',
    borderRadius: 8,
    paddingHorizontal: 14,
    paddingVertical: 10,
  },
  primaryButtonText: {
    color: '#FFFFFF',
    fontWeight: '600',
  },
  secondaryButton: {
    backgroundColor: '#E5E7EB',
    borderRadius: 8,
    paddingHorizontal: 14,
    paddingVertical: 10,
  },
  secondaryButtonText: {
    color: '#111827',
    fontWeight: '600',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '700',
    marginBottom: 6,
    color: '#111827',
  },
  listContent: {
    paddingBottom: 24,
  },
  row: {
    backgroundColor: '#FFFFFF',
    borderRadius: 10,
    borderWidth: 1,
    borderColor: '#E5E7EB',
    padding: 12,
    marginBottom: 8,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  rowDate: {
    color: '#6B7280',
    fontSize: 12,
  },
  rowWeight: {
    color: '#111827',
    fontSize: 20,
    fontWeight: '700',
  },
  rowActions: {
    flexDirection: 'row',
    gap: 12,
  },
  link: {
    color: '#2563EB',
    fontWeight: '600',
  },
  deleteLink: {
    color: '#DC2626',
  },
  emptyText: {
    color: '#6B7280',
    textAlign: 'center',
    paddingVertical: 24,
  },
});
