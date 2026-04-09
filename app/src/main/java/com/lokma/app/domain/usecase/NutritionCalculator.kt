package com.lokma.app.domain.usecase

import com.lokma.app.data.local.entity.FoodItem
import kotlin.math.roundToInt

object NutritionCalculator {
    fun calories(food: FoodItem, grams: Float): Int = ((food.caloriesPer100g * grams) / 100f).roundToInt()
    fun protein(food: FoodItem, grams: Float): Float = ((food.proteinPer100g * grams) / 100f)
    fun carbs(food: FoodItem, grams: Float): Float = ((food.carbsPer100g * grams) / 100f)
    fun fat(food: FoodItem, grams: Float): Float = ((food.fatPer100g * grams) / 100f)
}
