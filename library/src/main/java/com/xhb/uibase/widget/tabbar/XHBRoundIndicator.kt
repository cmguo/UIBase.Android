package com.xhb.uibase.widget.tabbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.xhb.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator

class XHBRoundIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WrapPagerIndicator(context, attrs, if (defStyleAttr == 0) R.attr.roundIndicatorStyle else defStyleAttr) {

    enum class WidthMode
    {
        MatchEdge,
        WrapContent,
        Exactly
    }

    var widthMode: WidthMode
        get() = WidthMode.values()[mode]
        set(value) {  mode = value.ordinal }

    var paddingX: Int
        get() = horizontalPadding + shadowRadius.toInt()
        set(value) { horizontalPadding = value - shadowRadius.toInt() }

    var paddingY: Int
        get() = verticalPadding + shadowRadius.toInt()
        set(value) { verticalPadding = value - shadowRadius.toInt() }

    var borderRadius: Float
        get() = roundRadius
        set(value) { roundRadius = value }

    var shadowRadius = 0f
        set(value) {
            val px = paddingX
            val py = paddingY
            field = value
            paddingX = px
            paddingY = py
        }

    var color: Int
        get() = fillColor
        set(value) { fillColor = value }
    
    init {
        val style = if (defStyleAttr == 0) R.attr.roundIndicatorStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBRoundIndicator, style, 0)
        mode = a.getInt(R.styleable.XHBRoundIndicator_widthMode, mode)
        paddingX = a.getDimensionPixelSize(R.styleable.XHBRoundIndicator_paddingX, paddingX)
        paddingY = a.getDimensionPixelSize(R.styleable.XHBRoundIndicator_paddingY, paddingY)
        roundRadius = a.getDimension(R.styleable.XHBRoundIndicator_borderRadius, roundRadius)
        shadowRadius = a.getDimension(R.styleable.XHBRoundIndicator_shadowRadius, shadowRadius)
        color = a.getColor(R.styleable.XHBRoundIndicator_color, color)
        a.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        paint.setShadowLayer(shadowRadius, 0f, 0f, Color.LTGRAY)
        super.onDraw(canvas)
    }

}
