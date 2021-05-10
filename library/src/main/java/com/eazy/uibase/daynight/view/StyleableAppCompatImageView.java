package com.eazy.uibase.daynight.view;

import androidx.appcompat.widget.AppCompatImageView;

import com.google.auto.service.AutoService;
import com.eazy.uibase.R;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableAppCompatImageView extends StyleableSet<AppCompatImageView> {

    public StyleableAppCompatImageView() {
        addStyleable(R.attr.srcCompat, (view, value) ->
            view.setImageDrawable(Drawables.getDrawable(view.getContext(), value.resourceId)));
    }
}
