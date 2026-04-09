package com.lokma.app

import android.app.Application
import com.lokma.app.data.local.LokmaDatabase
import com.lokma.app.data.repository.FoodRepository
import com.lokma.app.data.repository.MealRepository
import com.lokma.app.data.repository.SettingsRepository
import com.lokma.app.data.repository.WeightRepository

class LokmaApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

class AppContainer(application: Application) {
    private val database = LokmaDatabase.getDatabase(application)

    val foodRepository = FoodRepository(database.foodDao())
    val mealRepository = MealRepository(database.mealDao(), database.foodDao())
    val weightRepository = WeightRepository(database.weightDao())
    val settingsRepository = SettingsRepository(database.userSettingsDao())
}
