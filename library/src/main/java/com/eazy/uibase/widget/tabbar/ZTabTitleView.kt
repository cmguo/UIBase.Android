package com.eazy.uibase.widget.tabbar

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import com.eazy.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

class ZTabTitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ColorTransitionPagerTitleView(context) {

    var textSizeNormal = 16f
    var textSizeSelected = 16f

    init {
        tag = attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "tag")
        val style = if (defStyleAttr == 0) R.attr.tabTitleStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZTabTitleView, style, 0)
        val color = a.getColorStateList(R.styleable.ZTabTitleView_android_textColor)
        if (color != null) {
            normalColor = color.defaultColor
            selectedColor = color.getColorForState(intArrayOf(android.R.attr.state_selected), normalColor)
        }
        textSizeNormal = a.getDimension(R.styleable.ZTabTitleView_textSizeNormal, textSizeNormal)
        textSizeSelected = a.getDimension(R.styleable.ZTabTitleView_textSizeSelected, textSizeSelected)
        a.recycle()
    }

    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
        super.onEnter(index, totalCount, enterPercent, leftToRight)
        val size = (1f - enterPercent) * textSizeNormal + enterPercent * textSizeSelected
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        typeface = Typeface.defaultFromStyle(if (enterPercent > 0.5f) Typeface.BOLD else Typeface.NORMAL)
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
        super.onLeave(index, totalCount, leavePercent, leftToRight)
        val size = (1f - leavePercent) * textSizeSelected + leavePercent * textSizeNormal
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        typeface = Typeface.defaultFromStyle(if (leavePercent < 0.5f) Typeface.BOLD else Typeface.NORMAL)
    }

}
