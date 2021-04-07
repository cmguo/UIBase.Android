package com.xhb.uibase.daynight.view

import androidx.appcompat.widget.AppCompatTextView
import com.google.auto.service.AutoService
import com.xhb.uibase.R
import com.xhb.uibase.daynight.styleable.IStyleableSet
import com.xhb.uibase.daynight.styleable.StyleableSet

@AutoService(IStyleableSet::class)
class StyleableSetAppCompatTextView : StyleableSet<AppCompatTextView>() {

    override fun defStyleAttr(): Int {
        return android.R.attr.textViewStyle;
    }

}
