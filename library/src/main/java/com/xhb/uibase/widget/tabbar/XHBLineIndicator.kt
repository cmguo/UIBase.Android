package com.xhb.uibase.widget.tabbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.xhb.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class XHBLineIndicator @JvmOverloads constructor(
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
        get() = yOffset
        set(value) { yOffset = value }

    var color: Int
        get() = colors?.getOrNull(0) ?: 0
        set(value) { setColors(value) }

    var longLineColor: Int
        get() = longLinePaint.color
        set(value) {
            longLinePaint.color = value
            invalidate()
        }

    var longLineHeight = 0f
        set(value) {
            val lh = lineHeight
            field = value
            lineHeight = lh
        }

    private val longLineRect = RectF()
    private val longLinePaint = Paint()

    init {
        val style = if (defStyleAttr == 0) R.attr.lineIndicatorStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBLineIndicator, style, 0)
        mode = a.getInt(R.styleable.XHBLineIndicator_widthMode, mode)
        lineWidth = a.getDimension(R.styleable.XHBLineIndicator_lineWidth, lineWidth)
        lineHeight = a.getDimension(R.styleable.XHBLineIndicator_lineHeight, lineHeight)
        longLineHeight = a.getDimension(R.styleable.XHBLineIndicator_longLineHeight, longLineHeight)
        offsetX = a.getDimension(R.styleable.XHBLineIndicator_offsetX, offsetX)
        offsetY = a.getDimension(R.styleable.XHBLineIndicator_offsetY, offsetY)
        roundRadius = a.getDimension(R.styleable.XHBLineIndicator_borderRadius, roundRadius)
        color = a.getColor(R.styleable.XHBLineIndicator_color, color)
        longLineColor = a.getColor(R.styleable.XHBLineIndicator_longLineColor, longLineColor)
        a.recycle()
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        longLineRect.set(l.toFloat(), b - offsetY - longLineHeight, r.toFloat(), b - offsetY)
    }

    override fun onDraw(canvas: Canvas) {
        if (longLineHeight > 0) {
            canvas.drawRect(longLineRect, longLinePaint)
        }
        super.onDraw(canvas)
    }
}
