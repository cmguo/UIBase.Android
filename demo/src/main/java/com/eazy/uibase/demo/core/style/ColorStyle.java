package com.eazy.uibase.demo.core.style;

import android.graphics.Color;

import com.eazy.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ColorStyle extends ComponentStyle {

    public ColorStyle(Field field) {
        this(field, null, null);
    }

    public ColorStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
        String[] values = {
                String.valueOf(Color.BLACK),
                String.valueOf(Color.WHITE),
                String.valueOf(Color.RED),
                String.valueOf(Color.GREEN),
                String.valueOf(Color.BLUE) };
        String[] valueTitles = {"black", "white", "red", "green", "blue"};
        setValues(Arrays.asList(values), Arrays.asList(valueTitles));
    }
}
