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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lokma.app.LokmaApplication
import com.lokma.app.ui.components.FoodRow
import com.lokma.app.ui.viewmodel.AddMealViewModel
import kotlinx.coroutines.delay

@Composable
fun AddMealScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: AddMealViewModel =
        viewModel(factory = AddMealViewModel.factory(app.container.foodRepository, app.container.mealRepository))
    val foods by vm.foods.collectAsState()

    var query by remember { mutableStateOf("") }
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
            text = "Quick add uses each food's default grams and logs it as Snack.",
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(foods, key = { it.id }) { food ->
                FoodRow(food = food, trailing = {
                    val isAdded = recentlyAddedFoodId == food.id
                    Button(onClick = {
                        val gramsToAdd = food.defaultGramAmount
                        vm.addMeal(food.id, grams = gramsToAdd)
                        recentlyAddedFoodId = food.id
                        recentlyAddedGrams = gramsToAdd
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
