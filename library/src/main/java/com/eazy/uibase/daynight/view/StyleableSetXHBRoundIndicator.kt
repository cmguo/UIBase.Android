package com.eazy.uibase.daynight.view

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.auto.service.AutoService
import com.eazy.uibase.daynight.styleable.IStyleable
import com.eazy.uibase.daynight.styleable.StyleableSet
import com.eazy.uibase.widget.tabbar.ZLineIndicator
import com.eazy.uibase.widget.tabbar.ZRoundIndicator

@AutoService(IStyleable::class)
class StyleableSetZRoundIndicator : StyleableSet<ZRoundIndicator>() {

    init {
        addStyleable("color", IStyleable { view: ZRoundIndicator, value: TypedValue ->
            view.color = ContextCompat.getColor(view.context, value.data)
        })
    }
}