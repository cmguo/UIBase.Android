package com.eazy.uibase.daynight.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.AttrRes;

import com.eazy.uibase.R;
import com.eazy.uibase.utils.Generic;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public class StyleableSet<E extends View> implements IStyleableSet<E> {

    private static final String TAG = "StyleableSet";

    private static final Map<Class<? extends View>, IStyleableSet<? extends View>> sStyleables = new HashMap<>();

    static {
        for (IStyleableSet<?> styleableSet : ServiceLoader.load(IStyleableSet.class)) {
            Class<? extends View> cls = Generic.getParamType(styleableSet.getClass(), StyleableSet.class, 0);
            Log.d(TAG, "collect styleableSet " + cls.getSimpleName());
            sStyleables.put(cls, styleableSet);
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends View> IStyleableSet<E> get(Class<E> cls) {
        return (IStyleableSet<E>) sStyleables.get(cls);
    }

    public static <E extends View> void add(Class<E> cls, StyleableSet<E> styleableSet) {
        sStyleables.put(cls, styleableSet);
    }

    @Override
    public void collectAttrs(Map<Integer, IStyleable<? extends E>> attrs) {
        attrs.putAll(mStyleables);
    }

    @SuppressWarnings("unchecked")
    public static <E extends View> AttrValueSet<E> createAttrValueSet(E view, AttributeSet attrs) {
        Log.d(TAG, "createAttrValueSet " + view);
//        if (view.getId() == R.id.information_fragment) {
//            Log.d(TAG, "createAttrValueSet " + view);
//        }
        Context context = view.getContext();
        Map<Integer, IStyleable<? extends E>> styleableAttrs = new TreeMap<>();
        Class<? extends View> cls = (Class<? extends View>) view.getClass();
        int defStyleAttr = 0;
        while (cls != null) {
            IStyleableSet<E> styleableSet = (IStyleableSet<E>) StyleableSet.get(cls);
            if (styleableSet != null) {
                styleableSet.collectAttrs(styleableAttrs);
                if (defStyleAttr == 0)
                    defStyleAttr = styleableSet.defStyleAttr();
            }
            if (cls == View.class)
                break;
            cls = (Class<? extends View>) cls.getSuperclass();
        }
        int[] styleableAttrsArray = new int[styleableAttrs.size()];
        int i = 0;
        for (Integer attr : styleableAttrs.keySet())
            styleableAttrsArray[i++] = attr;
        AttrValueSet<E> attrValueSet = new AttrValueSet<>();
        TypedArray a = context.obtainStyledAttributes(attrs, styleableAttrsArray, defStyleAttr, 0);
        TypedValue v = new TypedValue();
        for (i = 0; i < a.getIndexCount(); ++i) {
            int index = a.getIndex(i);
            a.getValue(index, v);
            if (AttrValue.maybeThemed(v)) {
                int attr = styleableAttrsArray[index];
                Log.d(TAG, "analyze " + context.getResources().getResourceName(attr) + "=" + context.getResources().getResourceName(v.resourceId));
                IStyleable<E> styleable = (IStyleable<E>) styleableAttrs.get(attr);
                attrValueSet.put(attr, styleable, v);
                v = new TypedValue();
                styleable.prepare(context, attrValueSet);
            }
        }
        a.recycle();
        return attrValueSet;
    }

    final Map<Integer, IStyleable<E>> mStyleables = new TreeMap<>();

    protected void addStyleable(@AttrRes int attr, IStyleable<E> styleable) {
        mStyleables.put(attr, styleable);
    }

    protected IStyleable<E> getStyleable(@AttrRes int attr) {
        return mStyleables.get(attr);
    }

}
