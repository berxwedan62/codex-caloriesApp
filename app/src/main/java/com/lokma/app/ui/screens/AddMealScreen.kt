package com.lokma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lokma.app.LokmaApplication
import com.lokma.app.data.local.entity.FoodItem
import com.lokma.app.ui.components.FoodRow
import com.lokma.app.ui.viewmodel.AddMealViewModel
import kotlinx.coroutines.delay

private const val MIN_GRAMS = 5
private const val GRAM_STEP = 5

@Composable
fun AddMealScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: AddMealViewModel =
        viewModel(factory = AddMealViewModel.factory(app.container.foodRepository, app.container.mealRepository))
    val foods by vm.foods.collectAsState()

    var query by remember { mutableStateOf("") }
    var selectedFoodForAdd by remember { mutableStateOf<FoodItem?>(null) }
    var gramInput by remember { mutableStateOf("") }
    var recentlyAddedFoodId by remember { mutableStateOf<Long?>(null) }
    var recentlyAddedGrams by remember { mutableStateOf<Float?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(recentlyAddedFoodId, recentlyAddedGrams) {
        recentlyAddedFoodId ?: return@LaunchedEffect
        val addedGrams = recentlyAddedGrams ?: return@LaunchedEffect
        val message = "Added ${addedGrams.toInt()}g as Snack"
        snackbarHostState.showSnackbar(message = message)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SnackbarHost(hostState = snackbarHostState)
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
            text = "Tap Add and enter grams (minimum 5g, in 5g steps).",
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(foods, key = { it.id }) { food ->
                FoodRow(food = food, trailing = {
                    val isAdded = recentlyAddedFoodId == food.id
                    Button(onClick = {
                        selectedFoodForAdd = food
                        gramInput = food.defaultGramAmount.toInt().toString()
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
        val grams = gramInput.toIntOrNull()
        val validationError = when {
            gramInput.isBlank() -> "Enter gram amount"
            grams == null -> "Enter a valid number"
            grams < MIN_GRAMS -> "Minimum is ${MIN_GRAMS}g"
            grams % GRAM_STEP != 0 -> "Use ${GRAM_STEP}g steps"
            else -> null
        }

        AlertDialog(
            onDismissRequest = { selectedFoodForAdd = null },
            title = { Text("How many grams?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(selectedFood.name)
                    OutlinedTextField(
                        value = gramInput,
                        onValueChange = { gramInput = it.filter(Char::isDigit) },
                        label = { Text("Gram amount") },
                        placeholder = { Text("e.g. 125") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = validationError != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Minimum ${MIN_GRAMS}g • Step ${GRAM_STEP}g")
                    if (validationError != null) {
                        Text(validationError)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (grams != null && validationError == null) {
                            vm.addMeal(foodId = selectedFood.id, grams = grams.toFloat())
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
