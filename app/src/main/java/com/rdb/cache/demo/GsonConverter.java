package com.rdb.cache.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rdb.cache.JsonConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GsonConverter implements JsonConverter {

    private static Gson mGson;

    static {
        mGson = new GsonBuilder().create();
    }

    public static String json(Object obj) {
        return mGson.toJson(obj);
    }

    public static <T> T parse(String json, Class<T> t) {
        T obj = null;
        try {
            obj = mGson.fromJson(json, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> T parse(String json, Type typeOfT) {
        T obj = null;
        try {
            obj = mGson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> ArrayList<T> parseArray(String json, Class<T> t) {
        ArrayList<T> objs = null;
        try {
            Type type = TypeToken.getParameterized(ArrayList.class, t).getType();
            objs = mGson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs;
    }

    @Override
    public String toString(Object obj) {
        return json(obj);
    }

    @Override
    public <T> T toObject(String json, Class<T> t) {
        return parse(json, t);
    }

    @Override
    public <T> T toObject(String json, Type typeOfT) {
        return parse(json, typeOfT);
    }

    @Override
    public <T> ArrayList<T> toArray(String json, Class<T> t) {
        return parseArray(json, t);
    }
}
