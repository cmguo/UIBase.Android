package com.eazy.uibase.demo.core.style;

import com.eazy.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LayoutStyle extends ResourceStyle {

    public static int[] icons = {
        R.layout.tab_bar,
    };

    private static final String[] resources = new String[]{
        "layout/tab_bar",
    };

    public LayoutStyle(Field field) {
        this(field, null, null);
    }

    public LayoutStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter, resources);
    }
}
