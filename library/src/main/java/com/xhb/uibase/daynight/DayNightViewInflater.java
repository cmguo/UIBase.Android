package com.xhb.uibase.daynight;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xhb.uibase.daynight.styleable.AttrValueSet;
import com.xhb.uibase.daynight.styleable.StyleableSet;

import java.util.Map;
import java.util.WeakHashMap;

public class DayNightViewInflater extends DayNightBaseViewInflater {

    private static final String TAG = "DayNightViewInflater";

    public DayNightViewInflater() {
        DayNightManager.getInstance().addActiveViewInflater(this);
    }

    private final Map<View, AttrValueSet<View>> mStyledViews = new WeakHashMap<>();

    public void updateViews() {
        Log.d(TAG, "updateViews");
        for (Map.Entry<View, AttrValueSet<View>> view : mStyledViews.entrySet()) {
            view.getValue().apply(view.getKey());
        }
    }

    @Override
    protected void inspectView(View view, AttributeSet attrs) {
        AttrValueSet<View> styleSet = StyleableSet.createAttrValueSet(view, attrs);
        if (!styleSet.isEmpty()) {
            mStyledViews.put(view, styleSet);
        }
    }

}
