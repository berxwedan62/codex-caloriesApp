package com.example.calories.settings

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

private const val SETTINGS_STORE_NAME = "settings"

enum class UnitPreference {
    METRIC,
    IMPERIAL,
}

data class SettingsUiState(
    val dailyCalorieTarget: Int = 2000,
    val isPremiumEnabled: Boolean = false,
    val exportEnabled: Boolean = false,
    val unitPreference: UnitPreference = UnitPreference.METRIC,
)

class SettingsPreferences(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SETTINGS_STORE_NAME, Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(readSettings())

    val settings: Flow<SettingsUiState> = _settings.asStateFlow().map { it }

    private fun readSettings(): SettingsUiState {
        val storedUnitName = sharedPreferences.getString(Keys.UNIT_PREFERENCE, UnitPreference.METRIC.name)
        val parsedUnit = UnitPreference.entries.firstOrNull { it.name == storedUnitName } ?: UnitPreference.METRIC

        return SettingsUiState(
            dailyCalorieTarget = sharedPreferences.getInt(Keys.DAILY_CALORIE_TARGET, 2000),
            isPremiumEnabled = sharedPreferences.getBoolean(Keys.PREMIUM_ENABLED, false),
            exportEnabled = sharedPreferences.getBoolean(Keys.EXPORT_ENABLED, false),
            unitPreference = parsedUnit,
        )
    }

    suspend fun setDailyCalorieTarget(value: Int) {
        sharedPreferences.edit().putInt(Keys.DAILY_CALORIE_TARGET, value).apply()
        _settings.value = readSettings()
    }

    suspend fun setPremiumEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(Keys.PREMIUM_ENABLED, enabled).apply()
        _settings.value = readSettings()
    }

    suspend fun setExportEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(Keys.EXPORT_ENABLED, enabled).apply()
        _settings.value = readSettings()
    }

    suspend fun setUnitPreference(preference: UnitPreference) {
        sharedPreferences.edit().putString(Keys.UNIT_PREFERENCE, preference.name).apply()
        _settings.value = readSettings()
    }

    private object Keys {
<<<<<<< codex/fix-reported-issues-4tll6s
        const val DAILY_CALORIE_TARGET = "daily_calorie_target"
        const val PREMIUM_ENABLED = "premium_enabled"
        const val EXPORT_ENABLED = "export_enabled"
        const val UNIT_PREFERENCE = "unit_preference"
=======
        val DAILY_CALORIE_TARGET: Preferences.Key<Int> = intPreferencesKey("daily_calorie_target")
        val PREMIUM_ENABLED: Preferences.Key<Boolean> = booleanPreferencesKey("premium_enabled")
        val EXPORT_ENABLED: Preferences.Key<Boolean> = booleanPreferencesKey("export_enabled")
        val UNIT_PREFERENCE: Preferences.Key<String> = stringPreferencesKey("unit_preference")
>>>>>>> main
    }
}
