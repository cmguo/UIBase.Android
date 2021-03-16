package com.xhb.uibase.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.xhb.uibase.R
import com.xhb.uibase.resources.ShapeDrawables

class XHBRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs) {

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

    private var text_padding_ : Int? = null

    init {
        val background = ShapeDrawables.getDrawable(context, backgroundDrawable)
        val foreground = ShapeDrawables.getDrawable(context, foregroundDrawable)
        val layer = LayerDrawable(arrayOf(background, foreground))
        buttonDrawable = layer
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        text_padding_ = paddingLeft
        if (text.isNullOrEmpty())
            setPadding(0, paddingTop, paddingRight, paddingBottom)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (text_padding_ != null)
            setPadding(if (text.isNullOrEmpty()) 0 else text_padding_!!, paddingTop, paddingRight, paddingBottom)
    }

}
