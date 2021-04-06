package com.eazy.uibase.daynight.view

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.auto.service.AutoService
import com.eazy.uibase.daynight.styleable.IStyleable
import com.eazy.uibase.daynight.styleable.StyleableSet
import com.eazy.uibase.widget.tabbar.ZLineIndicator

@AutoService(IStyleable::class)
class StyleableSetZLineIndicator : StyleableSet<ZLineIndicator>() {

    init {
        addStyleable("color", IStyleable { view: ZLineIndicator, value: TypedValue ->
            view.color = ContextCompat.getColor(view.context, value.data)
        })
        addStyleable("longLineColor", IStyleable { view: ZLineIndicator, value: TypedValue ->
            view.longLineColor = ContextCompat.getColor(view.context, value.data)
        })
    }
}