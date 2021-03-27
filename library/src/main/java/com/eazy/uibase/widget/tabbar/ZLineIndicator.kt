package com.eazy.uibase.widget.tabbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import com.eazy.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class ZLineIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinePagerIndicator(context, attrs, if (defStyleAttr == 0) R.attr.lineIndicatorStyle else defStyleAttr) {

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
        get() = yOffset - longLineHeight
        set(value) { yOffset = value + longLineHeight }

    var color: Int
        get() = colors?.getOrNull(0) ?: 0
        set(value) { setColors(value) }

    var longLineHeight = 0f
        set(value) {
            val lh = lineHeight
            field = value
            lineHeight = lh
        }

    init {
        val style = if (defStyleAttr == 0) R.attr.lineIndicatorStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZLineIndicator, style, 0)
        mode = a.getInt(R.styleable.ZLineIndicator_widthMode, mode)
        lineWidth = a.getDimension(R.styleable.ZLineIndicator_lineWidth, lineWidth)
        lineHeight = a.getDimension(R.styleable.ZLineIndicator_lineHeight, lineHeight)
        longLineHeight = a.getDimension(R.styleable.ZLineIndicator_longLineHeight, longLineHeight)
        offsetX = a.getDimension(R.styleable.ZLineIndicator_offsetX, offsetX)
        offsetY = a.getDimension(R.styleable.ZLineIndicator_offsetY, offsetY)
        roundRadius = a.getDimension(R.styleable.ZLineIndicator_borderRadius, roundRadius)
        color = a.getColor(R.styleable.ZLineIndicator_color, color)
        a.recycle()
    }

    private val longLineRect = RectF()

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        longLineRect.set(l.toFloat(), b - offsetY - longLineHeight, r.toFloat(), b - offsetY)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (longLineHeight > 0) {
            canvas.drawRect(longLineRect, paint)
        }
    }
}
