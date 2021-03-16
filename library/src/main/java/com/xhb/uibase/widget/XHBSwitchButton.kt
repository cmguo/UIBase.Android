package com.xhb.uibase.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import com.xhb.uibase.R
import com.xhb.uibase.resources.ShapeDrawables

class XHBSwitchButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SwitchCompat(context, attrs) {

    companion object {
        val trackDrawableConfig = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
            R.dimen.switch_button_radius, R.color.bluegrey300_checked,
            0, 0,
            0, 0
        )
        val thumbDrawableConfig = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
            R.dimen.switch_button_thumb_radius, R.color.bluegrey_00,
            R.dimen.switch_button_thumb_padding, R.color.transparent,
            R.dimen.switch_button_thumb_size, R.dimen.switch_button_thumb_size
        )
    }

    init {
        trackDrawable = ShapeDrawables.getDrawable(context, trackDrawableConfig)
        thumbDrawable = ShapeDrawables.getDrawable(context, thumbDrawableConfig)
    }
}
