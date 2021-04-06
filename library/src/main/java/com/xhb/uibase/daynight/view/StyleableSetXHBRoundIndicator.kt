package com.xhb.uibase.daynight.view

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.auto.service.AutoService
import com.xhb.uibase.daynight.styleable.IStyleable
import com.xhb.uibase.daynight.styleable.StyleableSet
import com.xhb.uibase.widget.tabbar.XHBLineIndicator
import com.xhb.uibase.widget.tabbar.XHBRoundIndicator

@AutoService(IStyleable::class)
class StyleableSetXHBRoundIndicator : StyleableSet<XHBRoundIndicator>() {

    init {
        addStyleable("color", IStyleable { view: XHBRoundIndicator, value: TypedValue ->
            view.color = ContextCompat.getColor(view.context, value.data)
        })
    }
}