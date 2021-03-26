package com.xhb.uibase.widget.tabbar

import android.content.Context
import android.util.AttributeSet
import com.xhb.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class XHBLineIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinePagerIndicator(context) {

    enum class WidthMode
    {
        MatchEdge,
        WrapContent,
        Exactly
    }

    var widthMode: WidthMode
        get() = WidthMode.values()[mode]
        set(value) {  mode = value.ordinal }

    var borderRadius: Float
        get() = roundRadius
        set(value) { roundRadius = value }

    var offsetX: Float
        get() = xOffset
        set(value) { xOffset = value }

    var offsetY: Float
        get() = yOffset
        set(value) { yOffset = value }

    var color: Int
        get() = colors?.getOrNull(0) ?: 0
        set(value) { setColors(value) }
    
    init {
        tag = attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "tag")
        val style = if (defStyleAttr == 0) R.attr.lineIndicatorStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBLineIndicator, style, 0)
        mode = a.getInt(R.styleable.XHBLineIndicator_widthMode, mode)
        lineWidth = a.getDimension(R.styleable.XHBLineIndicator_lineWidth, lineWidth)
        lineHeight = a.getDimension(R.styleable.XHBLineIndicator_lineHeight, lineHeight)
        offsetX = a.getDimension(R.styleable.XHBLineIndicator_offsetX, offsetX)
        offsetY = a.getDimension(R.styleable.XHBLineIndicator_offsetY, offsetY)
        roundRadius = a.getDimension(R.styleable.XHBLineIndicator_borderRadius, roundRadius)
        color = a.getColor(R.styleable.XHBLineIndicator_color, color)
        a.recycle()
    }

}
