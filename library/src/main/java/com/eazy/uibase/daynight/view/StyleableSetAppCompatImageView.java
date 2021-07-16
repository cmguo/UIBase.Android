package com.eazy.uibase.daynight.view;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.R;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableSetAppCompatImageView extends StyleableSet<AppCompatImageView> {

    public StyleableSetAppCompatImageView() {
        addStyleable(R.attr.srcCompat, (view, value) ->
            view.setImageDrawable(Drawables.getDrawable(view.getContext(), value.resourceId)));
        addStyleable(R.attr.tint, (view, value) ->
                view.setImageTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
    }
}
