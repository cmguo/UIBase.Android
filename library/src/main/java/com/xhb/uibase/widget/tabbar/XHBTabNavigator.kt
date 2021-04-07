package com.xhb.uibase.widget.tabbar

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.xhb.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class XHBTabNavigator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : CommonNavigator(context, attrs, if (defStyleAttr == 0) R.attr.tabNavigatorStyle else defStyleAttr) {

    var borderRadius = 0f
        set(value) {
            field = value
            if (titleContainer != null && value > 0) {
                val i = borderRadius.toInt()
                val lp = titleContainer.layoutParams as FrameLayout.LayoutParams
                lp.marginStart = i
                lp.marginEnd = i
                titleContainer.layoutParams = lp
                indicatorContainer.setPadding(i, 0, i, 0)
                indicatorContainer.clipToPadding = false
                indicatorContainer.clipChildren = false
            }
        }

    var gravityCenter: Boolean
        get() = isAdjustMode
        set(value) { isAdjustMode = value }

    private var _backgroundColorId = 0
    private val backgroundPaint = Paint()
    private val bounds = RectF()

    init {
        val style = if (defStyleAttr == 0) R.attr.tabNavigatorStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBTabNavigator, style, defStyleRes)
        borderRadius = a.getDimension(R.styleable.XHBTabNavigator_borderRadius, borderRadius)
        gravityCenter = a.getBoolean(R.styleable.XHBTabNavigator_gravityCenter, gravityCenter)
        _backgroundColorId = a.getResourceId(R.styleable.XHBTabNavigator_backgroundColor, 0)
        backgroundPaint.color = a.getColor(R.styleable.XHBTabNavigator_backgroundColor, -1)
        a.recycle()

        setWillNotDraw(false)
    }

    override fun init() {
        super.init()
        // sync margin
        borderRadius = borderRadius
    }

    override fun draw(canvas: Canvas) {
        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(bounds, borderRadius, borderRadius, backgroundPaint)
        super.draw(canvas)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (_backgroundColorId != 0)
            backgroundPaint.color = ContextCompat.getColor(context, _backgroundColorId)
    }

}
