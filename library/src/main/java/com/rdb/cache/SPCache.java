package com.rdb.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPCache {

    private static Context context;
    private static SharedPreferences appSP;
    private static JsonConverter converter;

    public static void init(Context context, String name, JsonConverter converter) {
        SPCache.context = context;
        SPCache.converter = converter;
        appSP = SPCache.context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    protected static SharedPreferences getSharedPreferencesByType() {
        return appSP;
    }

    public static void clearSharedPreferences() {
        getSharedPreferencesByType().edit().clear().commit();
    }

    public static void putObject(String key, Object obj) {
        Editor editor = getSharedPreferencesByType().edit();
        if (obj == null) {
            editor.remove(key);
        } else {
            editor.putString(key, converter.toString(obj));
        }
        editor.commit();
    }

    public static void remove(String key) {
        Editor editor = getSharedPreferencesByType().edit();
        editor.remove(key);
        editor.commit();
    }

    public static void putString(String key, String value) {
        Editor editor = getSharedPreferencesByType().edit();
        if (value == null) {
            editor.remove(key);
        } else {
            editor.putString(key, value);
        }
        editor.commit();
    }

    public static void putInt(String key, int value) {
        Editor editor = getSharedPreferencesByType().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putLong(String key, long value) {
        Editor editor = getSharedPreferencesByType().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void putFloat(String key, float value) {
        Editor editor = getSharedPreferencesByType().edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void putBoolean(String key, boolean value) {
        Editor editor = getSharedPreferencesByType().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static <T> T getObject(String key, Class<T> tClass) {
        try {
            String value = getSharedPreferencesByType().getString(key, null);
            return converter.toObject(value, tClass);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(String key, String defValue) {
        try {
            return getSharedPreferencesByType().getString(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static boolean getBoolean(String key, boolean defValue) {
        try {
            return getSharedPreferencesByType().getBoolean(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static int getInt(String key, int defValue) {
        try {
            return getSharedPreferencesByType().getInt(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static long getLong(String key, long defValue) {
        try {
            return getSharedPreferencesByType().getLong(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static float getFloat(String key, float defValue) {
        try {
            return getSharedPreferencesByType().getFloat(key, defValue);
        } catch (Exception e) {
            return defValue;
        }
    }
}
