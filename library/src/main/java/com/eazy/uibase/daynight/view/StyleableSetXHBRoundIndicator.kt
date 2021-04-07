package com.eazy.uibase.daynight.view

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.auto.service.AutoService
import com.eazy.uibase.R
import com.eazy.uibase.daynight.styleable.IStyleableSet
import com.eazy.uibase.daynight.styleable.StyleableSet
import com.eazy.uibase.widget.tabbar.ZRoundIndicator

@AutoService(IStyleableSet::class)
class StyleableSetZRoundIndicator : StyleableSet<ZRoundIndicator>() {

    init {
        addStyleable(R.attr.color) { view: ZRoundIndicator, value: TypedValue ->
            view.color = ContextCompat.getColor(view.context, value.resourceId)
        }
        addStyleable(R.attr.splitterColor) { view: ZRoundIndicator, value: TypedValue ->
            view.splitterColor = ContextCompat.getColor(view.context, value.data)
        }
    }

    override fun defStyleAttr(): Int {
        return R.attr.roundIndicatorStyle
    }
}
