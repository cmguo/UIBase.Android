package com.eazy.uibase.daynight.view;

import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableSetProgressBar extends StyleableSet<ProgressBar> {

    public StyleableSetProgressBar() {
        addStyleable(android.R.attr.progressDrawable, (view, value) ->
            view.setProgressDrawable(Drawables.getDrawable(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.progressTint, (view, value) ->
            view.setProgressTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.indeterminateDrawable, (view, value) ->
            view.setIndeterminateDrawable(Drawables.getDrawable(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.indeterminateTint, (view, value) ->
            view.setIndeterminateTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
    }
}
