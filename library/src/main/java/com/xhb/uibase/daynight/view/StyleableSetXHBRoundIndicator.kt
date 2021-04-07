package com.xhb.uibase.daynight.view

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.auto.service.AutoService
import com.xhb.uibase.R
import com.xhb.uibase.daynight.styleable.IStyleableSet
import com.xhb.uibase.daynight.styleable.StyleableSet
import com.xhb.uibase.widget.tabbar.XHBRoundIndicator

@AutoService(IStyleableSet::class)
class StyleableSetXHBRoundIndicator : StyleableSet<XHBRoundIndicator>() {

    init {
        addStyleable(R.attr.color) { view: XHBRoundIndicator, value: TypedValue ->
            view.color = ContextCompat.getColor(view.context, value.resourceId)
        }
        addStyleable(R.attr.splitterColor) { view: XHBRoundIndicator, value: TypedValue ->
            view.splitterColor = ContextCompat.getColor(view.context, value.resourceId)
        }
    }

    override fun defStyleAttr(): Int {
        return R.attr.roundIndicatorStyle
    }
}
