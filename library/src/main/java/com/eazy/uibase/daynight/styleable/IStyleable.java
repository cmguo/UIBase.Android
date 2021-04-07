package com.eazy.uibase.daynight.styleable;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.AnyRes;

@FunctionalInterface
public interface IStyleable<E extends View> {

    default void prepare(Context context, AttrValueSet<E> attrValueSet) {}

    void apply(E view, TypedValue value);

}
