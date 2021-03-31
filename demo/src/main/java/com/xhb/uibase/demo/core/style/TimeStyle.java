package com.xhb.uibase.demo.core.style;

import android.graphics.Color;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class TimeStyle extends ComponentStyle {

    public TimeStyle(Field field) {
        this(field, null, null);
    }

    public TimeStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
    }

    private static final SimpleDateFormat formmat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault());

    @Override
    protected Object valueFromString(String value) {
        try {
            return formmat.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    protected String valueToString(Object value) {
        return formmat.format((Date) value);
    }
}
