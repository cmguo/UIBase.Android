package com.eazy.uibase.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.ShapeDrawables

class ZCheckBox @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatCheckBox(context, attrs) {

    enum class CheckedState {
        NotChecked,
        HalfChecked,
        FullChecked
    }

    companion object {
        val backgroundDrawable = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
                R.dimen.check_box_radius, R.color.bluegrey00_checked_disabled,
                R.dimen.check_box_border_size, R.color.bluegrey500_checked_disabled,
                R.dimen.check_box_icon_size, R.dimen.check_box_icon_size
        )
    }

    @FunctionalInterface
    interface OnCheckedStateChangeListener {
        fun onCheckedStateChanged(checkBox: ZCheckBox, state: CheckedState)
    }

    private var _halfChecked: Boolean = false
    private var _textPadding : Int? = null
    private var onCheckedStateChangeListener: OnCheckedStateChangeListener? = null

    init {
        val background = ShapeDrawables.getDrawable(context, backgroundDrawable)
        val foreground = ContextCompat.getDrawable(context, R.drawable.check_box_foreground)
        buttonDrawable = LayerDrawable(arrayOf(background, foreground))
    }

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

    override fun onFinishInflate() {
        super.onFinishInflate()
        _textPadding = paddingLeft
        if (text.isNullOrEmpty())
            setPadding(0, paddingTop, paddingRight, paddingBottom)
    }

    fun setOnCheckedStateChangeListener(listener: OnCheckedStateChangeListener?) {
        onCheckedStateChangeListener = listener
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
}