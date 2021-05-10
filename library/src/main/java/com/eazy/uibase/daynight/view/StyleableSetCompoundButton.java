package com.eazy.uibase.daynight.view;

import android.widget.CompoundButton;

import androidx.core.content.ContextCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableSetCompoundButton extends StyleableSet<CompoundButton> {

    public StyleableSetCompoundButton() {
        addStyleable(android.R.attr.button, (view, value) ->
            view.setButtonDrawable(Drawables.getDrawable(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.buttonTint, (view, value) ->
            view.setButtonTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
    }
}
