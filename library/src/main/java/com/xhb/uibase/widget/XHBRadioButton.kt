package com.xhb.uibase.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.xhb.uibase.R
import com.xhb.uibase.resources.ShapeDrawables

class XHBRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs, defStyleAttr) {

    companion object {
        val backgroundDrawable = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
            R.dimen.radio_radius, R.color.xhb_bluegrey00_checked_disabled,
            R.dimen.radio_border_size, R.color.xhb_bluegrey500_checked_disabled,
            R.dimen.radio_icon_size, R.dimen.radio_icon_size
        )
    }

    init {
        buttonDrawable = ShapeDrawables.getDrawable(context, backgroundDrawable)
    }
}
