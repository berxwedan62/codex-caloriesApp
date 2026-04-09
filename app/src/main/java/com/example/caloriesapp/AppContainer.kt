package com.example.caloriesapp

import android.content.Context
import androidx.room.Room
import com.example.caloriesapp.data.AppDatabase
import com.example.caloriesapp.data.FoodRepository

class AppContainer(context: Context) {
    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "calories.db",
    ).build()

    val foodRepository = FoodRepository(db.customFoodDao())
}
