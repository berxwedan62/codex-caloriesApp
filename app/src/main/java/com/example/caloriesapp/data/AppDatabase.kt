package com.example.caloriesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CustomFood::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customFoodDao(): CustomFoodDao
}
