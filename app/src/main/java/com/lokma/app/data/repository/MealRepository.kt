package com.lokma.app.data.repository

import com.lokma.app.data.local.dao.FoodDao
import com.lokma.app.data.local.dao.MealDao
import com.lokma.app.data.local.entity.MealEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class MealEntryUi(
    val entry: MealEntry,
    val foodName: String
)

class MealRepository(
    private val mealDao: MealDao,
    private val foodDao: FoodDao
) {
    fun entriesByDate(date: String): Flow<List<MealEntryUi>> =
        combine(mealDao.getByDate(date), foodDao.getAllFoods()) { meals, foods ->
            val byId = foods.associateBy { it.id }
            meals.map {
                MealEntryUi(
                    entry = it,
                    foodName = byId[it.foodItemId]?.name ?: "Unknown food"
                )
            }
        }

    fun range(fromDate: String, toDate: String): Flow<List<MealEntryUi>> =
        combine(mealDao.getByDateRange(fromDate, toDate), foodDao.getAllFoods()) { meals, foods ->
            val byId = foods.associateBy { it.id }
            meals.map {
                MealEntryUi(entry = it, foodName = byId[it.foodItemId]?.name ?: "Unknown food")
            }
        }

    suspend fun add(entry: MealEntry) = mealDao.insert(entry)

    suspend fun update(entry: MealEntry) = mealDao.update(entry)

    suspend fun delete(entry: MealEntry) = mealDao.delete(entry)

    suspend fun deleteById(id: Long) = mealDao.deleteById(id)
}
