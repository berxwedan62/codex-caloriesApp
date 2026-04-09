package com.example.calories.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val resetDemoData: suspend () -> Unit,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = settingsPreferences.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState(),
        )

    private val _messages = MutableStateFlow<String?>(null)
    val messages = _messages.asStateFlow()

    fun onDailyCalorieTargetChanged(value: String) {
        val target = value.toIntOrNull() ?: return
        if (target <= 0) return

        viewModelScope.launch {
            settingsPreferences.setDailyCalorieTarget(target)
        }
    }

    fun onResetDemoDataClick() {
        viewModelScope.launch {
            resetDemoData()
            _messages.value = "Demo data reset"
        }
    }

    fun onPremiumChanged(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setPremiumEnabled(enabled)
        }
    }

    fun onExportChanged(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setExportEnabled(enabled)
        }
    }

    fun onUnitPreferenceChanged(unitPreference: UnitPreference) {
        viewModelScope.launch {
            settingsPreferences.setUnitPreference(unitPreference)
        }
    }

    fun onMessageShown() {
        _messages.value = null
    }
}
