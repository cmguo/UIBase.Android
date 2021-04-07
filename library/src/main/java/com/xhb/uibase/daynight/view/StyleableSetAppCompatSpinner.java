package com.xhb.uibase.daynight.view;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.xhb.uibase.daynight.styleable.IStyleableSet;
import com.xhb.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableSetAppCompatSpinner extends StyleableSet<AppCompatSpinner> {

    public StyleableSetAppCompatSpinner() {
        addStyleable(android.R.attr.popupBackground, (view, value) ->
            view.setPopupBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), value.resourceId)));
    }
}
