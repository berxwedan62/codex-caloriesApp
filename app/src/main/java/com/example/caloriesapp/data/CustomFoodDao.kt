package com.example.caloriesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomFoodDao {
    @Query("SELECT * FROM custom_foods ORDER BY isFavorite DESC, name COLLATE NOCASE ASC")
    fun observeFoods(): Flow<List<CustomFood>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: CustomFood): Long

    @Update
    suspend fun update(food: CustomFood)
}
