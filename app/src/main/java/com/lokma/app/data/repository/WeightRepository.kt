package com.lokma.app.data.repository

import com.lokma.app.data.local.dao.WeightDao
import com.lokma.app.data.local.entity.WeightEntry
import kotlinx.coroutines.flow.Flow

class WeightRepository(private val dao: WeightDao) {
    fun getAll(): Flow<List<WeightEntry>> = dao.getAll()

    suspend fun upsert(date: String, kg: Float) {
        val existing = dao.getByDate(date)
        if (existing == null) {
            dao.insert(WeightEntry(date = date, weightKg = kg))
        } else {
            dao.update(existing.copy(weightKg = kg))
        }
    }

    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
