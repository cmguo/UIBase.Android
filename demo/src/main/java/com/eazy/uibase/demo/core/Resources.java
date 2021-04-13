package com.eazy.uibase.demo.core;

import android.content.Context;
import android.util.Log;

import com.eazy.uibase.R;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Resources {

    private static final String TAG = "Resources";

    public static Map<String, Integer> getResources(Class<?> clazz, Pattern pattern) {
        Map<String, Integer> ress = new TreeMap<>();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                if (pattern == null || pattern.matcher(f.getName()).find()) {
                    Log.d(TAG, f.getName());
                    int id = (Integer) f.get(clazz);
                    ress.put(f.getName(), id);
                }
            }
        } catch (IllegalAccessException e) {
            Log.w(TAG, "", e);
        }
        return ress;
    }

    public static int getResource(Class<?> clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            return (Integer) f.get(clazz);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.w(TAG, "", e);
            return  0;
        }
    }
}