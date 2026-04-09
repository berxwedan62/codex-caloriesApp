package com.lokma.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lokma.app.LokmaApplication
import com.lokma.app.ui.components.FoodRow
import com.lokma.app.ui.viewmodel.FoodLibraryViewModel

@Composable
fun FoodLibraryScreen() {
    val app = LocalContext.current.applicationContext as LokmaApplication
    val vm: FoodLibraryViewModel = viewModel(factory = FoodLibraryViewModel.factory(app.container.foodRepository))
    val foods by vm.foods.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Food library")
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Custom food name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = calories, onValueChange = { calories = it }, label = { Text("Calories / 100g") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            val kcal = calories.toIntOrNull() ?: return@Button
            if (name.isNotBlank()) {
                vm.addCustomFood(name.trim(), kcal, 0f, 0f, 0f)
                name = ""
                calories = ""
            }
        }) { Text("Add custom food") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(foods, key = { it.id }) { food ->
                FoodRow(food = food, onFavoriteToggle = { vm.toggleFavorite(food) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodLibraryPreview() {
    Column(modifier = Modifier.padding(16.dp)) { Text("Food library preview") }
}
