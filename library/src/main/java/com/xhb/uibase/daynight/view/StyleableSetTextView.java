package com.xhb.uibase.daynight.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.google.auto.service.AutoService;
import com.xhb.uibase.R;
import com.xhb.uibase.daynight.styleable.AttrValue;
import com.xhb.uibase.daynight.styleable.AttrValueSet;
import com.xhb.uibase.daynight.styleable.IStyleableSet;
import com.xhb.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableSetTextView extends StyleableSet<TextView> {

    private static final String TAG = "StyleableSetTextView";

    public StyleableSetTextView() {
        addStyleable("textColor", (view, value) ->
            view.setTextColor(ContextCompat.getColorStateList(view.getContext(), value.data)));
        addStyleable("textAppearance", (view, value) ->
            TextViewCompat.setTextAppearance(view, value.data));
    }

    @Override
    public void analyze(Context context, AttributeSet attrs, AttrValueSet<? extends TextView> attrValueSet) {
        super.analyze(context, attrs, attrValueSet);
        AttrValue<?> textAppearance = attrValueSet.get("textAppearance");
        if (textAppearance != null && !textAppearance.isFromTheme() && attrValueSet.get("textColor") == null) {
            TypedArray a = context.obtainStyledAttributes(textAppearance.getValue(), R.styleable.StyleableSetTextView);
            TypedValue value = new TypedValue();
            if (a.getValue(R.styleable.StyleableSetTextView_android_textColor, value)) {
                if (value.type == TypedValue.TYPE_STRING) { // xml color
                    value.type = TypedValue.TYPE_REFERENCE;
                    value.data = value.resourceId;
                }
                if (value.type == TypedValue.TYPE_ATTRIBUTE || value.type == TypedValue.TYPE_REFERENCE) {
                    Log.d(TAG, "analyze textColor=" + context.getResources().getResourceName(value.data));
                    attrValueSet.put("textColor", getStyleable("textColor"), value.data, value.type == TypedValue.TYPE_ATTRIBUTE);
                }
            }
            a.recycle();
        }
        attrValueSet.remove("textAppearance");
    }
}
