package com.example.caloriesapp.data

import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val customFoodDao: CustomFoodDao,
) {
    fun observeFoods(): Flow<List<CustomFood>> = customFoodDao.observeFoods()

    suspend fun save(food: CustomFood) {
        if (food.id == 0L) {
            customFoodDao.insert(food)
        } else {
            customFoodDao.update(food)
        }
    }
}
