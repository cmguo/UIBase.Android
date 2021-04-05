package com.xhb.uibase.daynight.styleable;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xhb.uibase.R;
import com.xhb.uibase.utils.Generic;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public class StyleableSet<E extends View> implements IStyleableSet<E> {

    private static final String TAG = "StyleableSet";

    private static final String nsAndroid = "http://schemas.android.com/apk/res/android";

    private static final Map<Class<? extends View>, IStyleableSet<? extends View>> sStyleables = new HashMap<>();

    static {
        for (IStyleableSet<?> styleableSet : ServiceLoader.load(IStyleableSet.class)) {
            Class<? extends View> cls = Generic.getParamType(styleableSet.getClass(), StyleableSet.class, 0);
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

    @SuppressWarnings("unchecked")
    public static  <E extends View> AttrValueSet<E> createAttrValueSet(E view, AttributeSet attrs) {
        Log.d(TAG, "createAttrValueSet " + view);
        AttrValueSet<E> attrValueSet = new AttrValueSet<>();
        Class<? extends View> cls = (Class<? extends View>) view.getClass();
        while (cls != null) {
            IStyleableSet<? super View> styleableSet = (IStyleableSet<? super View>) StyleableSet.get(cls);
            if (styleableSet != null) {
                styleableSet.analyze(view.getContext(), attrs, attrValueSet);
            }
            if (cls == View.class)
                break;
            cls = (Class<? extends View>) cls.getSuperclass();
        }
        return attrValueSet;
    }

    final Map<String, IStyleable<E>> mStyleables = new TreeMap<>();

    protected void addStyleable(String attr, IStyleable<E> styleable) {
        mStyleables.put(attr, styleable);
    }

    protected IStyleable<E> getStyleable(String attr) {
        return mStyleables.get(attr);
    }

    @Override
    public void analyze(Context context, AttributeSet attrs, AttrValueSet<? extends E> attrValueSet) {
        for (Map.Entry<String, IStyleable<E>> os : mStyleables.entrySet()) {
            String attr = os.getKey();
            IStyleable<E> style = os.getValue();
            try {
                String value = attrs.getAttributeValue(nsAndroid, attr);
                if (value == null)
                    continue;
                Log.d(TAG, "analyze " + attr + "=" + value);
                char type = value.charAt(0);
                if (type != '?' && type != '@')
                    continue;
                int id = Integer.parseInt(value.substring(1));
                if (id == 0)
                    continue;
                Log.d(TAG, "analyze " + attr + "=" + context.getResources().getResourceName(id));
                attrValueSet.put(attr, style, id, type == '?');
            } catch (Throwable e) {
                Log.w(TAG, e);
            }
        }
    }
}
