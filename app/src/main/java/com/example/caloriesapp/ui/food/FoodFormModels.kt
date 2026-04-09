package com.example.caloriesapp.ui.food

import com.example.caloriesapp.data.CustomFood

data class FoodFormUiState(
    val id: Long = 0,
    val name: String = "",
    val caloriesPer100g: String = "",
    val proteinPer100g: String = "",
    val carbsPer100g: String = "",
    val fatPer100g: String = "",
    val defaultGramAmount: String = "",
    val isFavorite: Boolean = false,
    val errors: Map<String, String> = emptyMap(),
) {
    fun toFoodOrNull(): CustomFood? {
        val calories = caloriesPer100g.toDoubleOrNull() ?: return null
        val protein = proteinPer100g.toDoubleOrNull() ?: return null
        val carbs = carbsPer100g.toDoubleOrNull() ?: return null
        val fat = fatPer100g.toDoubleOrNull() ?: return null
        val grams = defaultGramAmount.toDoubleOrNull() ?: return null

        return CustomFood(
            id = id,
            name = name.trim(),
            caloriesPer100g = calories,
            proteinPer100g = protein,
            carbsPer100g = carbs,
            fatPer100g = fat,
            defaultGramAmount = grams,
            isFavorite = isFavorite,
        )
    }

    companion object {
        fun from(food: CustomFood): FoodFormUiState = FoodFormUiState(
            id = food.id,
            name = food.name,
            caloriesPer100g = food.caloriesPer100g.toString(),
            proteinPer100g = food.proteinPer100g.toString(),
            carbsPer100g = food.carbsPer100g.toString(),
            fatPer100g = food.fatPer100g.toString(),
            defaultGramAmount = food.defaultGramAmount.toString(),
            isFavorite = food.isFavorite,
        )
    }
}
