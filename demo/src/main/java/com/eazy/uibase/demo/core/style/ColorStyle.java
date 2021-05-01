package com.eazy.uibase.demo.core.style;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.eazy.uibase.demo.resources.Colors;
import com.eazy.uibase.demo.resources.Resources;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColorStyle extends ComponentStyle {

    private static List<String> values;
    private static List<String> valueIds;
    private static List<String> valueTitles;

    public static void init(Context context) {
        final Map<String, Resources.ResourceValue> colors = Colors.stdDynamicColors(context);
        values = new ArrayList<>();
        valueIds = new ArrayList<>();
        valueTitles = new ArrayList<>();
        for (Map.Entry<String, Resources.ResourceValue> e : colors.entrySet()) {
            values.add(String.valueOf(e.getValue().getValue()));
            valueIds.add(String.valueOf(e.getValue().getResId()));
            valueTitles.add(Resources.simpleName(e.getKey()));
        }
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
