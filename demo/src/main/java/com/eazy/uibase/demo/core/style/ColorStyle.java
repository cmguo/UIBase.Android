package com.eazy.uibase.demo.core.style;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.eazy.uibase.demo.core.Colors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColorStyle extends ComponentStyle {

    private static List<String> values;
    private static List<String> valueIds;
    private static List<String> valueTitles;

    public static void init(Context context) {
        final Map<String, Integer> colors = Colors.stdDynamicColorIds();
        values = new ArrayList<>();
        valueIds = new ArrayList<>();
        for (int c : colors.values()) {
            int cl = ContextCompat.getColor(context, c);
            values.add(String.valueOf(cl));
            valueIds.add(String.valueOf(c));
        }
        valueTitles = new ArrayList<>(colors.keySet());
    }

    public ColorStyle(Field field) {
        this(field, null, null);
    }

    public ColorStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
//        String[] values = {
//                String.valueOf(Color.BLACK),
//                String.valueOf(Color.WHITE),
//                String.valueOf(Color.RED),
//                String.valueOf(Color.GREEN),
//                String.valueOf(Color.BLUE) };
//        String[] valueTitles = {"black", "white", "red", "green", "blue"};
        if (styleParams().length == 0)
            setValues(values, valueTitles);
        else
            setValues(valueIds, valueTitles);
    }
}
