package com.lokma.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lokma.app.data.local.entity.UserSettings
import com.lokma.app.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    val settings: StateFlow<UserSettings> = repository.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    fun updateTarget(target: Int) {
        viewModelScope.launch { repository.updateDailyTarget(target) }
    }

    fun updateWarningThreshold(threshold: Int) {
        viewModelScope.launch { repository.updateWarningThreshold(threshold) }
    }

    fun toggleMetric(useMetric: Boolean) {
        viewModelScope.launch { repository.updateUseMetric(useMetric) }
    }

    companion object {
        fun factory(repository: SettingsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(repository) as T
                }
            }
    }
}
