package com.lokma.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey val id: Int = 1,
    val dailyCalorieTarget: Int = 2200,
    val premiumUnlocked: Boolean = false,
    val useMetricUnits: Boolean = true
)
