package com.lokma.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lokma.app.data.local.entity.MealEntry
import com.lokma.app.data.repository.FoodRepository
import com.lokma.app.data.repository.MealRepository
import com.lokma.app.domain.model.MealType
import com.lokma.app.domain.usecase.NutritionCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddMealViewModel(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository
) : ViewModel() {
    private val query = MutableStateFlow("")

    val foods = query.flatMapLatest { foodRepository.searchFoods(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun addMeal(
        foodId: Long,
        mealType: MealType = MealType.SNACK,
        grams: Float? = null
    ) {
        viewModelScope.launch {
            val food = foodRepository.getFoodById(foodId) ?: return@launch
            val effectiveGrams = grams ?: food.defaultGramAmount
            mealRepository.add(
                MealEntry(
                    date = LocalDate.now().toString(),
                    mealType = mealType.name,
                    foodItemId = foodId,
                    grams = effectiveGrams,
                    calculatedCalories = NutritionCalculator.calories(food, effectiveGrams),
                    calculatedProtein = NutritionCalculator.protein(food, effectiveGrams),
                    calculatedCarbs = NutritionCalculator.carbs(food, effectiveGrams),
                    calculatedFat = NutritionCalculator.fat(food, effectiveGrams)
                )
            )
        }
    }

    companion object {
        fun factory(foodRepository: FoodRepository, mealRepository: MealRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AddMealViewModel(foodRepository, mealRepository) as T
                }
            }
    }
}
