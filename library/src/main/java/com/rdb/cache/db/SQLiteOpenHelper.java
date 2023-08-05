package com.rdb.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by DB on 2016/3/20 0020.
 */
public abstract class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String TAG = "SQLiteOpenHelper";
    private final AtomicInteger atomicInteger = new AtomicInteger();
    private SQLiteDatabase dataBase;

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (atomicInteger.incrementAndGet() == 1 || dataBase == null || !dataBase.isOpen()) {
            dataBase = getWritableDatabase();
        }
        return dataBase;
    }

    public synchronized void closeDatabase() {
        if (atomicInteger.decrementAndGet() == 0) {
            if (dataBase != null) {
                dataBase.close();
                dataBase = null;
            }
        }
    }

    public long replace(String table, ContentValues content) {
        try {
            return openDatabase().replace(table, null, content);
        } catch (Exception e) {
            Log.e(TAG, "replace : " + e.getMessage());
        } finally {
            closeDatabase();
        }
        return -1;
    }

    public int deleteByCondition(String table, String whereClause, String[] whereArgs) {
        try {
            return openDatabase().delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            Log.e(TAG, "deleteByCondition : " + e.getMessage());
        } finally {
            closeDatabase();
        }
        return 0;
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        try {
            return openDatabase().update(table, values, whereClause, whereArgs);
        } catch (Exception e) {
            Log.e(TAG, "updateIndeterminate : " + e.getMessage());
        } finally {
            closeDatabase();
        }
        return 0;
    }

    public <T> T queryForObject(RowMapper<T> rowMapper, String table, String whereClause, String[] args) {
        Cursor cursor = null;
        T object = null;
        try {
            cursor = openDatabase().query(table, null, whereClause, args, null, null, null);
            if (cursor.moveToFirst()) {
                object = rowMapper.mapRow(cursor, cursor.getCount());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase();
        }
        return object;
    }

    public <T> ArrayList<T> queryForList(RowMapper<T> rowMapper, String table, String whereClause, String[] args, String orderBy) {
        Cursor cursor = null;
        ArrayList<T> list = new ArrayList<T>();
        try {
            cursor = openDatabase().query(table, null, whereClause, args, null, null, orderBy);
            if (cursor == null) {
                throw new RuntimeException("cursor == null sql = " + whereClause);
            }
            while (cursor.moveToNext()) {
                T result = rowMapper.mapRow(cursor, cursor.getPosition());
                if (result != null) {
                    list.add(result);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase();
        }
        return list;
    }

    public interface RowMapper<T> {

        T mapRow(Cursor cursor, int index);
    }
}
