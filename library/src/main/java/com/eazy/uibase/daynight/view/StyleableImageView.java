package com.eazy.uibase.daynight.view;

import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableImageView extends StyleableSet<ImageView> {

    public StyleableImageView() {
        addStyleable(android.R.attr.src, (view, value) ->
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), value.resourceId)));
    }
}
