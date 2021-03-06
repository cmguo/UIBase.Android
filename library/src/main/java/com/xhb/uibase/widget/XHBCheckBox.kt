package com.xhb.uibase.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.xhb.uibase.R
import com.xhb.uibase.resources.ShapeDrawables

class XHBCheckBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatCheckBox(context, attrs, defStyleAttr) {

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

    private var half_checked_: Boolean = false

    init {
        val background = ShapeDrawables.getDrawable(context, backgroundDrawable)
        val foreground = resources.getDrawable(R.drawable.check_box_foreground)
        buttonDrawable = LayerDrawable(arrayOf(background, foreground))
    }

    var checkedState: CheckedState
        get() {
            return if (isChecked()) CheckedState.FullChecked
            else if (half_checked_) CheckedState.HalfChecked
            else CheckedState.NotChecked
        }
        set(value) {
            isChecked = value == CheckedState.FullChecked
            half_checked_ = value == CheckedState.HalfChecked
            refreshDrawableState()
        }

    override fun setChecked(checked: Boolean) {
        half_checked_ = false
        super.setChecked(checked)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (half_checked_)
            mergeDrawableStates(drawableState, intArrayOf(R.attr.state_half_checked))
        return drawableState
    }
}