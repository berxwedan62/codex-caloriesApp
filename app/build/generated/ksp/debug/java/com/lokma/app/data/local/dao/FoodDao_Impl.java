package com.lokma.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lokma.app.data.local.entity.FoodItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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
public final class FoodDao_Impl implements FoodDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FoodItem> __insertionAdapterOfFoodItem;

  private final EntityDeletionOrUpdateAdapter<FoodItem> __updateAdapterOfFoodItem;

  private final SharedSQLiteStatement __preparedStmtOfSetFavorite;

  public FoodDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFoodItem = new EntityInsertionAdapter<FoodItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `food_items` (`id`,`name`,`caloriesPer100g`,`proteinPer100g`,`carbsPer100g`,`fatPer100g`,`isFavorite`,`defaultGramAmount`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getCaloriesPer100g());
        statement.bindDouble(4, entity.getProteinPer100g());
        statement.bindDouble(5, entity.getCarbsPer100g());
        statement.bindDouble(6, entity.getFatPer100g());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindDouble(8, entity.getDefaultGramAmount());
      }
    };
    this.__updateAdapterOfFoodItem = new EntityDeletionOrUpdateAdapter<FoodItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `food_items` SET `id` = ?,`name` = ?,`caloriesPer100g` = ?,`proteinPer100g` = ?,`carbsPer100g` = ?,`fatPer100g` = ?,`isFavorite` = ?,`defaultGramAmount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodItem entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getCaloriesPer100g());
        statement.bindDouble(4, entity.getProteinPer100g());
        statement.bindDouble(5, entity.getCarbsPer100g());
        statement.bindDouble(6, entity.getFatPer100g());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindDouble(8, entity.getDefaultGramAmount());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfSetFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE food_items SET isFavorite = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FoodItem foodItem, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFoodItem.insertAndReturnId(foodItem);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<FoodItem> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFoodItem.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final FoodItem foodItem, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFoodItem.handle(foodItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object setFavorite(final long id, final boolean favorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetFavorite.acquire();
        int _argIndex = 1;
        final int _tmp = favorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfSetFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FoodItem>> getAllFoods() {
    final String _sql = "SELECT * FROM food_items ORDER BY isFavorite DESC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"food_items"}, new Callable<List<FoodItem>>() {
      @Override
      @NonNull
      public List<FoodItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCaloriesPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPer100g");
          final int _cursorIndexOfProteinPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinPer100g");
          final int _cursorIndexOfCarbsPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsPer100g");
          final int _cursorIndexOfFatPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "fatPer100g");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfDefaultGramAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultGramAmount");
          final List<FoodItem> _result = new ArrayList<FoodItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpCaloriesPer100g;
            _tmpCaloriesPer100g = _cursor.getInt(_cursorIndexOfCaloriesPer100g);
            final float _tmpProteinPer100g;
            _tmpProteinPer100g = _cursor.getFloat(_cursorIndexOfProteinPer100g);
            final float _tmpCarbsPer100g;
            _tmpCarbsPer100g = _cursor.getFloat(_cursorIndexOfCarbsPer100g);
            final float _tmpFatPer100g;
            _tmpFatPer100g = _cursor.getFloat(_cursorIndexOfFatPer100g);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final float _tmpDefaultGramAmount;
            _tmpDefaultGramAmount = _cursor.getFloat(_cursorIndexOfDefaultGramAmount);
            _item = new FoodItem(_tmpId,_tmpName,_tmpCaloriesPer100g,_tmpProteinPer100g,_tmpCarbsPer100g,_tmpFatPer100g,_tmpIsFavorite,_tmpDefaultGramAmount);
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
  public Object getFoodById(final long id, final Continuation<? super FoodItem> $completion) {
    final String _sql = "SELECT * FROM food_items WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FoodItem>() {
      @Override
      @Nullable
      public FoodItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCaloriesPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPer100g");
          final int _cursorIndexOfProteinPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinPer100g");
          final int _cursorIndexOfCarbsPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsPer100g");
          final int _cursorIndexOfFatPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "fatPer100g");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfDefaultGramAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultGramAmount");
          final FoodItem _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpCaloriesPer100g;
            _tmpCaloriesPer100g = _cursor.getInt(_cursorIndexOfCaloriesPer100g);
            final float _tmpProteinPer100g;
            _tmpProteinPer100g = _cursor.getFloat(_cursorIndexOfProteinPer100g);
            final float _tmpCarbsPer100g;
            _tmpCarbsPer100g = _cursor.getFloat(_cursorIndexOfCarbsPer100g);
            final float _tmpFatPer100g;
            _tmpFatPer100g = _cursor.getFloat(_cursorIndexOfFatPer100g);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final float _tmpDefaultGramAmount;
            _tmpDefaultGramAmount = _cursor.getFloat(_cursorIndexOfDefaultGramAmount);
            _result = new FoodItem(_tmpId,_tmpName,_tmpCaloriesPer100g,_tmpProteinPer100g,_tmpCarbsPer100g,_tmpFatPer100g,_tmpIsFavorite,_tmpDefaultGramAmount);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FoodItem>> searchFoods(final String query) {
    final String _sql = "SELECT * FROM food_items WHERE name LIKE '%' || ? || '%' ORDER BY isFavorite DESC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"food_items"}, new Callable<List<FoodItem>>() {
      @Override
      @NonNull
      public List<FoodItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCaloriesPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPer100g");
          final int _cursorIndexOfProteinPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinPer100g");
          final int _cursorIndexOfCarbsPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsPer100g");
          final int _cursorIndexOfFatPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "fatPer100g");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfDefaultGramAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultGramAmount");
          final List<FoodItem> _result = new ArrayList<FoodItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpCaloriesPer100g;
            _tmpCaloriesPer100g = _cursor.getInt(_cursorIndexOfCaloriesPer100g);
            final float _tmpProteinPer100g;
            _tmpProteinPer100g = _cursor.getFloat(_cursorIndexOfProteinPer100g);
            final float _tmpCarbsPer100g;
            _tmpCarbsPer100g = _cursor.getFloat(_cursorIndexOfCarbsPer100g);
            final float _tmpFatPer100g;
            _tmpFatPer100g = _cursor.getFloat(_cursorIndexOfFatPer100g);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final float _tmpDefaultGramAmount;
            _tmpDefaultGramAmount = _cursor.getFloat(_cursorIndexOfDefaultGramAmount);
            _item = new FoodItem(_tmpId,_tmpName,_tmpCaloriesPer100g,_tmpProteinPer100g,_tmpCarbsPer100g,_tmpFatPer100g,_tmpIsFavorite,_tmpDefaultGramAmount);
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
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM food_items";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
