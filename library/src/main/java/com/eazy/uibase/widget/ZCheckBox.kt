package com.eazy.uibase.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatCheckBox
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
        fun onCheckedStateChanged(buttonView: ZCheckBox?, state: CheckedState)
    }

    private var half_checked_: Boolean = false
    private var text_padding_ : Int? = null
    private var onCheckedStateChangeListener: OnCheckedStateChangeListener? = null

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

    override fun onFinishInflate() {
        super.onFinishInflate()
        text_padding_ = paddingLeft
        if (text.isNullOrEmpty())
            setPadding(0, paddingTop, paddingRight, paddingBottom)
    }

    fun setOnCheckedStateChangeListener(listener: OnCheckedStateChangeListener?) {
        onCheckedStateChangeListener = listener
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (text_padding_ != null)
            setPadding(if (text.isNullOrEmpty()) 0 else text_padding_!!, paddingTop, paddingRight, paddingBottom)
    }

    override fun setChecked(checked: Boolean) {
        half_checked_ = false
        super.setChecked(checked)
        onCheckedStateChangeListener?.onCheckedStateChanged(this, checkedState)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (half_checked_)
            mergeDrawableStates(drawableState, intArrayOf(R.attr.state_half_checked))
        return drawableState
    }
}