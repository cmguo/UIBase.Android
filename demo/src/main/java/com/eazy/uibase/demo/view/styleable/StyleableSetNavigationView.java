package com.eazy.uibase.demo.view.styleable;

import androidx.core.content.ContextCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.auto.service.AutoService;
import com.eazy.uibase.R;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableSetNavigationView extends StyleableSet<NavigationView> {

    @Override
    public int defStyleAttr() {
        return R.attr.navigationViewStyle;
    }

    public StyleableSetNavigationView() {
        addStyleable(R.attr.itemTextColor, (view, value) ->
            view.setItemTextColor(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
        addStyleable(R.attr.itemIconTint, (view, value) ->
            view.setItemIconTintList(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
    }
}
