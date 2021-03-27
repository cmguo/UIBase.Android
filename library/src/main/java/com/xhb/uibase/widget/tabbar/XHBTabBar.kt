package com.xhb.uibase.widget.tabbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xhb.uibase.R
import net.lucode.hackware.magicindicator.MagicIndicator

class XHBTabBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MagicIndicator(context, attrs, if (defStyleAttr == 0) R.attr.tabBarStyle else defStyleAttr) {

    var borderRadius = 0f

    private val backgroundPaint = Paint()
    private val bounds = RectF()

    init {
        val style = if (defStyleAttr == 0) R.attr.tabBarStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBTabBar, style, 0)
        val navigatorStyle = a.getResourceId(R.styleable.XHBTabBar_navigatorStyle, 0);
        a.recycle()

        // use defStyleAttr 1 to disable defStyleAttr and use defStyleRes
        val navigator = XHBTabNavigator(context, null, 1, navigatorStyle)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        lp.gravity = Gravity.CENTER_HORIZONTAL
        navigator.layoutParams = lp
        setNavigator(navigator)
    }

    override fun draw(canvas: Canvas) {
        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(bounds, borderRadius, borderRadius, backgroundPaint)
        super.draw(canvas)
    }

}
