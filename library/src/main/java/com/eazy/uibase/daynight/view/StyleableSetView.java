package com.eazy.uibase.daynight.view;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableSetView extends StyleableSet<View> {

    public StyleableSetView() {
        addStyleable(android.R.attr.background, (view, value) -> {
            int paddingLeft = view.getPaddingLeft();
            int paddingTop = view.getPaddingTop();
            int paddingRight = view.getPaddingRight();
            int paddingBottom = view.getPaddingBottom();
            ViewCompat.setBackground(view, ContextCompat.getDrawable(view.getContext(), value.resourceId));
            view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        });
    }
}
