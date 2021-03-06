package com.eazy.uibase.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.eazy.uibase.R
import com.eazy.uibase.resources.ShapeDrawables

class ZRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs, defStyleAttr) {

    companion object {
        var backgroundDrawable = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
                R.dimen.radio_button_radius, R.color.bluegrey00_disabled,
                R.dimen.radio_button_border_size, R.color.bluegrey500_checked_disabled,
                R.dimen.radio_button_icon_size, R.dimen.radio_button_icon_size
        )
        var foregroundDrawable = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
                R.dimen.radio_button_radius, R.color.transparent_checked_disabled,
                R.dimen.radio_button_border_size2, R.color.transparent,
                R.dimen.radio_button_icon_size, R.dimen.radio_button_icon_size
        )
    }

    init {
        val background = ShapeDrawables.getDrawable(context, backgroundDrawable)
        val foreground = ShapeDrawables.getDrawable(context, foregroundDrawable)
        val layer = LayerDrawable(arrayOf(background, foreground))
        buttonDrawable = layer
    }
}
