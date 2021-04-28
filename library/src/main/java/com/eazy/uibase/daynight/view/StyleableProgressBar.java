package com.eazy.uibase.daynight.view;

import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableProgressBar extends StyleableSet<ProgressBar> {

    public StyleableProgressBar() {
        addStyleable(android.R.attr.progressDrawable, (view, value) ->
            view.setProgressDrawable(ContextCompat.getDrawable(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.progressTint, (view, value) ->
            view.setProgressTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.indeterminateDrawable, (view, value) ->
            view.setIndeterminateDrawable(ContextCompat.getDrawable(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.indeterminateTint, (view, value) ->
            view.setIndeterminateTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
    }
}
