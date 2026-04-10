package com.lokma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lokma.app.LokmaApplication
import com.lokma.app.domain.model.MealType
import com.lokma.app.ui.components.FoodRow
import com.lokma.app.ui.viewmodel.AddMealViewModel

@Composable
fun AddMealScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: AddMealViewModel =
        viewModel(factory = AddMealViewModel.factory(app.container.foodRepository, app.container.mealRepository))
    val foods by vm.foods.collectAsState()

    var query by remember { mutableStateOf("") }
    var grams by remember { mutableStateOf("100") }
    var selectedMeal by remember { mutableStateOf(MealType.BREAKFAST) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
        MealTypeSelector(selected = selectedMeal, onSelect = { selectedMeal = it })
        OutlinedTextField(
            value = grams,
            onValueChange = { grams = it },
            label = { Text("Grams") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(foods, key = { it.id }) { food ->
                FoodRow(food = food, trailing = {
                    Button(onClick = { vm.addMeal(food.id, selectedMeal, grams.toFloatOrNull() ?: food.defaultGramAmount) }) {
                        Text("Add")
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealTypeSelector(selected: MealType, onSelect: (MealType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected.label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Meal") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            MealType.entries.forEach {
                DropdownMenuItem(
                    text = { Text(it.label) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMealPreview() {
    Column(modifier = Modifier.padding(16.dp)) { Text("Add meal preview") }
}
