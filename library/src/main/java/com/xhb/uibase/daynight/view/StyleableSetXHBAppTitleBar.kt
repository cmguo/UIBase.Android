package com.xhb.uibase.daynight.view

import com.google.auto.service.AutoService
import com.xhb.uibase.R
import com.xhb.uibase.daynight.styleable.IStyleableSet
import com.xhb.uibase.daynight.styleable.StyleableSet
import com.xhb.uibase.widget.XHBAppTitleBar

@AutoService(IStyleableSet::class)
class StyleableSetXHBAppTitleBar : StyleableSet<XHBAppTitleBar>() {

    override fun defStyleAttr(): Int {
        return R.attr.appTitleBarStyle
    }

}
