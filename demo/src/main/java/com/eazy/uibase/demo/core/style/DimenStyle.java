package com.eazy.uibase.demo.core.style;

import android.content.Context;
import android.util.SizeF;

import com.eazy.uibase.demo.core.ViewStyles;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DimenStyle extends ComponentStyle {

    public static Converter dpconv;
    public static Converter spconv;

    public static void init(Context context) {
        dpconv = new DpConverter(context);
        spconv = new SpConverter(context);
    }

    private Converter conv = dpconv;

    public DimenStyle(Field field) {
        this(field, null, null);
    }

    public DimenStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
        String[] params = styleParams();
        for (String p : params) {
            if ("dp".equals(p))
                conv = dpconv;
            else if ("sp".equals(p))
                conv = spconv;
        }
    }

    @Override
    public void init(ViewStyles styles) {
        Object value = getRaw(styles);
        if (value instanceof Integer)
            value = conv.toPx((Integer) value);
        else if (value instanceof Float)
            value = conv.toPx((Float) value);
        else if (value instanceof SizeF)
            value = new SizeF(conv.toPx(((SizeF) value).getWidth()), conv.toPx(((SizeF) value).getHeight()));
        setRaw(styles, value);
    }

    @Override
    protected String valueToString(Object value) {
        if (value instanceof Integer)
            value = conv.fromPx((Integer) value);
        else if (value instanceof Float)
            value = conv.fromPx((Float) value);
        else if (value instanceof SizeF)
            value = new SizeF(conv.fromPx(((SizeF) value).getWidth()), conv.fromPx(((SizeF) value).getHeight()));
        return super.valueToString(value) + "dp";
    }

    @Override
    protected Object valueFromString(String string) {
        Object value = super.valueFromString(string.replace("dp", ""));
        if (value instanceof Integer)
            value = conv.toPx((Integer) value);
        else if (value instanceof Float)
            value = conv.toPx((Float) value);
        else if (value instanceof SizeF)
            value = new SizeF(conv.toPx(((SizeF) value).getWidth()), conv.toPx(((SizeF) value).getHeight()));
        return value;
    }

    public static class Converter {
        protected float density = 1f;
        public float toPx(float v) {
            return v * density;
        }
        public float fromPx(float v)  {
            return v * density;
        }
        public int toPx(int v) {
            return ceil(v * density);
        }
        public int fromPx(int v) {
            return ceil(v / density);
        }
        private static int ceil(float f) {
            return (int)(f > 0 ? Math.ceil(f) : Math.floor(f));
        }
    }

    static class DpConverter extends Converter {
        DpConverter(Context context) {
            density = context.getResources().getDisplayMetrics().density;
        }
    }

    static class SpConverter extends Converter {
        SpConverter(Context context) {
            density = context.getResources().getDisplayMetrics().density;
        }
    }

}
