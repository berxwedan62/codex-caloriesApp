package com.lokma.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lokma.app.data.local.entity.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items ORDER BY isFavorite DESC, name ASC")
    fun getAllFoods(): Flow<List<FoodItem>>

    @Query("SELECT * FROM food_items WHERE id = :id LIMIT 1")
    suspend fun getFoodById(id: Long): FoodItem?

    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%' ORDER BY isFavorite DESC, name ASC")
    fun searchFoods(query: String): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItem: FoodItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FoodItem>)

    @Update
    suspend fun update(foodItem: FoodItem)

    @Query("UPDATE food_items SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean)

    @Query("SELECT COUNT(*) FROM food_items")
    suspend fun count(): Int
}
