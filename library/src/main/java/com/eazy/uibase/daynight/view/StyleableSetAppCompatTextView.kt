package com.eazy.uibase.daynight.view

import androidx.appcompat.widget.AppCompatTextView
import com.google.auto.service.AutoService
import com.eazy.uibase.R
import com.eazy.uibase.daynight.styleable.IStyleableSet
import com.eazy.uibase.daynight.styleable.StyleableSet

@AutoService(IStyleableSet::class)
class StyleableSetAppCompatTextView : StyleableSet<AppCompatTextView>() {

    override fun defStyleAttr(): Int {
        return android.R.attr.textViewStyle;
    }

}
