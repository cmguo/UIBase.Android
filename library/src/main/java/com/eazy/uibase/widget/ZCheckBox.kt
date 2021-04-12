package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.RoundDrawable

class ZCheckBox @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatCheckBox(context, attrs) {

    enum class CheckedState {
        NotChecked,
        HalfChecked,
        FullChecked
    }

    @FunctionalInterface
    interface OnCheckedStateChangeListener {
        fun onCheckedStateChanged(checkBox: ZCheckBox, state: CheckedState)
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
            _halfChecked = value == CheckedState.HalfChecked
            super.setChecked(value == CheckedState.FullChecked)
            onCheckedStateChangeListener?.onCheckedStateChanged(this, value)
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
        private const val TAG = "ZCheckBox"

        private fun createButtonDrawable(context: Context) : Drawable {
            val background = RoundDrawable(context, R.style.ZCheckBox_Background)
            val foreground = ContextCompat.getDrawable(context, R.drawable.check_box_foreground)
            if (Build.VERSION.SDK_INT < 25) {
                // TODO: SDK21 (5.1) VectorDrawable not support ColorStateList
                val layer = foreground as LayerDrawable
                layer.getDrawable(0).setTintList(ContextCompat.getColorStateList(context, R.color.transparent_halfchecked_disabled))
                layer.getDrawable(1).setTintList(ContextCompat.getColorStateList(context, R.color.transparent_checked_disabled2))
            }
            return LayerDrawable(arrayOf(background, foreground))
        }
    }

}