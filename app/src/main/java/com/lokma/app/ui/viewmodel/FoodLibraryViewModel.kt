package com.lokma.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lokma.app.data.local.entity.FoodItem
import com.lokma.app.data.repository.FoodRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FoodLibraryViewModel(private val repository: FoodRepository) : ViewModel() {
    val foods: StateFlow<List<FoodItem>> = repository.allFoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun toggleFavorite(foodItem: FoodItem) {
        viewModelScope.launch {
            repository.toggleFavorite(foodItem.id, foodItem.isFavorite)
        }
    }

    fun addCustomFood(name: String, calories: Int, protein: Float, carbs: Float, fat: Float) {
        viewModelScope.launch {
            repository.addFood(
                FoodItem(
                    name = name,
                    caloriesPer100g = calories,
                    proteinPer100g = protein,
                    carbsPer100g = carbs,
                    fatPer100g = fat,
                    defaultGramAmount = 100f
                )
            )
        }
    }

    companion object {
        fun factory(repository: FoodRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FoodLibraryViewModel(repository) as T
                }
            }
    }
}
