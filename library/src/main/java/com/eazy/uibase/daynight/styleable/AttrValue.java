package com.eazy.uibase.daynight.styleable;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import org.jetbrains.annotations.NotNull;

public class AttrValue<E extends View> {

    private static final String TAG = "AttrValue";

    public static <E extends View> AttrValue<E> make(IStyleable<E> styleable, int id, boolean fromTheme) {
        AttrValue<E> attrValue = new AttrValue<>();
        attrValue.mStyleable = styleable;
        attrValue.mValue = id;
        attrValue.mFromTheme = fromTheme;
        return attrValue;
    }

    IStyleable<E> mStyleable;
    int mValue;
    boolean mFromTheme;

    public int getValue() {
        return mValue;
    }

    public boolean isFromTheme() {
        return mFromTheme;
    }

    public void apply(E view) {
        Log.d(TAG, "apply " + mValue + " " + mFromTheme);
        Context context = view.getContext();
        int id = mValue;
        TypedValue value = new TypedValue();
        if (mFromTheme) {
            if (!context.getTheme().resolveAttribute(id, value, false)) {
                return;
            }
            if (value.type != TypedValue.TYPE_REFERENCE)
                return;
        } else {
            value.data = id;
            value.type = TypedValue.TYPE_REFERENCE;
        }
        Log.d(TAG, "apply " + context.getResources().getResourceName(value.data));
        mStyleable.apply(view, value);
    }

    @NotNull
    @Override
    public String toString() {
        return Integer.toHexString(mValue) + ": " + mStyleable;
    }
}
