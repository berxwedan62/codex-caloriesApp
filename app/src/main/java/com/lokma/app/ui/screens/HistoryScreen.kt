package com.lokma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lokma.app.LokmaApplication
import com.lokma.app.data.repository.MealEntryUi
import com.lokma.app.ui.viewmodel.HistoryViewModel

@Composable
fun HistoryScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: HistoryViewModel = viewModel(factory = HistoryViewModel.factory(app.container.mealRepository))
    val entries by vm.entries.collectAsStateWithLifecycle()

    HistoryContent(
        date = vm.date(),
        entries = entries,
        onPrevious = { vm.shiftDays(-1) },
        onNext = { vm.shiftDays(1) }
    )
}

@Composable
private fun HistoryContent(
    date: String,
    entries: List<MealEntryUi>,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("History")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onPrevious) { Text("Prev") }
            Button(onClick = onNext) { Text("Next") }
            Text(date, modifier = Modifier.padding(top = 10.dp))
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entries, key = { it.entry.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                        Text(item.foodName)
                        Text("${item.entry.mealType} • ${item.entry.grams.toInt()}g • ${item.entry.calculatedCalories} kcal")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryPreview() {
    HistoryContent("2026-04-09", emptyList(), {}, {})
}
