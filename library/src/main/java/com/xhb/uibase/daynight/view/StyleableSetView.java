package com.xhb.uibase.daynight.view;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.xhb.uibase.daynight.styleable.IStyleableSet;
import com.xhb.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableSetView extends StyleableSet<View> {

    public StyleableSetView() {
        addStyleable("background", (view, value) ->
            view.setBackground(ContextCompat.getDrawable(view.getContext(), value.data)));
    }
}
