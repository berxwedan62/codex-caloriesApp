package com.lokma.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lokma.app.data.repository.MealEntryUi
import com.lokma.app.data.repository.MealRepository
import com.lokma.app.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val mealRepository: MealRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val today = LocalDate.now().toString()

    val uiState: StateFlow<HomeUiState> = combine(
        mealRepository.entriesByDate(today),
        settingsRepository.observe()
    ) { meals, settings ->
        val total = meals.sumOf { it.entry.calculatedCalories }
        val remainingCalories = settings.dailyCalorieTarget - total
        val remainingWarningState = when {
            total > settings.dailyCalorieTarget -> RemainingWarningState.OverTarget
            remainingCalories <= settings.calorieWarningThreshold -> RemainingWarningState.NearTarget
            else -> RemainingWarningState.Normal
        }
        HomeUiState(
            today = today,
            meals = meals,
            totalCalories = total,
            calorieTarget = settings.dailyCalorieTarget,
            remainingCalories = remainingCalories,
            calorieWarningThreshold = settings.calorieWarningThreshold,
            remainingWarningState = remainingWarningState
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState(today = today))

    fun delete(id: Long) {
        viewModelScope.launch {
            mealRepository.deleteById(id)
        }
    }

    companion object {
        fun factory(mealRepository: MealRepository, settingsRepository: SettingsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(mealRepository, settingsRepository) as T
                }
            }
    }
}

data class HomeUiState(
    val today: String,
    val meals: List<MealEntryUi> = emptyList(),
    val totalCalories: Int = 0,
    val calorieTarget: Int = 2200,
    val remainingCalories: Int = 2200,
    val calorieWarningThreshold: Int = 200,
    val remainingWarningState: RemainingWarningState = RemainingWarningState.Normal
)

enum class RemainingWarningState {
    Normal,
    NearTarget,
    OverTarget
}
