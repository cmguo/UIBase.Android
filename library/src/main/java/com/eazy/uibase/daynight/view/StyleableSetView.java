package com.eazy.uibase.daynight.view;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableSetView extends StyleableSet<View> {

    public StyleableSetView() {
        addStyleable(android.R.attr.background, (view, value) ->
            view.setBackground(ContextCompat.getDrawable(view.getContext(), value.resourceId)));
    }
}
