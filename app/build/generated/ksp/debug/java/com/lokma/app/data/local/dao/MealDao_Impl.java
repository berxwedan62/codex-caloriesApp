package com.lokma.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lokma.app.data.local.entity.MealEntry;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MealDao_Impl implements MealDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MealEntry> __insertionAdapterOfMealEntry;

  private final EntityDeletionOrUpdateAdapter<MealEntry> __deletionAdapterOfMealEntry;

  private final EntityDeletionOrUpdateAdapter<MealEntry> __updateAdapterOfMealEntry;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public MealDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMealEntry = new EntityInsertionAdapter<MealEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `meal_entries` (`id`,`date`,`mealType`,`foodItemId`,`grams`,`calculatedCalories`,`calculatedProtein`,`calculatedCarbs`,`calculatedFat`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealEntry entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindString(3, entity.getMealType());
        statement.bindLong(4, entity.getFoodItemId());
        statement.bindDouble(5, entity.getGrams());
        statement.bindLong(6, entity.getCalculatedCalories());
        statement.bindDouble(7, entity.getCalculatedProtein());
        statement.bindDouble(8, entity.getCalculatedCarbs());
        statement.bindDouble(9, entity.getCalculatedFat());
      }
    };
    this.__deletionAdapterOfMealEntry = new EntityDeletionOrUpdateAdapter<MealEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `meal_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealEntry entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMealEntry = new EntityDeletionOrUpdateAdapter<MealEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `meal_entries` SET `id` = ?,`date` = ?,`mealType` = ?,`foodItemId` = ?,`grams` = ?,`calculatedCalories` = ?,`calculatedProtein` = ?,`calculatedCarbs` = ?,`calculatedFat` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealEntry entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindString(3, entity.getMealType());
        statement.bindLong(4, entity.getFoodItemId());
        statement.bindDouble(5, entity.getGrams());
        statement.bindLong(6, entity.getCalculatedCalories());
        statement.bindDouble(7, entity.getCalculatedProtein());
        statement.bindDouble(8, entity.getCalculatedCarbs());
        statement.bindDouble(9, entity.getCalculatedFat());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM meal_entries WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MealEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMealEntry.insert(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final MealEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMealEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final MealEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMealEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MealEntry>> getByDate(final String date) {
    final String _sql = "SELECT * FROM meal_entries WHERE date = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meal_entries"}, new Callable<List<MealEntry>>() {
      @Override
      @NonNull
      public List<MealEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfFoodItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "foodItemId");
          final int _cursorIndexOfGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "grams");
          final int _cursorIndexOfCalculatedCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedCalories");
          final int _cursorIndexOfCalculatedProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedProtein");
          final int _cursorIndexOfCalculatedCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedCarbs");
          final int _cursorIndexOfCalculatedFat = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedFat");
          final List<MealEntry> _result = new ArrayList<MealEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final long _tmpFoodItemId;
            _tmpFoodItemId = _cursor.getLong(_cursorIndexOfFoodItemId);
            final float _tmpGrams;
            _tmpGrams = _cursor.getFloat(_cursorIndexOfGrams);
            final int _tmpCalculatedCalories;
            _tmpCalculatedCalories = _cursor.getInt(_cursorIndexOfCalculatedCalories);
            final float _tmpCalculatedProtein;
            _tmpCalculatedProtein = _cursor.getFloat(_cursorIndexOfCalculatedProtein);
            final float _tmpCalculatedCarbs;
            _tmpCalculatedCarbs = _cursor.getFloat(_cursorIndexOfCalculatedCarbs);
            final float _tmpCalculatedFat;
            _tmpCalculatedFat = _cursor.getFloat(_cursorIndexOfCalculatedFat);
            _item = new MealEntry(_tmpId,_tmpDate,_tmpMealType,_tmpFoodItemId,_tmpGrams,_tmpCalculatedCalories,_tmpCalculatedProtein,_tmpCalculatedCarbs,_tmpCalculatedFat);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MealEntry>> getByDateRange(final String fromDate, final String toDate) {
    final String _sql = "SELECT * FROM meal_entries WHERE date BETWEEN ? AND ? ORDER BY date DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, fromDate);
    _argIndex = 2;
    _statement.bindString(_argIndex, toDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meal_entries"}, new Callable<List<MealEntry>>() {
      @Override
      @NonNull
      public List<MealEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfFoodItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "foodItemId");
          final int _cursorIndexOfGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "grams");
          final int _cursorIndexOfCalculatedCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedCalories");
          final int _cursorIndexOfCalculatedProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedProtein");
          final int _cursorIndexOfCalculatedCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedCarbs");
          final int _cursorIndexOfCalculatedFat = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedFat");
          final List<MealEntry> _result = new ArrayList<MealEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final long _tmpFoodItemId;
            _tmpFoodItemId = _cursor.getLong(_cursorIndexOfFoodItemId);
            final float _tmpGrams;
            _tmpGrams = _cursor.getFloat(_cursorIndexOfGrams);
            final int _tmpCalculatedCalories;
            _tmpCalculatedCalories = _cursor.getInt(_cursorIndexOfCalculatedCalories);
            final float _tmpCalculatedProtein;
            _tmpCalculatedProtein = _cursor.getFloat(_cursorIndexOfCalculatedProtein);
            final float _tmpCalculatedCarbs;
            _tmpCalculatedCarbs = _cursor.getFloat(_cursorIndexOfCalculatedCarbs);
            final float _tmpCalculatedFat;
            _tmpCalculatedFat = _cursor.getFloat(_cursorIndexOfCalculatedFat);
            _item = new MealEntry(_tmpId,_tmpDate,_tmpMealType,_tmpFoodItemId,_tmpGrams,_tmpCalculatedCalories,_tmpCalculatedProtein,_tmpCalculatedCarbs,_tmpCalculatedFat);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
