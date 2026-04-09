package com.example.caloriesapp.ui.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caloriesapp.data.CustomFood
import com.example.caloriesapp.data.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoodLibraryViewModel(
    private val repository: FoodRepository,
) : ViewModel() {

    val foods = repository.observeFoods().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    private val _editorState = MutableStateFlow<FoodFormUiState?>(null)
    val editorState: StateFlow<FoodFormUiState?> = _editorState.asStateFlow()

    fun openCreate() {
        _editorState.value = FoodFormUiState()
    }

    fun openEdit(food: CustomFood) {
        _editorState.value = FoodFormUiState.from(food)
    }

    fun dismissEditor() {
        _editorState.value = null
    }

    fun updateName(value: String) = mutate { copy(name = value) }
    fun updateCalories(value: String) = mutate { copy(caloriesPer100g = value) }
    fun updateProtein(value: String) = mutate { copy(proteinPer100g = value) }
    fun updateCarbs(value: String) = mutate { copy(carbsPer100g = value) }
    fun updateFat(value: String) = mutate { copy(fatPer100g = value) }
    fun updateDefaultGramAmount(value: String) = mutate { copy(defaultGramAmount = value) }
    fun updateFavorite(value: Boolean) = mutate { copy(isFavorite = value) }

    fun save() {
        val current = _editorState.value ?: return
        val errors = validate(current)
        if (errors.isNotEmpty()) {
            _editorState.value = current.copy(errors = errors)
            return
        }

        val food = current.toFoodOrNull() ?: return
        viewModelScope.launch {
            repository.save(food)
            _editorState.value = null
        }
    }

    private fun validate(state: FoodFormUiState): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (state.name.isBlank()) {
            errors["name"] = "Name is required"
        }

        listOf(
            "calories" to state.caloriesPer100g,
            "protein" to state.proteinPer100g,
            "carbs" to state.carbsPer100g,
            "fat" to state.fatPer100g,
            "defaultGramAmount" to state.defaultGramAmount,
        ).forEach { (field, value) ->
            val number = value.toDoubleOrNull()
            if (number == null) {
                errors[field] = "Enter a valid number"
            } else if (number < 0) {
                errors[field] = "Cannot be negative"
            }
        }

        return errors
    }

    private fun mutate(block: FoodFormUiState.() -> FoodFormUiState) {
        _editorState.update { state ->
            state?.let {
                block(it).copy(errors = it.errors - setOf(
                    "name",
                    "calories",
                    "protein",
                    "carbs",
                    "fat",
                    "defaultGramAmount",
                ))
            }
        }
    }

    class Factory(
        private val repository: FoodRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FoodLibraryViewModel(repository) as T
        }
    }
}
