package com.example.caloriesapp.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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
public final class CustomFoodDao_Impl implements CustomFoodDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CustomFood> __insertionAdapterOfCustomFood;

  private final EntityDeletionOrUpdateAdapter<CustomFood> __updateAdapterOfCustomFood;

  public CustomFoodDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCustomFood = new EntityInsertionAdapter<CustomFood>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `custom_foods` (`id`,`name`,`caloriesPer100g`,`proteinPer100g`,`carbsPer100g`,`fatPer100g`,`defaultGramAmount`,`isFavorite`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomFood entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getCaloriesPer100g());
        statement.bindDouble(4, entity.getProteinPer100g());
        statement.bindDouble(5, entity.getCarbsPer100g());
        statement.bindDouble(6, entity.getFatPer100g());
        statement.bindDouble(7, entity.getDefaultGramAmount());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__updateAdapterOfCustomFood = new EntityDeletionOrUpdateAdapter<CustomFood>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `custom_foods` SET `id` = ?,`name` = ?,`caloriesPer100g` = ?,`proteinPer100g` = ?,`carbsPer100g` = ?,`fatPer100g` = ?,`defaultGramAmount` = ?,`isFavorite` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomFood entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getCaloriesPer100g());
        statement.bindDouble(4, entity.getProteinPer100g());
        statement.bindDouble(5, entity.getCarbsPer100g());
        statement.bindDouble(6, entity.getFatPer100g());
        statement.bindDouble(7, entity.getDefaultGramAmount());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final CustomFood food, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCustomFood.insertAndReturnId(food);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CustomFood food, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCustomFood.handle(food);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CustomFood>> observeFoods() {
    final String _sql = "SELECT * FROM custom_foods ORDER BY isFavorite DESC, name COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"custom_foods"}, new Callable<List<CustomFood>>() {
      @Override
      @NonNull
      public List<CustomFood> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCaloriesPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesPer100g");
          final int _cursorIndexOfProteinPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "proteinPer100g");
          final int _cursorIndexOfCarbsPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "carbsPer100g");
          final int _cursorIndexOfFatPer100g = CursorUtil.getColumnIndexOrThrow(_cursor, "fatPer100g");
          final int _cursorIndexOfDefaultGramAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultGramAmount");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<CustomFood> _result = new ArrayList<CustomFood>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CustomFood _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpCaloriesPer100g;
            _tmpCaloriesPer100g = _cursor.getDouble(_cursorIndexOfCaloriesPer100g);
            final double _tmpProteinPer100g;
            _tmpProteinPer100g = _cursor.getDouble(_cursorIndexOfProteinPer100g);
            final double _tmpCarbsPer100g;
            _tmpCarbsPer100g = _cursor.getDouble(_cursorIndexOfCarbsPer100g);
            final double _tmpFatPer100g;
            _tmpFatPer100g = _cursor.getDouble(_cursorIndexOfFatPer100g);
            final double _tmpDefaultGramAmount;
            _tmpDefaultGramAmount = _cursor.getDouble(_cursorIndexOfDefaultGramAmount);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item = new CustomFood(_tmpId,_tmpName,_tmpCaloriesPer100g,_tmpProteinPer100g,_tmpCarbsPer100g,_tmpFatPer100g,_tmpDefaultGramAmount,_tmpIsFavorite);
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
