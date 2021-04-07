package com.eazy.uibase.daynight.view

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.auto.service.AutoService
import com.eazy.uibase.R
import com.eazy.uibase.daynight.styleable.IStyleableSet
import com.eazy.uibase.daynight.styleable.StyleableSet
import com.eazy.uibase.widget.tabbar.ZLineIndicator

@AutoService(IStyleableSet::class)
class StyleableSetZLineIndicator : StyleableSet<ZLineIndicator>() {

    init {
        addStyleable(R.attr.color) { view: ZLineIndicator, value: TypedValue ->
            view.color = ContextCompat.getColor(view.context, value.resourceId)
        }
        addStyleable(R.attr.longLineColor) { view: ZLineIndicator, value: TypedValue ->
            view.longLineColor = ContextCompat.getColor(view.context, value.resourceId)
        }
    }

    override fun defStyleAttr(): Int {
        return R.attr.lineIndicatorStyle
    }
}
