package com.lokma.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lokma.app.data.local.dao.FoodDao
import com.lokma.app.data.local.dao.MealDao
import com.lokma.app.data.local.dao.UserSettingsDao
import com.lokma.app.data.local.dao.WeightDao
import com.lokma.app.data.local.entity.FoodItem
import com.lokma.app.data.local.entity.MealEntry
import com.lokma.app.data.local.entity.UserSettings
import com.lokma.app.data.local.entity.WeightEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [FoodItem::class, MealEntry::class, WeightEntry::class, UserSettings::class],
    version = 1,
    exportSchema = false
)
abstract class LokmaDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun mealDao(): MealDao
    abstract fun weightDao(): WeightDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: LokmaDatabase? = null

        fun getDatabase(context: Context): LokmaDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LokmaDatabase::class.java,
                    "lokma_database"
                ).addCallback(SeedCallback).build()
                INSTANCE = instance
                instance
            }

        private object SeedCallback : RoomDatabase.Callback() {
            private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        seedFoodItems(database.foodDao())
                        database.userSettingsDao().insert(UserSettings())
                    }
                }
            }

            suspend fun seedFoodItems(foodDao: FoodDao) {
                if (foodDao.count() > 0) return

                val foods = listOf(
                    FoodItem(name = "Menemen", caloriesPer100g = 120, proteinPer100g = 6f, carbsPer100g = 4f, fatPer100g = 8f, defaultGramAmount = 220f),
                    FoodItem(name = "Mercimek Çorbası", caloriesPer100g = 70, proteinPer100g = 4f, carbsPer100g = 10f, fatPer100g = 1f, defaultGramAmount = 250f),
                    FoodItem(name = "Ezogelin Çorbası", caloriesPer100g = 80, proteinPer100g = 3f, carbsPer100g = 12f, fatPer100g = 2f, defaultGramAmount = 250f),
                    FoodItem(name = "Pirinç Pilavı", caloriesPer100g = 130, proteinPer100g = 2.5f, carbsPer100g = 28f, fatPer100g = 1f, defaultGramAmount = 180f),
                    FoodItem(name = "Bulgur Pilavı", caloriesPer100g = 110, proteinPer100g = 3f, carbsPer100g = 23f, fatPer100g = 1f, defaultGramAmount = 180f),
                    FoodItem(name = "Tavuk Şiş", caloriesPer100g = 165, proteinPer100g = 26f, carbsPer100g = 1f, fatPer100g = 6f, defaultGramAmount = 150f),
                    FoodItem(name = "Adana Kebap", caloriesPer100g = 250, proteinPer100g = 17f, carbsPer100g = 1f, fatPer100g = 20f, defaultGramAmount = 150f),
                    FoodItem(name = "İskender", caloriesPer100g = 220, proteinPer100g = 13f, carbsPer100g = 14f, fatPer100g = 12f, defaultGramAmount = 280f),
                    FoodItem(name = "Lahmacun", caloriesPer100g = 250, proteinPer100g = 10f, carbsPer100g = 30f, fatPer100g = 10f, defaultGramAmount = 120f),
                    FoodItem(name = "Pide (Kaşarlı)", caloriesPer100g = 280, proteinPer100g = 11f, carbsPer100g = 34f, fatPer100g = 11f, defaultGramAmount = 170f),
                    FoodItem(name = "Kuru Fasulye", caloriesPer100g = 140, proteinPer100g = 8f, carbsPer100g = 20f, fatPer100g = 3f, defaultGramAmount = 220f),
                    FoodItem(name = "Nohut Yemeği", caloriesPer100g = 150, proteinPer100g = 7f, carbsPer100g = 22f, fatPer100g = 4f, defaultGramAmount = 220f),
                    FoodItem(name = "İmam Bayıldı", caloriesPer100g = 90, proteinPer100g = 2f, carbsPer100g = 8f, fatPer100g = 6f, defaultGramAmount = 200f),
                    FoodItem(name = "Karnıyarık", caloriesPer100g = 160, proteinPer100g = 8f, carbsPer100g = 7f, fatPer100g = 11f, defaultGramAmount = 220f),
                    FoodItem(name = "Yoğurt", caloriesPer100g = 60, proteinPer100g = 3.5f, carbsPer100g = 4.5f, fatPer100g = 3f, defaultGramAmount = 200f),
                    FoodItem(name = "Ayran", caloriesPer100g = 37, proteinPer100g = 1.8f, carbsPer100g = 2.7f, fatPer100g = 1.5f, defaultGramAmount = 250f),
                    FoodItem(name = "Beyaz Peynir", caloriesPer100g = 260, proteinPer100g = 14f, carbsPer100g = 2f, fatPer100g = 22f, defaultGramAmount = 50f),
                    FoodItem(name = "Kaşar Peyniri", caloriesPer100g = 330, proteinPer100g = 25f, carbsPer100g = 2f, fatPer100g = 26f, defaultGramAmount = 40f),
                    FoodItem(name = "Zeytin", caloriesPer100g = 115, proteinPer100g = 0.8f, carbsPer100g = 6f, fatPer100g = 11f, defaultGramAmount = 30f),
                    FoodItem(name = "Simit", caloriesPer100g = 330, proteinPer100g = 9f, carbsPer100g = 62f, fatPer100g = 7f, defaultGramAmount = 100f),
                    FoodItem(name = "Poğaça", caloriesPer100g = 320, proteinPer100g = 8f, carbsPer100g = 35f, fatPer100g = 16f, defaultGramAmount = 90f),
                    FoodItem(name = "Börek (Peynirli)", caloriesPer100g = 310, proteinPer100g = 9f, carbsPer100g = 27f, fatPer100g = 18f, defaultGramAmount = 120f),
                    FoodItem(name = "Dolma", caloriesPer100g = 180, proteinPer100g = 5f, carbsPer100g = 22f, fatPer100g = 7f, defaultGramAmount = 160f),
                    FoodItem(name = "Sarma", caloriesPer100g = 145, proteinPer100g = 3f, carbsPer100g = 17f, fatPer100g = 7f, defaultGramAmount = 140f),
                    FoodItem(name = "Köfte", caloriesPer100g = 250, proteinPer100g = 17f, carbsPer100g = 6f, fatPer100g = 18f, defaultGramAmount = 140f),
                    FoodItem(name = "Izgara Balık", caloriesPer100g = 170, proteinPer100g = 23f, carbsPer100g = 0f, fatPer100g = 8f, defaultGramAmount = 180f),
                    FoodItem(name = "Mücver", caloriesPer100g = 190, proteinPer100g = 5f, carbsPer100g = 11f, fatPer100g = 14f, defaultGramAmount = 130f),
                    FoodItem(name = "Çoban Salata", caloriesPer100g = 40, proteinPer100g = 1f, carbsPer100g = 4f, fatPer100g = 2f, defaultGramAmount = 180f),
                    FoodItem(name = "Baklava", caloriesPer100g = 430, proteinPer100g = 5f, carbsPer100g = 50f, fatPer100g = 24f, defaultGramAmount = 60f),
                    FoodItem(name = "Lokum", caloriesPer100g = 360, proteinPer100g = 0f, carbsPer100g = 89f, fatPer100g = 0f, defaultGramAmount = 40f)
                )

                foodDao.insertAll(foods)
            }
        }
    }
}
