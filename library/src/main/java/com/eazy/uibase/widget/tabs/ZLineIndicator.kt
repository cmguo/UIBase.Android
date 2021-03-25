package com.eazy.uibase.widget.tabs

import android.content.Context
import android.util.AttributeSet
import com.eazy.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class ZLineIndicator @JvmOverloads constructor(
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

    var color: Int
        get() = colors?.getOrNull(0) ?: 0
        set(value) { setColors(value) }
    
    init {
        val style = if (defStyleAttr == 0) R.attr.lineIndicatorStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZLineIndicator, style, 0)
        mode = a.getInt(R.styleable.ZLineIndicator_widthMode, mode)
        lineWidth = a.getDimension(R.styleable.ZLineIndicator_lineWidth, lineWidth)
        lineHeight = a.getDimension(R.styleable.ZLineIndicator_lineHeight, lineHeight)
        roundRadius = a.getDimension(R.styleable.ZLineIndicator_borderRadius, roundRadius)
        color = a.getColor(R.styleable.ZLineIndicator_color, color)
        a.recycle()
    }

}
