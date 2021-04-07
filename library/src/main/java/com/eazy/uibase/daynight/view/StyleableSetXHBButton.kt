package com.eazy.uibase.daynight.view

import com.google.auto.service.AutoService
import com.eazy.uibase.R
import com.eazy.uibase.daynight.styleable.IStyleableSet
import com.eazy.uibase.daynight.styleable.StyleableSet
import com.eazy.uibase.widget.ZButton

@AutoService(IStyleableSet::class)
class StyleableSetZButton : StyleableSet<ZButton>() {

    override fun defStyleAttr(): Int {
        return R.attr.buttonStyle
    }

}
