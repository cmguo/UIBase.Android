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
import com.xhb.uibase.daynight.styleable.IStyleable;
import com.xhb.uibase.daynight.styleable.IStyleableSet;
import com.xhb.uibase.daynight.styleable.StyleableSet;

@AutoService(IStyleableSet.class)
public class StyleableSetTextView extends StyleableSet<TextView> {

    private static final String TAG = "StyleableSetTextView";

    public StyleableSetTextView() {
        addStyleable(android.R.attr.textColor, (view, value) ->
            view.setTextColor(ContextCompat.getColorStateList(view.getContext(), value.resourceId)));
        addStyleable(android.R.attr.textAppearance, new IStyleable<TextView>() {
            @Override
            @SuppressWarnings("unchecked")
            public void prepare(Context context, AttrValueSet<TextView> attrValueSet) {
                AttrValue<TextView> textAppearance = (AttrValue<TextView>) attrValueSet.get(android.R.attr.textAppearance);
                if (textAppearance == null || textAppearance.isThemed())
                    return;
                attrValueSet.remove(android.R.attr.textAppearance);
                if (attrValueSet.get(android.R.attr.textColor) == null) {
                    TypedValue value2 = textAppearance.getValue();
                    TypedArray a = context.obtainStyledAttributes(value2.resourceId, R.styleable.StyleableSetTextView);
                    TypedValue value = new TypedValue();
                    if (a.getValue(R.styleable.StyleableSetTextView_android_textColor, value)) {
                        if (AttrValue.maybeThemed(value)) {
                            Log.d(TAG, "analyze textColor=" + context.getResources().getResourceName(value.resourceId));
                            attrValueSet.put(android.R.attr.textColor, getStyleable(android.R.attr.textColor), value);
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

}
