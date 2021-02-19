package com.xhb.uibase.demo.core;

import android.content.Context;
import android.util.Log;

import com.xhb.uibase.demo.R;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Layouts {

    private static final String TAG = "Layouts";

    public static Map<String, Integer> dialogLayouts(Context context) {
        return getLayouts(context, R.layout.class, Pattern.compile("[Dd]ialog"));
        // return getLayouts(context, R.layout.class, Pattern.compile("YellowLargeButtonStyle"));
    }

    public static Map<String, Integer> getLayouts(Context context, Class<?> clazz, Pattern pattern) {
        Map<String, Integer> layouts = new TreeMap<>();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                if (pattern == null || pattern.matcher(f.getName()).find()) {
                    Log.d(TAG, f.getName());
                    int id = (Integer) f.get(clazz);
                    layouts.put(f.getName(), id);
                }
            }
        } catch (IllegalAccessException e) {
            Log.w(TAG, "", e);
        }
        return layouts;
    }

}
