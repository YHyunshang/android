package com.yh.base.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {
    public static Gson gson;

    public static Gson Gson() {
        return gson;
    }

    static {
        gson = new Gson();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJsonType(String json, TypeToken<T> typeOfT) {
        return gson.fromJson(json, typeOfT.getType());
    }

//    public static String fromField(String json,String field){
//       JsonParser.parseString(json).getAsString().concat()
//    }
}
