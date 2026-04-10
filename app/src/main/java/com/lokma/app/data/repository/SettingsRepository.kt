package com.lokma.app.data.repository

import com.lokma.app.data.local.dao.UserSettingsDao
import com.lokma.app.data.local.entity.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dao: UserSettingsDao) {
    fun observe(): Flow<UserSettings> = dao.observeSettings().map { it ?: UserSettings() }

    suspend fun updateDailyTarget(target: Int) {
        val current = dao.getSettings() ?: UserSettings()
        dao.insert(current.copy(dailyCalorieTarget = target))
    }

    suspend fun updateCalorieWarningThreshold(threshold: Int) {
        val current = dao.getSettings() ?: UserSettings()
        dao.insert(current.copy(calorieWarningThreshold = threshold))
    }

    suspend fun updateUseMetric(useMetric: Boolean) {
        val current = dao.getSettings() ?: UserSettings()
        dao.insert(current.copy(useMetricUnits = useMetric))
    }
}
