package com.lokma.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.lokma.app.data.local.dao.FoodDao;
import com.lokma.app.data.local.dao.FoodDao_Impl;
import com.lokma.app.data.local.dao.MealDao;
import com.lokma.app.data.local.dao.MealDao_Impl;
import com.lokma.app.data.local.dao.UserSettingsDao;
import com.lokma.app.data.local.dao.UserSettingsDao_Impl;
import com.lokma.app.data.local.dao.WeightDao;
import com.lokma.app.data.local.dao.WeightDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LokmaDatabase_Impl extends LokmaDatabase {
  private volatile FoodDao _foodDao;

  private volatile MealDao _mealDao;

  private volatile WeightDao _weightDao;

  private volatile UserSettingsDao _userSettingsDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `food_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `caloriesPer100g` INTEGER NOT NULL, `proteinPer100g` REAL NOT NULL, `carbsPer100g` REAL NOT NULL, `fatPer100g` REAL NOT NULL, `isFavorite` INTEGER NOT NULL, `defaultGramAmount` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `meal_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `mealType` TEXT NOT NULL, `foodItemId` INTEGER NOT NULL, `grams` REAL NOT NULL, `calculatedCalories` INTEGER NOT NULL, `calculatedProtein` REAL NOT NULL, `calculatedCarbs` REAL NOT NULL, `calculatedFat` REAL NOT NULL, FOREIGN KEY(`foodItemId`) REFERENCES `food_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_entries_foodItemId` ON `meal_entries` (`foodItemId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_meal_entries_date` ON `meal_entries` (`date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `weight_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `weightKg` REAL NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_weight_entries_date` ON `weight_entries` (`date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_settings` (`id` INTEGER NOT NULL, `dailyCalorieTarget` INTEGER NOT NULL, `premiumUnlocked` INTEGER NOT NULL, `useMetricUnits` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '79b3b9cf8714d16310e5f02bd328e6ce')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `food_items`");
        db.execSQL("DROP TABLE IF EXISTS `meal_entries`");
        db.execSQL("DROP TABLE IF EXISTS `weight_entries`");
        db.execSQL("DROP TABLE IF EXISTS `user_settings`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFoodItems = new HashMap<String, TableInfo.Column>(8);
        _columnsFoodItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodItems.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodItems.put("caloriesPer100g", new TableInfo.Column("caloriesPer100g", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodItems.put("proteinPer100g", new TableInfo.Column("proteinPer100g", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodItems.put("carbsPer100g", new TableInfo.Column("carbsPer100g", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodItems.put("fatPer100g", new TableInfo.Column("fatPer100g", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodItems.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodItems.put("defaultGramAmount", new TableInfo.Column("defaultGramAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFoodItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFoodItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFoodItems = new TableInfo("food_items", _columnsFoodItems, _foreignKeysFoodItems, _indicesFoodItems);
        final TableInfo _existingFoodItems = TableInfo.read(db, "food_items");
        if (!_infoFoodItems.equals(_existingFoodItems)) {
          return new RoomOpenHelper.ValidationResult(false, "food_items(com.lokma.app.data.local.entity.FoodItem).\n"
                  + " Expected:\n" + _infoFoodItems + "\n"
                  + " Found:\n" + _existingFoodItems);
        }
        final HashMap<String, TableInfo.Column> _columnsMealEntries = new HashMap<String, TableInfo.Column>(9);
        _columnsMealEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("mealType", new TableInfo.Column("mealType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("foodItemId", new TableInfo.Column("foodItemId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("grams", new TableInfo.Column("grams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("calculatedCalories", new TableInfo.Column("calculatedCalories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("calculatedProtein", new TableInfo.Column("calculatedProtein", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("calculatedCarbs", new TableInfo.Column("calculatedCarbs", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealEntries.put("calculatedFat", new TableInfo.Column("calculatedFat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMealEntries = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMealEntries.add(new TableInfo.ForeignKey("food_items", "CASCADE", "NO ACTION", Arrays.asList("foodItemId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMealEntries = new HashSet<TableInfo.Index>(2);
        _indicesMealEntries.add(new TableInfo.Index("index_meal_entries_foodItemId", false, Arrays.asList("foodItemId"), Arrays.asList("ASC")));
        _indicesMealEntries.add(new TableInfo.Index("index_meal_entries_date", false, Arrays.asList("date"), Arrays.asList("ASC")));
        final TableInfo _infoMealEntries = new TableInfo("meal_entries", _columnsMealEntries, _foreignKeysMealEntries, _indicesMealEntries);
        final TableInfo _existingMealEntries = TableInfo.read(db, "meal_entries");
        if (!_infoMealEntries.equals(_existingMealEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "meal_entries(com.lokma.app.data.local.entity.MealEntry).\n"
                  + " Expected:\n" + _infoMealEntries + "\n"
                  + " Found:\n" + _existingMealEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsWeightEntries = new HashMap<String, TableInfo.Column>(3);
        _columnsWeightEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeightEntries.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeightEntries.put("weightKg", new TableInfo.Column("weightKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWeightEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWeightEntries = new HashSet<TableInfo.Index>(1);
        _indicesWeightEntries.add(new TableInfo.Index("index_weight_entries_date", false, Arrays.asList("date"), Arrays.asList("ASC")));
        final TableInfo _infoWeightEntries = new TableInfo("weight_entries", _columnsWeightEntries, _foreignKeysWeightEntries, _indicesWeightEntries);
        final TableInfo _existingWeightEntries = TableInfo.read(db, "weight_entries");
        if (!_infoWeightEntries.equals(_existingWeightEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "weight_entries(com.lokma.app.data.local.entity.WeightEntry).\n"
                  + " Expected:\n" + _infoWeightEntries + "\n"
                  + " Found:\n" + _existingWeightEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsUserSettings = new HashMap<String, TableInfo.Column>(4);
        _columnsUserSettings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("dailyCalorieTarget", new TableInfo.Column("dailyCalorieTarget", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("premiumUnlocked", new TableInfo.Column("premiumUnlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("useMetricUnits", new TableInfo.Column("useMetricUnits", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserSettings = new TableInfo("user_settings", _columnsUserSettings, _foreignKeysUserSettings, _indicesUserSettings);
        final TableInfo _existingUserSettings = TableInfo.read(db, "user_settings");
        if (!_infoUserSettings.equals(_existingUserSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "user_settings(com.lokma.app.data.local.entity.UserSettings).\n"
                  + " Expected:\n" + _infoUserSettings + "\n"
                  + " Found:\n" + _existingUserSettings);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "79b3b9cf8714d16310e5f02bd328e6ce", "d31370b7f973643c00cbff50f28e98ec");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "food_items","meal_entries","weight_entries","user_settings");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `food_items`");
      _db.execSQL("DELETE FROM `meal_entries`");
      _db.execSQL("DELETE FROM `weight_entries`");
      _db.execSQL("DELETE FROM `user_settings`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FoodDao.class, FoodDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MealDao.class, MealDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WeightDao.class, WeightDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserSettingsDao.class, UserSettingsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FoodDao foodDao() {
    if (_foodDao != null) {
      return _foodDao;
    } else {
      synchronized(this) {
        if(_foodDao == null) {
          _foodDao = new FoodDao_Impl(this);
        }
        return _foodDao;
      }
    }
  }

  @Override
  public MealDao mealDao() {
    if (_mealDao != null) {
      return _mealDao;
    } else {
      synchronized(this) {
        if(_mealDao == null) {
          _mealDao = new MealDao_Impl(this);
        }
        return _mealDao;
      }
    }
  }

  @Override
  public WeightDao weightDao() {
    if (_weightDao != null) {
      return _weightDao;
    } else {
      synchronized(this) {
        if(_weightDao == null) {
          _weightDao = new WeightDao_Impl(this);
        }
        return _weightDao;
      }
    }
  }

  @Override
  public UserSettingsDao userSettingsDao() {
    if (_userSettingsDao != null) {
      return _userSettingsDao;
    } else {
      synchronized(this) {
        if(_userSettingsDao == null) {
          _userSettingsDao = new UserSettingsDao_Impl(this);
        }
        return _userSettingsDao;
      }
    }
  }
}
