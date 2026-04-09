package com.example.caloriesapp.ui.food

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.data.CustomFood

@Composable
fun FoodLibraryScreen(
    viewModel: FoodLibraryViewModel,
    modifier: Modifier = Modifier,
) {
    val foods by viewModel.foods.collectAsState()
    val editorState by viewModel.editorState.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::openCreate) {
                Icon(Icons.Default.Add, contentDescription = "Create Food")
            }
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(foods, key = { it.id }) { food ->
                FoodCard(food = food, onClick = { viewModel.openEdit(food) })
            }
        }

        if (editorState != null) {
            FoodEditorDialog(
                state = editorState!!,
                onDismiss = viewModel::dismissEditor,
                onNameChange = viewModel::updateName,
                onCaloriesChange = viewModel::updateCalories,
                onProteinChange = viewModel::updateProtein,
                onCarbsChange = viewModel::updateCarbs,
                onFatChange = viewModel::updateFat,
                onDefaultGramChange = viewModel::updateDefaultGramAmount,
                onFavoriteChange = viewModel::updateFavorite,
                onSave = viewModel::save,
            )
        }
    }
}

@Composable
private fun FoodCard(
    food: CustomFood,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(food.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("${food.caloriesPer100g} kcal / 100g")
                Text("P ${food.proteinPer100g} • C ${food.carbsPer100g} • F ${food.fatPer100g}")
                Text("Default: ${food.defaultGramAmount}g")
            }

            IconButton(onClick = onClick) {
                Icon(
                    imageVector = if (food.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Favorite",
                )
            }
        }
    }
}

@Composable
private fun FoodEditorDialog(
    state: FoodFormUiState,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onDefaultGramChange: (String) -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (state.id == 0L) "Create Food" else "Edit Food") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    isError = state.errors.containsKey("name"),
                    supportingText = { state.errors["name"]?.let { Text(it) } },
                )
                MacroField("Calories / 100g", state.caloriesPer100g, state.errors["calories"], onCaloriesChange)
                MacroField("Protein / 100g", state.proteinPer100g, state.errors["protein"], onProteinChange)
                MacroField("Carbs / 100g", state.carbsPer100g, state.errors["carbs"], onCarbsChange)
                MacroField("Fat / 100g", state.fatPer100g, state.errors["fat"], onFatChange)
                MacroField(
                    label = "Default gram amount",
                    value = state.defaultGramAmount,
                    error = state.errors["defaultGramAmount"],
                    onValueChange = onDefaultGramChange,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = state.isFavorite, onCheckedChange = onFavoriteChange)
                    Text("Favorite")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

@Composable
private fun MacroField(
    label: String,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
    )
}
