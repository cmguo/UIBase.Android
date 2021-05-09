package com.eazy.uibase.daynight.styleable;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import static android.content.pm.ActivityInfo.CONFIG_DENSITY;
import static android.content.pm.ActivityInfo.CONFIG_UI_MODE;

public class AttrValue<E extends View> {

    private static final String TAG = "AttrValue";

    public static <E extends View> AttrValue<E> make(IStyleable<E> styleable, TypedValue value) {
        AttrValue<E> attrValue = new AttrValue<>();
        attrValue.mStyleable = styleable;
        attrValue.mValue = value;
        return attrValue;
    }

    IStyleable<E> mStyleable;
    TypedValue mValue;

    public TypedValue getValue() {
        return mValue;
    }

    public static boolean maybeThemed(TypedValue v) {
        // TODO: SDK21(5.1) v.changingConfigurations = 4096
        return (v.changingConfigurations & (CONFIG_UI_MODE | CONFIG_DENSITY)) != 0
            || v.type == TypedValue.TYPE_REFERENCE
            || (v.type == TypedValue.TYPE_STRING && v.data != 0 && v.string.toString().startsWith("res/"));
    }

    public static boolean isThemed(TypedValue v) {
        return v.type == TypedValue.TYPE_ATTRIBUTE || v.changingConfigurations != 0;
    }

    public boolean isFromTheme() {
        return mValue.type == TypedValue.TYPE_ATTRIBUTE;
    }

    public void apply(E view) {
        Log.d(TAG, "apply " + mValue);
        Context context = view.getContext();
        int id = mValue.resourceId;
        TypedValue value = new TypedValue();
        if (isFromTheme()) {
            if (!context.getTheme().resolveAttribute(id, value, false)) {
                return;
            }
            if (value.type != TypedValue.TYPE_REFERENCE)
                return;
        } else {
            value.resourceId = id;
            value.type = TypedValue.TYPE_REFERENCE;
        }
        Log.d(TAG, "apply " + context.getResources().getResourceName(value.resourceId));
        mStyleable.apply(view, value);
    }

    @NotNull
    @Override
    public String toString() {
        return mValue.toString();
    }
}
