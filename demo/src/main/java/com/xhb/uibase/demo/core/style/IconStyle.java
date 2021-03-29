package com.xhb.uibase.demo.core.style;

import com.xhb.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class IconStyle extends ResourceStyle {

    public static int[] icons = {
        R.drawable.ic_erase,
        R.drawable.icon_minus,
        R.drawable.icon_plus,
        R.drawable.icon_more,
        R.drawable.icon_left,
        R.drawable.icon_exit,
    };

    private static String[] resources = new String[] {
        "drawable/ic_erase", "icon_plus", "icon_minus", "icon_more", "icon_left", "icon_exit"};

    public IconStyle(Field field) {
        this(field, null, null);
    }

    public IconStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter, resources);
    }
}
