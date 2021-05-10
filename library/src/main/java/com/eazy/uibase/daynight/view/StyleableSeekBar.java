package com.eazy.uibase.daynight.view;

import android.widget.SeekBar;

import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableSeekBar extends StyleableSet<SeekBar> {

    public StyleableSeekBar() {
        addStyleable(android.R.attr.thumb, (view, value) ->
            view.setThumb(Drawables.getDrawable(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.thumbTint, (view, value) ->
            view.setThumbTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
    }
}
