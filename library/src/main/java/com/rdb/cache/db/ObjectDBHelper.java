package com.rdb.cache.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rdb.cache.JsonConverter;
import com.rdb.cache.TimeProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ObjectDBHelper extends SQLiteOpenHelper {

    public static final String OBJECT_TABLE = "object";
    public static final String TAG = ObjectDBHelper.class.getSimpleName();

    private final TimeProvider timeProvider;
    private final JsonConverter jsonConverter;

    public ObjectDBHelper(Context context, String name, TimeProvider timeProvider, JsonConverter jsonConverter) {
        super(context, name, null, 1);
        this.timeProvider = timeProvider;
        this.jsonConverter = jsonConverter;
        init();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");
        initTable(db, OBJECT_TABLE);
    }

    private void initTable(SQLiteDatabase db, String table) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
        if (OBJECT_TABLE.equals(table)) {
            String sql = "CREATE TABLE IF NOT EXISTS " + OBJECT_TABLE + " (" + ObjectColumn.object_key.name() + " NVARCHAR NOT NULL, " + ObjectColumn.object_value.name() + " TEXT NOT NULL, " + ObjectColumn.object_type.name() + " NVARCHAR NOT NULL, " + ObjectColumn.object_time.name() + " LONG NOT NULL, " + ObjectColumn.object_deadline.name() + " LONG NOT NULL, PRIMARY KEY (" + ObjectColumn.object_key.name() + ", " + ObjectColumn.object_type.name() + "))";
            db.execSQL(sql);
        }
    }

    private void init() {
        String time = String.valueOf(getCurTimeMillis());
        SQLiteDatabase db = openDatabase();
        db.delete(OBJECT_TABLE, ObjectColumn.object_deadline.name() + " < ?", new String[]{time});
        closeDatabase();
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected long getCurTimeMillis() {
        return timeProvider == null ? System.currentTimeMillis() : timeProvider.getCurTimeMillis();
    }

    public final boolean putString(String key, String value, String type, long deadline) {
        if (key == null || value == null || type == null) {
            Log.e(TAG, "putString error : key == null || value == null || type == null");
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ObjectColumn.object_key.name(), key);
        contentValues.put(ObjectColumn.object_value.name(), value);
        contentValues.put(ObjectColumn.object_type.name(), type);
        contentValues.put(ObjectColumn.object_time.name(), getCurTimeMillis());
        contentValues.put(ObjectColumn.object_deadline.name(), deadline);
        Log.i(TAG, "putString key = " + key + " type = " + type);
        return replace(OBJECT_TABLE, contentValues) > 0;
    }

    public final boolean delete(String key, String type) {
        if (key == null || type == null) {
            Log.e(TAG, "delete error : key == null || type == null");
        } else {
            int delete = deleteByCondition(OBJECT_TABLE, ObjectColumn.object_key.name() + " = ? and " + ObjectColumn.object_type.name() + " = ?", new String[]{key, type});
            return delete > 0;
        }
        return false;
    }

    public final boolean deleteAll() {
        int delete = deleteByCondition(OBJECT_TABLE, "1 = 1", new String[]{});
        return delete > 0;
    }

    public final String getString(String key, String type) {
        if (key == null || type == null) {
            Log.e(TAG, "getString error : key == null || type == null");
        } else {
            return queryForObject(new RowMapper<String>() {
                @Override
                public String mapRow(Cursor cursor, int index) {
                    return cursor.getString(cursor.getColumnIndex(ObjectColumn.object_value.name()));
                }
            }, OBJECT_TABLE, ObjectColumn.object_key.name() + " = ? and " + ObjectColumn.object_type.name() + " = ?", new String[]{key, type});
        }
        return null;
    }

    public final List<String> getStrings(String type, ObjectColumn orderby, boolean asc) {
        if (type == null) {
            Log.e(TAG, "getStrings error : type == null");
        } else {
            return queryForList(new RowMapper<String>() {
                @Override
                public String mapRow(Cursor cursor, int index) {
                    return cursor.getString(cursor.getColumnIndex(ObjectColumn.object_value.name()));
                }
            }, OBJECT_TABLE, ObjectColumn.object_type.name() + " = ?", new String[]{type}, orderby == null ? null : (orderby.name() + " " + (asc ? "asc" : "desc")));
        }
        return new ArrayList<String>();
    }

    public final boolean updateDeadLine(String key, String type, long deadline) {
        if (key == null || type == null) {
            Log.e(TAG, "updateDeadLine error : key == null || type == null");
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ObjectColumn.object_deadline.name(), deadline);
        Log.i(TAG, "updateDeadLine key = " + key + " type = " + type);
        return update(OBJECT_TABLE, contentValues, ObjectColumn.object_key.name() + " = ? and " + ObjectColumn.object_type.name() + " = ?", new String[]{key, type}) > 0;
    }

    public final <T> ObjectResult<T> getObjectResult(String key, String type, final Class<T> t) {
        ObjectResult<T> objectResult = null;
        if (key == null || type == null || t == null) {
            Log.e(TAG, "getObjectResult error : key == null || type == null || t == null");
        } else {
            objectResult = queryForObject(new RowMapper<ObjectResult<T>>() {
                @Override
                public ObjectResult<T> mapRow(Cursor cursor, int index) {
                    return getObjectResult(cursor, t);
                }
            }, OBJECT_TABLE, ObjectColumn.object_key.name() + " = ? and " + ObjectColumn.object_type.name() + " = ?", new String[]{key, type});
        }
        return objectResult;
    }

    public final <T> ObjectListResult<T> getObjectListResult(String key, String type, final Class<T> t) {
        ObjectListResult<T> objectListResult = null;
        if (key == null || type == null || t == null) {
            Log.e(TAG, "getObjectListResult error : key == null || type == null || t == null");
        } else {
            objectListResult = queryForObject(new RowMapper<ObjectListResult<T>>() {
                @Override
                public ObjectListResult<T> mapRow(Cursor cursor, int index) {
                    return getObjectListResult(cursor, t);
                }
            }, OBJECT_TABLE, ObjectColumn.object_key.name() + " = ? and " + ObjectColumn.object_type.name() + " = ?", new String[]{key, type});
        }
        return objectListResult;
    }

    public final <T> List<ObjectResult<T>> getObjectResults(String type, final Class<T> t, ObjectColumn orderby, boolean asc) {
        if (type == null || t == null) {
            Log.e(TAG, "getObjectResults error : type == null || t == null");
        } else {
            return queryForList(new RowMapper<ObjectResult<T>>() {
                @Override
                public ObjectResult<T> mapRow(Cursor cursor, int index) {
                    return getObjectResult(cursor, t);
                }
            }, OBJECT_TABLE, ObjectColumn.object_key.name() + " = ?", new String[]{type}, orderby == null ? null : (orderby.name() + " " + (asc ? "asc" : "desc")));
        }
        return new ArrayList<ObjectResult<T>>();
    }

    public final <T> List<ObjectListResult<T>> getObjectListResults(String type, final Class<T> t, ObjectColumn orderby, boolean asc) {
        if (type == null || t == null) {
            Log.e(TAG, "getObjectListResults error : type == null || t == null");
        } else {
            return queryForList(new RowMapper<ObjectListResult<T>>() {
                @Override
                public ObjectListResult<T> mapRow(Cursor cursor, int index) {
                    return getObjectListResult(cursor, t);
                }
            }, OBJECT_TABLE, ObjectColumn.object_key.name() + " = ?", new String[]{type}, orderby == null ? null : (orderby.name() + " " + (asc ? "asc" : "desc")));
        }
        return new ArrayList<ObjectListResult<T>>();
    }

    private <T> ObjectResult<T> getObjectResult(Cursor cursor, Class<T> t) {
        String value = cursor.getString(cursor.getColumnIndex(ObjectColumn.object_value.name()));
        Long time = cursor.getLong(cursor.getColumnIndex(ObjectColumn.object_time.name()));
        Long deadline = cursor.getLong(cursor.getColumnIndex(ObjectColumn.object_deadline.name()));
        return new ObjectResult<T>(time, deadline, jsonConverter.toObject(value, t));
    }

    private <T> ObjectListResult<T> getObjectListResult(Cursor cursor, Class<T> t) {
        String value = cursor.getString(cursor.getColumnIndex(ObjectColumn.object_value.name()));
        Long time = cursor.getLong(cursor.getColumnIndex(ObjectColumn.object_time.name()));
        Long deadline = cursor.getLong(cursor.getColumnIndex(ObjectColumn.object_deadline.name()));
        List<T> list = jsonConverter.toArray(value, t);
        return new ObjectListResult<T>(time, deadline, list);
    }

    public final boolean putObject(String key, Object object, String type, long deadline) {
        return putString(key, jsonConverter.toString(object), type, deadline);
    }

    public final <T> T getObject(String key, String type, Class<T> t) {
        String value = getString(key, type);
        return jsonConverter.toObject(value, t);
    }

    public final <T> T getObject(String key, String type, Type typeOfT) {
        String value = getString(key, type);
        return jsonConverter.toObject(value, typeOfT);
    }

    public final <T> List<T> getObjects(String type, Class<T> t, ObjectColumn orderby, boolean asc) {
        List<T> list = new ArrayList<T>();
        List<String> values = getStrings(type, orderby, asc);
        for (String value : values) {
            T object = jsonConverter.toObject(value, t);
            if (object != null) {
                list.add(object);
            }
        }
        return list;
    }

    public final <T> List<T> getObjects(String type, Type typeOfT, ObjectColumn orderby, boolean asc) {
        List<T> list = new ArrayList<T>();
        List<String> values = getStrings(type, orderby, asc);
        for (String value : values) {
            T object = jsonConverter.toObject(value, typeOfT);
            if (object != null) {
                list.add(object);
            }
        }
        return list;
    }
}
