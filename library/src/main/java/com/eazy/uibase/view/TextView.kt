package com.eazy.uibase.view

import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.eazy.uibase.R

var TextView.textAppearance: Int
    get() = 0
    set(value) {
        TextViewCompat.setTextAppearance(this, value)
        val a = context.obtainStyledAttributes(value, R.styleable.ZTextAppearance)
        val lineHeight = a.getDimensionPixelSize(R.styleable.ZTextAppearance_lineHeight, -1)
        val lineSpacing = a.getDimensionPixelSize(R.styleable.ZTextAppearance_lineSpacing, -1)
        val gravity = a.getInteger(R.styleable.ZTextAppearance_android_gravity, -1)
        if (lineHeight > 0 || lineSpacing > 0) {
            val fontHeight: Int = getPaint().getFontMetricsInt(null)
            if (lineHeight < 0) {
                setLineSpacing(lineSpacing.toFloat(), 1f)
            } else {
                val padding = (lineHeight - fontHeight) / 2
                setPadding(paddingLeft, padding, paddingRight, padding)
                setLineSpacing((lineHeight + lineSpacing - fontHeight).toFloat(), 1f)
            }
        }
        if (gravity >= 0) {
            setGravity(gravity)
        }
    }
