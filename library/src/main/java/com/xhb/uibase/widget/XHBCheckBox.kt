package com.xhb.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.xhb.uibase.R
import com.xhb.uibase.resources.RoundDrawable

class XHBCheckBox @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatCheckBox(context, attrs) {

    enum class CheckedState {
        NotChecked,
        HalfChecked,
        FullChecked
    }

    @FunctionalInterface
    interface OnCheckedStateChangeListener {
        fun onCheckedStateChanged(checkBox: XHBCheckBox, state: CheckedState)
    }

    private var _halfChecked: Boolean = false
    private var _textPadding : Int? = null
    private var onCheckedStateChangeListener: OnCheckedStateChangeListener? = null

    var checkedState: CheckedState
        get() {
            return when {
                isChecked -> CheckedState.FullChecked
                _halfChecked -> CheckedState.HalfChecked
                else -> CheckedState.NotChecked
            }
        }
        set(value) {
            isChecked = value == CheckedState.FullChecked
            _halfChecked = value == CheckedState.HalfChecked
            refreshDrawableState()
        }

    init {
        buttonDrawable = createButtonDrawable(context)
    }

    fun setOnCheckedStateChangeListener(listener: OnCheckedStateChangeListener?) {
        onCheckedStateChangeListener = listener
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

    override fun setChecked(checked: Boolean) {
        _halfChecked = false
        super.setChecked(checked)
        onCheckedStateChangeListener?.onCheckedStateChanged(this, checkedState)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (_halfChecked)
            mergeDrawableStates(drawableState, intArrayOf(R.attr.state_half_checked))
        return drawableState
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        buttonDrawable = createButtonDrawable(context)
    }

    companion object {
        private const val TAG = "XHBCheckBox"

        private fun createButtonDrawable(context: Context) : Drawable {
            val background = RoundDrawable(context, R.style.XHBCheckBox_Background)
            val foreground = ContextCompat.getDrawable(context, R.drawable.check_box_foreground)
            return LayerDrawable(arrayOf(background, foreground))
        }
    }

}