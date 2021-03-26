package com.eazy.uibase.widget.tabbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.eazy.uibase.R
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class ZTabBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MagicIndicator(context, attrs, if (defStyleAttr == 0) R.attr.tabBarStyle else defStyleAttr) {

    var borderRadius = 0f

    private val backgroundPaint = Paint()
    private val bounds = RectF()

    init {
        val style = if (defStyleAttr == 0) R.attr.tabBarStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZTabBar, style, 0)
        borderRadius = a.getDimension(R.styleable.ZTabBar_borderRadius, borderRadius)
        backgroundPaint.color = a.getColor(R.styleable.ZTabBar_backgroundColor, -1)
        a.recycle()

        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas) {
        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(bounds, borderRadius, borderRadius, backgroundPaint)
        super.draw(canvas)
    }

}
