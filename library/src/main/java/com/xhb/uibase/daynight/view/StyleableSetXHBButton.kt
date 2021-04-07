package com.xhb.uibase.daynight.view

import com.google.auto.service.AutoService
import com.xhb.uibase.R
import com.xhb.uibase.daynight.styleable.IStyleableSet
import com.xhb.uibase.daynight.styleable.StyleableSet
import com.xhb.uibase.widget.XHBButton

@AutoService(IStyleableSet::class)
class StyleableSetXHBButton : StyleableSet<XHBButton>() {

    override fun defStyleAttr(): Int {
        return R.attr.buttonStyle
    }

}
