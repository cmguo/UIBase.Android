package com.xhb.uibase.widget.tabbar

import android.content.Context
import android.util.AttributeSet
import com.xhb.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator

class XHBRoundIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WrapPagerIndicator(context, attrs, if (defStyleAttr == 0) R.attr.roundIndicatorStyle else defStyleAttr) {

    var paddingX: Int
        get() = horizontalPadding
        set(value) { horizontalPadding = value }

    var paddingY: Int
        get() = verticalPadding
        set(value) { verticalPadding = value }

    var borderRadius: Float
        get() = roundRadius
        set(value) { roundRadius = value }

    var color: Int
        get() = fillColor
        set(value) { fillColor = value }
    
    init {
        val style = if (defStyleAttr == 0) R.attr.roundIndicatorStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBRoundIndicator, style, 0)
        paddingX = a.getDimensionPixelSize(R.styleable.XHBRoundIndicator_paddingX, paddingX)
        paddingY = a.getDimensionPixelSize(R.styleable.XHBRoundIndicator_paddingY, paddingY)
        roundRadius = a.getDimension(R.styleable.XHBRoundIndicator_borderRadius, roundRadius)
        color = a.getColor(R.styleable.XHBRoundIndicator_color, color)
        a.recycle()
    }

}
