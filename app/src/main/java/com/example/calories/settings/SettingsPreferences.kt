package com.example.calories.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val SETTINGS_STORE_NAME = "settings"

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_STORE_NAME)

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

    val settings: Flow<SettingsUiState> = context.settingsDataStore.data.map { prefs ->
        SettingsUiState(
            dailyCalorieTarget = prefs[Keys.DAILY_CALORIE_TARGET] ?: 2000,
            isPremiumEnabled = prefs[Keys.PREMIUM_ENABLED] ?: false,
            exportEnabled = prefs[Keys.EXPORT_ENABLED] ?: false,
            unitPreference = prefs[Keys.UNIT_PREFERENCE]
                ?.let { value -> UnitPreference.entries.firstOrNull { it.name == value } }
                ?: UnitPreference.METRIC,
        )
    }

    suspend fun setDailyCalorieTarget(value: Int) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.DAILY_CALORIE_TARGET] = value
        }
    }

    suspend fun setPremiumEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.PREMIUM_ENABLED] = enabled
        }
    }

    suspend fun setExportEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.EXPORT_ENABLED] = enabled
        }
    }

    suspend fun setUnitPreference(preference: UnitPreference) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.UNIT_PREFERENCE] = preference.name
        }
    }

    private object Keys {
        val DAILY_CALORIE_TARGET: Preferences.Key<Int> = intPreferencesKey("daily_calorie_target")
        val PREMIUM_ENABLED: Preferences.Key<Boolean> = booleanPreferencesKey("premium_enabled")
        val EXPORT_ENABLED: Preferences.Key<Boolean> = booleanPreferencesKey("export_enabled")
        val UNIT_PREFERENCE: Preferences.Key<String> = stringPreferencesKey("unit_preference")
    }
}
