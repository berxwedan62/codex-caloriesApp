package com.lokma.app.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lokma.app.LokmaApplication
import com.lokma.app.data.local.entity.FoodItem
import com.lokma.app.ui.components.CalorieWarningState
import com.lokma.app.ui.components.FoodRow
import com.lokma.app.ui.components.resolveCalorieWarningState
import com.lokma.app.ui.viewmodel.AddMealViewModel
import kotlinx.coroutines.delay

private const val MIN_GRAMS = 5

private const val DEFAULT_GRAM_INPUT = "100"

private fun sanitizeGramInput(input: String): String {
    val digitsAndDecimal = input.filter { it.isDigit() || it == '.' }
    val firstDecimalIndex = digitsAndDecimal.indexOf('.')
    if (firstDecimalIndex == -1) return digitsAndDecimal

    val beforeDecimal = digitsAndDecimal.substring(0, firstDecimalIndex + 1)
    val afterDecimal = digitsAndDecimal.substring(firstDecimalIndex + 1).replace(".", "")
    return beforeDecimal + afterDecimal
}

private fun formatGrams(grams: Float): String =
    if (grams % 1f == 0f) grams.toInt().toString() else grams.toString()

@Composable
fun AddMealScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: AddMealViewModel =
        viewModel(
            factory = AddMealViewModel.factory(
                app.container.foodRepository,
                app.container.mealRepository,
                app.container.settingsRepository
            )
        )
    val foods by vm.foods.collectAsState()
    val uiState by vm.uiState.collectAsState()

    var query by remember { mutableStateOf("") }
    var selectedFoodForAdd by remember { mutableStateOf<FoodItem?>(null) }
    var gramInput by remember { mutableStateOf("") }
    var recentlyAddedFoodId by remember { mutableStateOf<Long?>(null) }
    var recentlyAddedGrams by remember { mutableStateOf<Float?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val gramsForDialog = gramInput.toFloatOrNull()
    val previewCalories = if (gramsForDialog != null && gramsForDialog > 0f) {
        selectedFoodForAdd?.let { ((it.caloriesPer100g * gramsForDialog) / 100f).toInt() } ?: 0
    } else {
        0
    }
    val previewTotal = uiState.totalCalories + previewCalories
    val previewRemaining = uiState.calorieTarget - previewTotal
    val warningState = resolveCalorieWarningState(
        totalCalories = previewTotal,
        calorieTarget = uiState.calorieTarget,
        calorieWarningThreshold = uiState.calorieWarningThreshold
    )
    val blinkTransition = rememberInfiniteTransition(label = "addWarningBlink")
    val blinkingAlpha by blinkTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "addBlinkingAlpha"
    )
    val shouldBlinkRemaining = warningState != CalorieWarningState.NORMAL
    val shouldBlinkCalories = warningState == CalorieWarningState.OVER_TARGET
    val remainingColor = when (warningState) {
        CalorieWarningState.NORMAL -> MaterialTheme.colorScheme.onSurface
        CalorieWarningState.LOW_REMAINING -> Color(0xFFF59E0B)
        CalorieWarningState.OVER_TARGET -> MaterialTheme.colorScheme.error
    }
    val caloriesColor = if (warningState == CalorieWarningState.OVER_TARGET) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    LaunchedEffect(recentlyAddedFoodId, recentlyAddedGrams) {
        recentlyAddedFoodId ?: return@LaunchedEffect
        val addedGrams = recentlyAddedGrams ?: return@LaunchedEffect
        val message = "Added ${formatGrams(addedGrams)}g as Snack"
        snackbarHostState.showSnackbar(message = message)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SnackbarHost(hostState = snackbarHostState)
        Text(
            text = "Remaining: ${previewRemaining} kcal",
            color = remainingColor,
            modifier = Modifier.alpha(if (shouldBlinkRemaining) blinkingAlpha else 1f)
        )
        Text(
            text = "Calories: ${previewTotal} / ${uiState.calorieTarget} kcal",
            color = caloriesColor,
            modifier = Modifier.alpha(if (shouldBlinkCalories) blinkingAlpha else 1f)
        )
        Text("Add meal")
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                vm.onQueryChange(it)
            },
            label = { Text("Search food") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Tap Add and enter grams (minimum 5g).",
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(foods, key = { it.id }) { food ->
                FoodRow(food = food, trailing = {
                    val isAdded = recentlyAddedFoodId == food.id
                    Button(onClick = {
                        selectedFoodForAdd = food
                        gramInput = DEFAULT_GRAM_INPUT
                    }) {
                        if (isAdded) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Added",
                            )
                        } else {
                            Text("Add")
                        }
                    }
                })
            }
        }
    }

    selectedFoodForAdd?.let { selectedFood ->
        val grams = gramInput.toFloatOrNull()
        val validationError = when {
            gramInput.isBlank() -> "Enter gram amount"
            grams == null -> "Enter a valid number"
            grams < MIN_GRAMS -> "Minimum is ${MIN_GRAMS}g"
            else -> null
        }

        AlertDialog(
            onDismissRequest = { selectedFoodForAdd = null },
            title = { Text("How many grams?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(selectedFood.name)
                    Text("Remaining after add: ${previewRemaining} kcal")
                    OutlinedTextField(
                        value = gramInput,
                        onValueChange = { input ->
                            gramInput = sanitizeGramInput(input)
                        },
                        label = { Text("Gram amount") },
                        placeholder = { Text("e.g. 125") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = validationError != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Minimum ${MIN_GRAMS}g")
                    if (validationError != null) {
                        Text(validationError)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (grams != null && validationError == null) {
                            vm.addMeal(foodId = selectedFood.id, grams = grams)
                            recentlyAddedFoodId = selectedFood.id
                            recentlyAddedGrams = grams
                            selectedFoodForAdd = null
                        }
                    },
                    enabled = validationError == null
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedFoodForAdd = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(recentlyAddedFoodId) {
        if (recentlyAddedFoodId == null) return@LaunchedEffect
        delay(1500)
        recentlyAddedFoodId = null
        recentlyAddedGrams = null
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMealPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) { Text("Add meal preview") }
}
