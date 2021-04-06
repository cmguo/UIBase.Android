package com.xhb.uibase.daynight.view

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.auto.service.AutoService
import com.xhb.uibase.daynight.styleable.IStyleable
import com.xhb.uibase.daynight.styleable.StyleableSet
import com.xhb.uibase.widget.tabbar.XHBLineIndicator

@AutoService(IStyleable::class)
class StyleableSetXHBLineIndicator : StyleableSet<XHBLineIndicator>() {

    init {
        addStyleable("color", IStyleable { view: XHBLineIndicator, value: TypedValue ->
            view.color = ContextCompat.getColor(view.context, value.data)
        })
        addStyleable("longLineColor", IStyleable { view: XHBLineIndicator, value: TypedValue ->
            view.longLineColor = ContextCompat.getColor(view.context, value.data)
        })
    }
}