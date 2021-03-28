package com.eazy.uibase.demo.core.style;

import com.eazy.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ContentStyle extends ResourceStyle {

    public static int[] icons = {
        R.drawable.icon_left,
        R.drawable.icon_exit,
        R.array.button_icon_text,
        R.style.button_content_prim_style,
        R.style.button_content_text_style,
    };

    private static final String[] resources = new String[]{
        "drawable/icon_left",
        "drawable/icon_exit",
        "string/app_name",
        "icon&text:array/button_icon_text",
        "prim_style:style/button_content_prim_style",
        "text_style:style/button_content_text_style"
    };

    public ContentStyle(Field field) {
        this(field, null, null);
    }

    public ContentStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter, resources);
    }
}
