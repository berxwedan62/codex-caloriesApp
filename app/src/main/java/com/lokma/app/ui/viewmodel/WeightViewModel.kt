package com.lokma.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lokma.app.data.local.entity.WeightEntry
import com.lokma.app.data.repository.WeightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class WeightViewModel(private val repository: WeightRepository) : ViewModel() {
    val entries: StateFlow<List<WeightEntry>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addOrUpdateToday(weightKg: Float) {
        viewModelScope.launch {
            repository.upsert(LocalDate.now().toString(), weightKg)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch { repository.deleteById(id) }
    }

    companion object {
        fun factory(repository: WeightRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WeightViewModel(repository) as T
                }
            }
    }
}
