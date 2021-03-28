package com.xhb.uibase.demo.core.style;

import com.xhb.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LayoutStyle extends ResourceStyle {

    public static int[] icons = {
        R.layout.xhb_tab_bar,
    };

    private static final String[] resources = new String[]{
        "layout/xhb_tab_bar",
    };

    public LayoutStyle(Field field) {
        this(field, null, null);
    }

    public LayoutStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter, resources);
    }
}
