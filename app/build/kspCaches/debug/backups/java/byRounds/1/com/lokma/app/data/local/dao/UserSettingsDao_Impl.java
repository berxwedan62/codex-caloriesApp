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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lokma.app.data.local.entity.UserSettings;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserSettingsDao_Impl implements UserSettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserSettings> __insertionAdapterOfUserSettings;

  private final EntityDeletionOrUpdateAdapter<UserSettings> __updateAdapterOfUserSettings;

  public UserSettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserSettings = new EntityInsertionAdapter<UserSettings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_settings` (`id`,`dailyCalorieTarget`,`premiumUnlocked`,`useMetricUnits`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserSettings entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDailyCalorieTarget());
        final int _tmp = entity.getPremiumUnlocked() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final int _tmp_1 = entity.getUseMetricUnits() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
      }
    };
    this.__updateAdapterOfUserSettings = new EntityDeletionOrUpdateAdapter<UserSettings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `user_settings` SET `id` = ?,`dailyCalorieTarget` = ?,`premiumUnlocked` = ?,`useMetricUnits` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserSettings entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDailyCalorieTarget());
        final int _tmp = entity.getPremiumUnlocked() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final int _tmp_1 = entity.getUseMetricUnits() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final UserSettings settings, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserSettings.insert(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final UserSettings settings, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserSettings.handle(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserSettings> observeSettings() {
    final String _sql = "SELECT * FROM user_settings WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_settings"}, new Callable<UserSettings>() {
      @Override
      @Nullable
      public UserSettings call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDailyCalorieTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyCalorieTarget");
          final int _cursorIndexOfPremiumUnlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "premiumUnlocked");
          final int _cursorIndexOfUseMetricUnits = CursorUtil.getColumnIndexOrThrow(_cursor, "useMetricUnits");
          final UserSettings _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDailyCalorieTarget;
            _tmpDailyCalorieTarget = _cursor.getInt(_cursorIndexOfDailyCalorieTarget);
            final boolean _tmpPremiumUnlocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfPremiumUnlocked);
            _tmpPremiumUnlocked = _tmp != 0;
            final boolean _tmpUseMetricUnits;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfUseMetricUnits);
            _tmpUseMetricUnits = _tmp_1 != 0;
            _result = new UserSettings(_tmpId,_tmpDailyCalorieTarget,_tmpPremiumUnlocked,_tmpUseMetricUnits);
          } else {
            _result = null;
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
  public Object getSettings(final Continuation<? super UserSettings> $completion) {
    final String _sql = "SELECT * FROM user_settings WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserSettings>() {
      @Override
      @Nullable
      public UserSettings call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDailyCalorieTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyCalorieTarget");
          final int _cursorIndexOfPremiumUnlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "premiumUnlocked");
          final int _cursorIndexOfUseMetricUnits = CursorUtil.getColumnIndexOrThrow(_cursor, "useMetricUnits");
          final UserSettings _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDailyCalorieTarget;
            _tmpDailyCalorieTarget = _cursor.getInt(_cursorIndexOfDailyCalorieTarget);
            final boolean _tmpPremiumUnlocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfPremiumUnlocked);
            _tmpPremiumUnlocked = _tmp != 0;
            final boolean _tmpUseMetricUnits;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfUseMetricUnits);
            _tmpUseMetricUnits = _tmp_1 != 0;
            _result = new UserSettings(_tmpId,_tmpDailyCalorieTarget,_tmpPremiumUnlocked,_tmpUseMetricUnits);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
