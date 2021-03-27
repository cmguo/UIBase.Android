package com.xhb.uibase.demo.core.style;

import android.content.Context;
import android.util.SizeF;

import com.xhb.uibase.demo.core.ViewStyles;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DimemDpStyle extends ComponentStyle {

    private static Float density = 1f;

    public static void init(Context context) {
        density = context.getResources().getDisplayMetrics().density;
    }

    public DimemDpStyle(Field field) {
        super(field);
    }

    public DimemDpStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
    }

    @Override
    public void init(ViewStyles styles) {
        Object value = getRaw(styles);
        if (value instanceof Integer)
            value = dp2px((Integer) value);
        else if (value instanceof Float)
            value = dp2px((Float) value);
        else if (value instanceof SizeF)
            value = new SizeF(dp2px(((SizeF) value).getWidth()), dp2px(((SizeF) value).getHeight()));
        setRaw(styles, value);
    }

    @Override
    protected String valueToString(Object value) {
        if (value instanceof Integer)
            value = px2dp((Integer) value);
        else if (value instanceof Float)
            value = px2dp((Float) value);
        else if (value instanceof SizeF)
            value = new SizeF(px2dp(((SizeF) value).getWidth()), px2dp(((SizeF) value).getHeight()));
        return super.valueToString(value) + "dp";
    }

    @Override
    protected Object valueFromString(String string) {
        Object value = super.valueFromString(string.replace("dp", ""));
        if (value instanceof Integer)
            value = dp2px((Integer) value);
        else if (value instanceof Float)
            value = dp2px((Float) value);
        else if (value instanceof SizeF)
            value = new SizeF(dp2px(((SizeF) value).getWidth()), dp2px(((SizeF) value).getHeight()));
        return value;
    }

    public static float dp2px(float dp) {
        return dp * density;
    }

    public static float px2dp(float px) {
        return px / density;
    }

    public static int dp2px(int dp) {
        return ceil(dp * density);
    }

    public static int px2dp(int px) {
        return ceil(px / density);
    }

    private static int ceil(float f) {
        return (int)(f > 0 ? Math.ceil(f) : Math.floor(f));
    }

}
