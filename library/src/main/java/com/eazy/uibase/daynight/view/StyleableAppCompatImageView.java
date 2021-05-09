package com.eazy.uibase.daynight.view;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.auto.service.AutoService;
import com.eazy.uibase.R;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableAppCompatImageView extends StyleableSet<AppCompatImageView> {

    public StyleableAppCompatImageView() {
        addStyleable(R.attr.srcCompat, (view, value) ->
            view.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), value.resourceId)));
    }
}
