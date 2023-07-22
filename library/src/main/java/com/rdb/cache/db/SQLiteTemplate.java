package com.rdb.cache.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLiteTemplate {

    private static final String TAG = "SQLiteTemplate";
    private SQLiteOpenHelper openHelper;
    private boolean isTransaction = false;

    private SQLiteTemplate(SQLiteOpenHelper openHelper, boolean isTransaction) {
        this.openHelper = openHelper;
        this.isTransaction = isTransaction;
    }

    public static SQLiteTemplate getInstance(SQLiteOpenHelper openHelper, boolean isTransaction) {
        return new SQLiteTemplate(openHelper, isTransaction);
    }

    public void execSQL(String sql) {
        try {
            request().execSQL(sql);
        } catch (Exception e) {
            Log.e(TAG, "execSQL : " + e.getMessage());
        } finally {
            if (!isTransaction) {
                release();
            }
        }
    }

    public void execSQL(String sql, Object[] bindArgs) {
        try {
            request().execSQL(sql, bindArgs);
        } catch (Exception e) {
            Log.e(TAG, "execSQL : " + e.getMessage());
        } finally {
            if (!isTransaction) {
                release();
            }
        }
    }

    public long insert(String table, ContentValues content) {
        try {
            return request().insert(table, null, content);
        } catch (Exception e) {
            Log.e(TAG, "insert : " + e.getMessage());
        } finally {
            if (!isTransaction) {
                release();
            }
        }
        return -1;
    }

    public long replace(String table, ContentValues content) {
        try {
            return request().replace(table, null, content);
        } catch (Exception e) {
            Log.e(TAG, "replace : " + e.getMessage());
        } finally {
            if (!isTransaction) {
                release();
            }
        }
        return -1;
    }

    public int deleteByField(String table, String filed, List<String> values) {
        if (values.size() > 0) {
            StringBuilder sb = new StringBuilder();
            if (values.size() > 0) {
                for (int i = 0; i < values.size(); i++) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(values.get(i));
                }
                return deleteByCondition(table, filed + " in (?)",
                        new String[]{
                                sb.toString()
                        });
            }
        }
        return 0;
    }

    public int deleteByField(String table, String field, String value) {

        return deleteByCondition(table, field + "=?", new String[]{
                value
        });
    }

    public int deleteByCondition(String table, String whereClause,
                                 String[] whereArgs) {
        try {
            return request().delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            Log.e(TAG, "deleteByCondition : " + e.getMessage());
        } finally {
            if (!isTransaction) {
                release();
            }
        }
        return 0;
    }

    public int update(String table, ContentValues values, String whereClause,
                      String[] whereArgs) {
        try {
            return request().update(table, values, whereClause, whereArgs);
        } catch (Exception e) {
            Log.e(TAG, "updateIndeterminate : " + e.getMessage());
        } finally {
            if (!isTransaction) {
                release();
            }
        }
        return 0;
    }

    public Boolean isExistsByField(String table, String field, String value) {
        return isExistsByCondition(table, field + " = " + value);
    }

    public boolean isExistsByCondition(String table, String where) {
        Cursor cursor = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(*) FROM ").append(table).append(" WHERE ")
                    .append(where);
            cursor = request().rawQuery(sql.toString(), new String[]{});
            if (cursor.moveToFirst()) {
                return (cursor.getInt(0) > 0);
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "isExistsByCondition : " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (!isTransaction) {
                release();
            }
        }
        return false;
    }

    public <T> T queryForObject(RowMapper<T> rowMapper, String sql,
                                String[] args) {
        Cursor cursor = null;
        T object = null;
        try {
            cursor = request().rawQuery(sql, args);
            if (cursor.moveToFirst()) {
                object = rowMapper.mapRow(cursor, cursor.getCount());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (!isTransaction) {
                release();
            }
        }
        return object;
    }


    public <T> T queryForObject(RowMapper<T> rowMapper, String table, String whereClause, String[] args) {
        Cursor cursor = null;
        T object = null;
        try {
            cursor = request().query(table, null, whereClause, args, null, null, null);
            if (cursor.moveToFirst()) {
                object = rowMapper.mapRow(cursor, cursor.getCount());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (!isTransaction) {
                release();
            }
        }
        return object;
    }

    public <T> ArrayList<T> queryForList(RowMapper<T> rowMapper, String table, String whereClause, String[] args, String orderBy) {
        Cursor cursor = null;
        ArrayList<T> list = new ArrayList<T>();
        try {
            cursor = request().query(table, null, whereClause, args, null, null, orderBy);
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
            if (!isTransaction) {
                release();
            }
        }
        return list;
    }

    public <T> ArrayList<T> queryForList(RowMapper<T> rowMapper, String sql,
                                         String[] selectionArgs) {
        Cursor cursor = null;
        ArrayList<T> list = new ArrayList<T>();
        try {
            cursor = request().rawQuery(sql, selectionArgs);
            if (cursor == null) {
                throw new RuntimeException("cursor == null sql = " + sql);
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
            if (!isTransaction) {
                release();
            }
        }
        return list;
    }

    public <T> List<T> queryForList(RowMapper<T> rowMapper, String sql,
                                    int startResult, int maxResult) {
        Cursor cursor = null;
        List<T> list = null;
        try {
            cursor = request().rawQuery(sql + " limit ?,?", new String[]{
                    String.valueOf(startResult), String.valueOf(maxResult)
            });
            list = new ArrayList<T>();
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
            if (!isTransaction) {
                release();
            }
        }
        return list;
    }

    public Integer getCount(String sql, String[] args) {
        Cursor cursor = null;
        try {
            cursor = request().rawQuery("select count(*) from (" + sql + ")",
                    args);
            if (cursor.moveToNext()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getCount : " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (!isTransaction) {
                release();
            }
        }
        return 0;
    }

    public <T> List<T> queryForList(RowMapper<T> rowMapper, String table,
                                    String[] columns, String selection, String[] selectionArgs,
                                    String groupBy, String having, String orderBy, String limit) {
        List<T> list = null;
        Cursor cursor = null;
        try {
            cursor = request().query(table, columns, selection, selectionArgs,
                    groupBy, having, orderBy, limit);
            list = new ArrayList<T>();
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
            if (!isTransaction) {
                release();
            }
        }
        return list;
    }

    private SQLiteDatabase request() {
        return openHelper.openDatabase();
    }

    public void release() {
        openHelper.closeDatabase();
        openHelper = null;
    }

    public interface RowMapper<T> {

        public T mapRow(Cursor cursor, int index);
    }
}
