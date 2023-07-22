package com.rdb.cache;

import android.content.Context;

import com.rdb.cache.db.ObjectColumn;
import com.rdb.cache.db.ObjectDBHelper;
import com.rdb.cache.db.ObjectListResult;
import com.rdb.cache.db.ObjectResult;

import java.lang.reflect.Type;
import java.util.List;

public class DBCache {

    private static ObjectDBHelper objectDBHelper;

    public static void init(Context context, String name, TimeProvider timeProvider, JsonConverter jsonConverter) {
        objectDBHelper = new ObjectDBHelper(context, name, timeProvider, jsonConverter);
    }

    public static boolean putString(String key, String value, String type, long deadline) {
        return objectDBHelper.putString(key, value, type, deadline);
    }

    public static boolean putObject(String key, Object object, String type, long deadline) {
        return objectDBHelper.putObject(key, object, type, deadline);
    }

    public static boolean updateDeadLine(String key, String type, long deadline) {
        return objectDBHelper.updateDeadLine(key, type, deadline);
    }

    public static String getString(String key, String type) {
        return objectDBHelper.getString(key, type);
    }

    public static List<String> getStrings(String type, ObjectColumn orderby, boolean asc) {
        return objectDBHelper.getStrings(type, orderby, asc);
    }

    public static <T> T getObject(String key, String type, Class<T> t) {
        return objectDBHelper.getObject(key, type, t);
    }

    public static <T> T getObject(String key, String type, Type typeOfT) {
        return objectDBHelper.getObject(key, type, typeOfT);
    }

    public static <T> List<T> getObjects(String type, Class<T> t, ObjectColumn orderby, boolean asc) {
        return objectDBHelper.getObjects(type, t, orderby, asc);
    }

    public static <T> List<T> getObjects(String type, Type typeOfT, ObjectColumn orderby, boolean asc) {
        return objectDBHelper.getObjects(type, typeOfT, orderby, asc);
    }

    public static <T> ObjectResult<T> getObjectResult(String key, String type, Class<T> t) {
        return objectDBHelper.getObjectResult(key, type, t);
    }

    public static <T> ObjectListResult<T> getObjectListResult(String key, String type, Class<T> t) {
        return objectDBHelper.getObjectListResult(key, type, t);
    }

    public static <T> List<ObjectResult<T>> getObjectResults(String type, Class<T> t, ObjectColumn orderby, boolean asc) {
        return objectDBHelper.getObjectResults(type, t, orderby, asc);
    }

    public static <T> List<ObjectListResult<T>> getObjectListResults(String type, Class<T> t, ObjectColumn orderby, boolean asc) {
        return objectDBHelper.getObjectListResults(type, t, orderby, asc);
    }

    public static boolean delete(String key, String type) {
        return objectDBHelper.delete(key, type);
    }

    public static boolean deleteAll() {
        return objectDBHelper.deleteAll();
    }
}
