package com.xhb.uibase.daynight.styleable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public interface IStyleableSet<E extends View> {

    void analyze(Context context, AttributeSet attrs, AttrValueSet<? extends E> attrValueSet);

}
