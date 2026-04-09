package com.lokma.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lokma.app.data.local.entity.MealEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meal_entries WHERE date = :date ORDER BY id DESC")
    fun getByDate(date: String): Flow<List<MealEntry>>

    @Query("SELECT * FROM meal_entries WHERE date BETWEEN :fromDate AND :toDate ORDER BY date DESC, id DESC")
    fun getByDateRange(fromDate: String, toDate: String): Flow<List<MealEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MealEntry)

    @Update
    suspend fun update(entry: MealEntry)

    @Delete
    suspend fun delete(entry: MealEntry)

    @Query("DELETE FROM meal_entries WHERE id = :id")
    suspend fun deleteById(id: Long)
}
