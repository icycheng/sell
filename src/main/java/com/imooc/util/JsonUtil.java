package com.imooc.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author: icych
 * @description: JSON工具
 * @date: Created on 21:57 2018/7/17
 */
public class JsonUtil {

    private JsonUtil() {
    }

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

}
