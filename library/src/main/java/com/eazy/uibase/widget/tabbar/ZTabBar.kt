package com.eazy.uibase.widget.tabbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.eazy.uibase.R
import com.eazy.uibase.binding.MagicIndicatorBindingAdapter
import com.eazy.uibase.view.ZTabAdapter
import net.lucode.hackware.magicindicator.MagicIndicator

class ZTabBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MagicIndicator(context, attrs, if (defStyleAttr == 0) R.attr.tabBarStyle else defStyleAttr) {

    var borderRadius = 0f

    private val backgroundPaint = Paint()
    private val bounds = RectF()

    init {
        val style = if (defStyleAttr == 0) R.attr.tabBarStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZTabBar, style, 0)
        val navigatorStyle = a.getResourceId(R.styleable.ZTabBar_navigatorStyle, 0);
        val titles = a.getResourceId(R.styleable.ZTabBar_titles, 0)
        a.recycle()

        // use defStyleAttr 1 to disable defStyleAttr and use defStyleRes
        val navigator = ZTabNavigator(context, null, 1, navigatorStyle)
        //val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        //lp.gravity = Gravity.CENTER_HORIZONTAL
        //navigator.layoutParams = lp
        setNavigator(navigator)

        // for test
        if (titles > 0) {
            MagicIndicatorBindingAdapter.setMagicIndicatorTitles<String>(this,
                titles, R.layout.tab_title, R.layout.round_indicator, null, null, null);
        }
    }

    override fun draw(canvas: Canvas) {
        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(bounds, borderRadius, borderRadius, backgroundPaint)
        super.draw(canvas)
    }

}
