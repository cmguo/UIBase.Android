package com.xhb.uibase.daynight.view

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.auto.service.AutoService
import com.xhb.uibase.R
import com.xhb.uibase.daynight.styleable.IStyleableSet
import com.xhb.uibase.daynight.styleable.StyleableSet
import com.xhb.uibase.widget.tabbar.XHBLineIndicator

@AutoService(IStyleableSet::class)
class StyleableSetXHBLineIndicator : StyleableSet<XHBLineIndicator>() {

    init {
        addStyleable(R.attr.color) { view: XHBLineIndicator, value: TypedValue ->
            view.color = ContextCompat.getColor(view.context, value.resourceId)
        }
        addStyleable(R.attr.longLineColor) { view: XHBLineIndicator, value: TypedValue ->
            view.longLineColor = ContextCompat.getColor(view.context, value.resourceId)
        }
    }

    override fun defStyleAttr(): Int {
        return R.attr.lineIndicatorStyle
    }
}
