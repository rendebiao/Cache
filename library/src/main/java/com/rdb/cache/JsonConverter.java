package com.rdb.cache;

import java.lang.reflect.Type;
import java.util.ArrayList;

public interface JsonConverter {


    String toString(Object obj);

    <T> T toObject(String json, Class<T> t);

    <T> T toObject(String json, Type typeOfT);

    <T> ArrayList<T> toArray(String json, Class<T> t);
}
