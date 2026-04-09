package com.lokma.app.data.repository

import com.lokma.app.data.local.dao.FoodDao
import com.lokma.app.data.local.entity.FoodItem
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val dao: FoodDao) {
    fun allFoods(): Flow<List<FoodItem>> = dao.getAllFoods()

    fun searchFoods(query: String): Flow<List<FoodItem>> =
        if (query.isBlank()) dao.getAllFoods() else dao.searchFoods(query)

    suspend fun addFood(foodItem: FoodItem) = dao.insert(foodItem)

    suspend fun toggleFavorite(foodId: Long, current: Boolean) = dao.setFavorite(foodId, !current)

    suspend fun getFoodById(id: Long): FoodItem? = dao.getFoodById(id)
}
