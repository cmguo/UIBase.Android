package com.eazy.uibase.demo.colors;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.eazy.uibase.demo.R;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Colors {

    private static final String TAG = "Colors";

    public static Map<String, Integer> stdColors(Context context) {
        return getColors(context, R.color.class, Pattern.compile("^[a-z]+_\\d+"));
    }

    public static Map<String, Integer> getColors(Context context, Class<?> clazz, Pattern pattern) {
        Map<String, Integer> colors = new TreeMap<>();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                if (pattern == null || pattern.matcher(f.getName()).find()) {
                    Log.d(TAG, f.getName());
                    colors.put(f.getName(), context.getColor((Integer) f.get(clazz)));
                }
            }
        } catch (IllegalAccessException e) {
            Log.w(TAG, "", e);
        }
        return colors;
    }

    public static int invert(int color) {
        return ((color) & 0xff000000) | (~(color & 0x00ffffff));
    }

    public static String text(int color) {
        return "#" + Integer.toHexString(color);
    }
}
