package com.lokma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.lokma.app.ui.viewmodel.HomeUiState
import com.lokma.app.ui.viewmodel.HomeViewModel

private fun formatGrams(grams: Float): String =
    if (grams % 1f == 0f) grams.toInt().toString() else grams.toString()

@Composable
fun HomeScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: HomeViewModel = viewModel(factory = HomeViewModel.factory(app.container.mealRepository, app.container.settingsRepository))
    val state by vm.uiState.collectAsStateWithLifecycle()
    HomeContent(state = state, onDelete = { vm.delete(it) })
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onDelete: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Today • ${state.today}", style = MaterialTheme.typography.titleLarge)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Total: ${state.totalCalories} kcal", style = MaterialTheme.typography.titleMedium)
                Text("Target: ${state.calorieTarget} kcal")
                Text("Remaining: ${state.remainingCalories} kcal")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Meals", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.meals, key = { it.entry.id }) { meal ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${meal.foodName} (${meal.entry.mealType})")
                            Text("${formatGrams(meal.entry.grams)}g • ${meal.entry.calculatedCalories} kcal")
                        }
                        Button(onClick = { onDelete(meal.entry.id) }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    HomeContent(
        state = HomeUiState(today = "2026-04-09", totalCalories = 980, calorieTarget = 2200, remainingCalories = 1220),
        onDelete = {}
    )
}
