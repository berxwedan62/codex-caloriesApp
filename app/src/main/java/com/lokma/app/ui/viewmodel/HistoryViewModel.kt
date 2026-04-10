package com.lokma.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lokma.app.data.repository.MealEntryUi
import com.lokma.app.data.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class HistoryViewModel(private val mealRepository: MealRepository) : ViewModel() {
    private val selectedDate = MutableStateFlow(LocalDate.now().toString())

    val entries: StateFlow<List<MealEntryUi>> = selectedDate
        .flatMapLatest { mealRepository.entriesByDate(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun date(): String = selectedDate.value

    fun shiftDays(days: Long) {
        selectedDate.value = LocalDate.parse(selectedDate.value).plusDays(days).toString()
    }

    companion object {
        fun factory(repository: MealRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(repository) as T
                }
            }
    }
}
