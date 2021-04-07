package com.xhb.uibase.daynight.styleable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

import java.util.Map;
import java.util.Set;

public interface IStyleableSet<E extends View> {

    @AttrRes
    default int defStyleAttr() { return 0; }

    void collectAttrs(Map<Integer, IStyleable<? extends E>> attrs);



}
