package com.eazy.uibase.demo.core;

import android.content.Context;
import android.util.Log;

import com.eazy.uibase.R;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Colors extends Resources {

    private static final String TAG = "Colors";

    public static Map<String, Integer> stdColors(Context context) {
        return getColors(context, R.color.class, Pattern.compile("^([a-z]{2,}_)+\\d+"));
    }

    public static Map<String, Integer> nonStdColors(Context context) {
        Map<String, Integer> colors = getColors(context, R.color.class, null);
        Map<String, Integer> stdColors = Colors.stdColors(context);
        for (String k : stdColors.keySet()) {
            colors.remove(k);
        }
        return colors;
    }

    public static Map<String, Integer> getColors(Context context, Class<?> clazz, Pattern pattern) {
        Map<String, Integer> colors = getResources(context, clazz, pattern);
        for (Map.Entry<String, Integer> entry : colors.entrySet()) {
            entry.setValue(SkinManager.getColor(context, entry.getValue()));
        }
        return colors;
    }

    public static int invert(int color) {
        return ((color) & 0xff000000) | (~(color & 0x00ffffff));
    }

    public static String text(int color) {
        return "#" + Integer.toHexString(color);
    }

    public static void update(Context context, Map<String, Integer> colors) {
        Class clazz = R.color.class;
        try {
            for (Map.Entry<String, Integer> e : colors.entrySet()) {
                int id = (Integer) clazz.getDeclaredField(e.getKey()).get(clazz);
                e.setValue(SkinManager.getColor(context, id));
            }
        } catch (Throwable e) {
        }
    }
}
