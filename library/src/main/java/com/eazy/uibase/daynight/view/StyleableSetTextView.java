package com.eazy.uibase.daynight.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.google.auto.service.AutoService;
import com.eazy.uibase.R;
import com.eazy.uibase.daynight.styleable.AttrValue;
import com.eazy.uibase.daynight.styleable.AttrValueSet;
import com.eazy.uibase.daynight.styleable.IStyleable;
import com.eazy.uibase.daynight.styleable.IStyleableSet;
import com.eazy.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableSetTextView extends StyleableSet<TextView> {

    private static final String TAG = "StyleableSetTextView";

    public StyleableSetTextView() {
        addStyleable(android.R.attr.textColor, (view, value) ->
            view.setTextColor(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.textColorHint, (view, value) ->
            view.setHintTextColor(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.textColorHighlight, (view, value) ->
            view.setHighlightColor(ContextCompat.getColor(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.textColorLink, (view, value) ->
            view.setLinkTextColor(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
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
        addStyleable(android.R.attr.textAppearance, new IStyleable<TextView>() {
            @Override
            @SuppressWarnings("unchecked")
            public void prepare(Context context, AttrValueSet<TextView> attrValueSet) {
                AttrValue<TextView> textAppearance = (AttrValue<TextView>) attrValueSet.get(android.R.attr.textAppearance);
                if (textAppearance == null)
                    return;
                attrValueSet.remove(android.R.attr.textAppearance);
                if (attrValueSet.get(android.R.attr.textColor) == null
                    || attrValueSet.get(android.R.attr.textColorHint) == null
                    || attrValueSet.get(android.R.attr.textColorHighlight) == null
                    || attrValueSet.get(android.R.attr.textColorLink) == null) {
                    TypedValue value2 = textAppearance.getValue();
                    TypedArray a = context.obtainStyledAttributes(value2.resourceId, R.styleable.StyleableSetTextView);
                    TypedValue value = new TypedValue();
                    if (attrValueSet.get(android.R.attr.textColor) == null && a.getValue(R.styleable.StyleableSetTextView_android_textColor, value)) {
                        if (AttrValue.maybeThemed(value)) {
                            Log.d(TAG, "analyze textColor=" + context.getResources().getResourceName(value.resourceId));
                            attrValueSet.put(android.R.attr.textColor, getStyleable(android.R.attr.textColor), value);
                        }
                    }
                    if (attrValueSet.get(android.R.attr.textColorHint) == null && a.getValue(R.styleable.StyleableSetTextView_android_textColorHint, value)) {
                        if (AttrValue.maybeThemed(value)) {
                            Log.d(TAG, "analyze textColorHint=" + context.getResources().getResourceName(value.resourceId));
                            attrValueSet.put(android.R.attr.textColorHint, getStyleable(android.R.attr.textColorHint), value);
                        }
                    }
                    if (attrValueSet.get(android.R.attr.textColorHighlight) == null && a.getValue(R.styleable.StyleableSetTextView_android_textColorHighlight, value)) {
                        if (AttrValue.maybeThemed(value)) {
                            Log.d(TAG, "analyze textColorHighlight=" + context.getResources().getResourceName(value.resourceId));
                            attrValueSet.put(android.R.attr.textColorHighlight, getStyleable(android.R.attr.textColorHighlight), value);
                        }
                    }
                    if (attrValueSet.get(android.R.attr.textColorLink) == null && a.getValue(R.styleable.StyleableSetTextView_android_textColorLink, value)) {
                        if (AttrValue.maybeThemed(value)) {
                            Log.d(TAG, "analyze textColorLink=" + context.getResources().getResourceName(value.resourceId));
                            attrValueSet.put(android.R.attr.textColorLink, getStyleable(android.R.attr.textColorLink), value);
                        }
                    }
                    a.recycle();
                }
            }
            @Override
            public void apply(TextView view, TypedValue value) {
                TextViewCompat.setTextAppearance(view, value.data);
            }
        });
    }

    private void setCompoundDrawable(TextView view, int index, TypedValue value) {
        Drawable[] drawables = view.getCompoundDrawables();
        drawables[index] = ContextCompat.getDrawable(view.getContext(), value.resourceId);
        view.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    private void setCompoundDrawableRelative(TextView view, int index, TypedValue value) {
        Drawable[] drawables = view.getCompoundDrawablesRelative();
        drawables[index] = ContextCompat.getDrawable(view.getContext(), value.resourceId);
        view.setCompoundDrawablesRelative(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

}
