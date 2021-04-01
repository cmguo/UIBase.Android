package com.xhb.uibase.demo.core.style;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ButtonStyle extends ResourceStyle {

    private static final String[] resources = new String[]{
        "style/XHBButton_Bottom",
        "style/XHBButton_Activity",
    };

    public ButtonStyle(Field field) {
        this(field, null, null);
    }

    public ButtonStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter, resources);
    }
}
