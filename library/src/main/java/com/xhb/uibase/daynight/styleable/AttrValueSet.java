package com.xhb.uibase.daynight.styleable;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.AttrRes;

import java.util.Map;
import java.util.TreeMap;

public class AttrValueSet<E extends View> {

    private final static String TAG = "AttrValueSet";

    final Map<Integer, AttrValue<? super E>> mAttrValues = new TreeMap<>();

    public void put(@AttrRes int attr, IStyleable<? super E> styleable, TypedValue value) {
        mAttrValues.put(attr, AttrValue.make(styleable, value));
    }

    public AttrValue<? super E> get(@AttrRes int attr) {
        return mAttrValues.get(attr);
    }

    public void remove(@AttrRes int attr) {
        mAttrValues.remove(attr);
    }

    public void apply(E view) {
        Log.d(TAG, "apply " + view);
        for (Map.Entry<Integer, AttrValue<? super E>> entry : mAttrValues.entrySet()) {
            Log.d(TAG, "apply " + view.getResources().getResourceName(entry.getKey()));
            entry.getValue().apply(view);
        }
    }

    public boolean isEmpty() {
        return mAttrValues.isEmpty();
    }
}
