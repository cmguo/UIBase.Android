package com.eazy.uibase.daynight.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.auto.service.AutoService;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;
import com.eazy.uibase.resources.Drawables;

@AutoService(IStyleableSet.class)
public class StyleableSetAppCompatTextView extends StyleableSet<AppCompatTextView> {

    private static final String TAG = "StyleableSetTextView";

    public StyleableSetAppCompatTextView() {
        // drawables
        addStyleable(android.R.attr.drawableLeft, (view, value) ->
            setCompoundDrawable(view, 0, value));
        addStyleable(android.R.attr.drawableTop, (view, value) ->
            setCompoundDrawable(view, 1, value));
        addStyleable(android.R.attr.drawableRight, (view, value) ->
            setCompoundDrawable(view, 2, value));
        addStyleable(android.R.attr.drawableBottom, (view, value) ->
            setCompoundDrawable(view, 3, value));
        addStyleable(android.R.attr.drawableStart, (view, value) ->
            setCompoundDrawableRelative(view, 0, value));
        addStyleable(android.R.attr.drawableEnd, (view, value) ->
            setCompoundDrawableRelative(view, 2, value));
    }

    private void setCompoundDrawable(TextView view, int index, TypedValue value) {
        Drawable[] drawables = view.getCompoundDrawables();
        drawables[index] = Drawables.getDrawable(view.getContext(), value.resourceId);
        view.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    private void setCompoundDrawableRelative(TextView view, int index, TypedValue value) {
        Drawable[] drawables = view.getCompoundDrawablesRelative();
        drawables[index] = Drawables.getDrawable(view.getContext(), value.resourceId);
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

}
