package com.eazy.uibase.daynight.styleable;

import android.util.Log;
import android.view.View;

import java.util.Map;
import java.util.TreeMap;

public class AttrValueSet<E extends View> {

    private final static String TAG = "AttrValueSet";

    final Map<String, AttrValue<? super E>> mAttrValues = new TreeMap<>();

    public void put(String attr, IStyleable<? super E> styleable, int id, boolean fromTheme) {
        mAttrValues.put(attr, AttrValue.make(styleable, id, fromTheme));
    }

    public AttrValue<? super E> get(String attr) {
        return mAttrValues.get(attr);
    }

    public void remove(String attr) {
        mAttrValues.remove(attr);
    }

    public void apply(E view) {
        Log.d(TAG, "apply " + view);
        for (Map.Entry<String, AttrValue<? super E>> entry : mAttrValues.entrySet()) {
            Log.d(TAG, "apply " + entry.getKey());
            entry.getValue().apply(view);
        }
    }

    public boolean isEmpty() {
        return mAttrValues.isEmpty();
    }
}
