package com.eazy.uibase.daynight.view;

import androidx.appcompat.widget.AppCompatSpinner;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableSetAppCompatSpinner extends StyleableSet<AppCompatSpinner> {

    public StyleableSetAppCompatSpinner() {
        addStyleable(android.R.attr.popupBackground, (view, value) ->
            view.setPopupBackgroundDrawable(Drawables.getDrawable(view.getContext(), value.resourceId)));
    }
}
