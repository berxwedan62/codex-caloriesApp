package com.lokma.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_entries",
    foreignKeys = [
        ForeignKey(
            entity = FoodItem::class,
            parentColumns = ["id"],
            childColumns = ["foodItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("foodItemId"), Index("date")]
)
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val mealType: String,
    val foodItemId: Long,
    val grams: Float,
    val calculatedCalories: Int,
    val calculatedProtein: Float,
    val calculatedCarbs: Float,
    val calculatedFat: Float
)
