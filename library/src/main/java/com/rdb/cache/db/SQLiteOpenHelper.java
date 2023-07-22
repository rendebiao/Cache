package com.rdb.cache.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by DB on 2016/3/20 0020.
 */
public abstract class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private SQLiteDatabase dataBase;
    private AtomicInteger atomicInteger = new AtomicInteger();

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (dataBase != null && !dataBase.isOpen()) {
            dataBase = getWritableDatabase();
        } else if (atomicInteger.incrementAndGet() == 1) {
            dataBase = getWritableDatabase();
        }
        return dataBase;
    }

    public synchronized void closeDatabase() {
        if (atomicInteger.decrementAndGet() == 0) {
            dataBase.close();
            dataBase = null;
        }
    }
}
