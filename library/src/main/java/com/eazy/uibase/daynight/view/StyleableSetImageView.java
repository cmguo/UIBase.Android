package com.eazy.uibase.daynight.view;

import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableSetImageView extends StyleableSet<ImageView> {

    public StyleableSetImageView() {
        addStyleable(android.R.attr.src, (view, value) ->
            view.setImageDrawable(Drawables.getDrawable(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.tint, (view, value) ->
            view.setImageTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
    }
}
