package com.eazy.uibase.daynight.view;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.auto.service.AutoService;
import com.eazy.uibase.R;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableSetAppCompatRadioButton extends StyleableSet<AppCompatRadioButton> {

    public StyleableSetAppCompatRadioButton() {
        addStyleable(R.attr.buttonCompat, (view, value) ->
            view.setButtonDrawable(Drawables.getDrawable(view.getContext(), value.resourceId)));
    }
}
