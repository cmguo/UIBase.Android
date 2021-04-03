package com.xhb.uibase.widget

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.xhb.uibase.R
import com.xhb.uibase.resources.RoundDrawable

class XHBRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatRadioButton(context, attrs) {

    private var _textPadding : Int? = null

    init {
        val background = RoundDrawable(context, R.style.XHBRadioButton_Background)
        val foreground = RoundDrawable(context, R.style.XHBRadioButton_Foreground)
        val layer = LayerDrawable(arrayOf(background, foreground))
        buttonDrawable = layer
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        _textPadding = paddingLeft
        if (text.isNullOrEmpty())
            setPadding(0, paddingTop, paddingRight, paddingBottom)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (_textPadding != null)
            setPadding(if (text.isNullOrEmpty()) 0 else _textPadding!!, paddingTop, paddingRight, paddingBottom)
    }

    companion object {
        private const val TAG = "XHBRadioButton"
    }

}
