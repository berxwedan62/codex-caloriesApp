package com.lokma.app.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lokma.app.LokmaApplication
import com.lokma.app.ui.components.CalorieWarningState
import com.lokma.app.ui.components.resolveCalorieWarningState
import com.lokma.app.ui.viewmodel.HomeUiState
import com.lokma.app.ui.viewmodel.HomeViewModel
import com.lokma.app.ui.viewmodel.RemainingWarningState

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
    val warningState = resolveCalorieWarningState(
        totalCalories = state.totalCalories,
        calorieTarget = state.calorieTarget,
        calorieWarningThreshold = state.calorieWarningThreshold
    )
    val blinkTransition = rememberInfiniteTransition(label = "homeWarningBlink")
    val blinkingAlpha by blinkTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "homeBlinkingAlpha"
    )
    val shouldBlinkRemaining = warningState != CalorieWarningState.NORMAL
    val shouldBlinkTotal = warningState == CalorieWarningState.OVER_TARGET
    val remainingColor = when (warningState) {
        CalorieWarningState.NORMAL -> MaterialTheme.colorScheme.onSurface
        CalorieWarningState.LOW_REMAINING -> Color(0xFFF59E0B)
        CalorieWarningState.OVER_TARGET -> MaterialTheme.colorScheme.error
    }
    val totalColor = if (warningState == CalorieWarningState.OVER_TARGET) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Today • ${state.today}", style = MaterialTheme.typography.titleLarge)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "Total: ${state.totalCalories} kcal",
                    style = MaterialTheme.typography.titleMedium,
                    color = totalColor,
                    modifier = Modifier.alpha(if (shouldBlinkTotal) blinkingAlpha else 1f)
                )
                Text("Target: ${state.calorieTarget} kcal")
                RemainingText(
                    remainingCalories = state.remainingCalories,
                    warningState = state.remainingWarningState
                )
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

@Composable
private fun RemainingText(
    remainingCalories: Int,
    warningState: RemainingWarningState
) {
    val blinkEnabled = warningState != RemainingWarningState.Normal
    val blinkingAlpha by rememberInfiniteTransition(label = "remainingBlink").animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 550),
            repeatMode = RepeatMode.Reverse
        ),
        label = "remainingBlinkAlpha"
    )
    val warningColor = when (warningState) {
        RemainingWarningState.OverTarget -> Color(0xFFDC2626)
        RemainingWarningState.NearTarget -> Color(0xFFEAB308)
        RemainingWarningState.Normal -> MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = "Remaining: $remainingCalories kcal",
        color = warningColor,
        modifier = Modifier.alpha(if (blinkEnabled) blinkingAlpha else 1f)
    )
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    HomeContent(
        state = HomeUiState(
            today = "2026-04-09",
            totalCalories = 980,
            calorieTarget = 2200,
            remainingCalories = 1220,
            calorieWarningThreshold = 200,
            remainingWarningState = RemainingWarningState.Normal
        ),
        onDelete = {}
    )
}
