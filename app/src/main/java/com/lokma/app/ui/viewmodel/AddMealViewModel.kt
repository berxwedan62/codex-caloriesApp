package com.lokma.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lokma.app.data.local.entity.MealEntry
import com.lokma.app.data.repository.FoodRepository
import com.lokma.app.data.repository.MealRepository
import com.lokma.app.data.repository.SettingsRepository
import com.lokma.app.domain.model.MealType
import com.lokma.app.domain.usecase.NutritionCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddMealViewModel(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val today = LocalDate.now().toString()

    val foods = query.flatMapLatest { foodRepository.searchFoods(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val uiState: StateFlow<AddMealUiState> = combine(
        mealRepository.entriesByDate(today),
        settingsRepository.observe()
    ) { meals, settings ->
        val totalCalories = meals.sumOf { it.entry.calculatedCalories }
        AddMealUiState(
            totalCalories = totalCalories,
            calorieTarget = settings.dailyCalorieTarget,
            calorieWarningThreshold = settings.calorieWarningThreshold
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AddMealUiState())

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun addMeal(
        foodId: Long,
        mealType: MealType = MealType.SNACK,
        grams: Float
    ) {
        viewModelScope.launch {
            val food = foodRepository.getFoodById(foodId) ?: return@launch
            mealRepository.add(
                MealEntry(
                    date = today,
                    mealType = mealType.name,
                    foodItemId = foodId,
                    grams = grams,
                    calculatedCalories = NutritionCalculator.calories(food, grams),
                    calculatedProtein = NutritionCalculator.protein(food, grams),
                    calculatedCarbs = NutritionCalculator.carbs(food, grams),
                    calculatedFat = NutritionCalculator.fat(food, grams)
                )
            )
        }
    }

    companion object {
        fun factory(
            foodRepository: FoodRepository,
            mealRepository: MealRepository,
            settingsRepository: SettingsRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AddMealViewModel(foodRepository, mealRepository, settingsRepository) as T
                }
            }
    }
}

data class AddMealUiState(
    val totalCalories: Int = 0,
    val calorieTarget: Int = 2200,
    val calorieWarningThreshold: Int = 300
) {
    val remainingCalories: Int
        get() = calorieTarget - totalCalories
}
