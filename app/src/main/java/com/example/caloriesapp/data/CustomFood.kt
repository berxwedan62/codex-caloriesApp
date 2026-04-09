package com.example.caloriesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_foods")
data class CustomFood(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,
    val defaultGramAmount: Double,
    val isFavorite: Boolean,
)
