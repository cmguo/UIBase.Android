package com.xhb.uibase.daynight.styleable;

import android.util.TypedValue;
import android.view.View;

public interface IStyleable<E extends View> {

    void apply(E view, TypedValue value);

}
