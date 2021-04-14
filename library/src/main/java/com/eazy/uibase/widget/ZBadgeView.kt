package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.SizeF
import android.view.View
import androidx.annotation.StyleRes
import com.eazy.uibase.R
import com.eazy.uibase.widget.badgeview.BadgeView

class ZBadgeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BadgeView(context, attrs) {

    enum class DragState {
        START, DRAGGING, DRAGGING_OUT_OF_RANGE, CANCELED, SUCCEED
    }

    fun interface OnDragStateChangeListener {
        fun onDragStateChanged(badge: ZBadgeView, dragState: DragState)
    }

    var maximum: Int
        get() = maximumNumber
        set(value) { maximumNumber = value }

    var number: Int
        get() = badgeNumber
        set(value) { badgeNumber = value }

    var text: CharSequence
        get() = badgeText
        set(value) { badgeText = value.toString() }

    var gravity: Int
        get() = badgeGravity
        set(value) { badgeGravity = value }

    var gravityOffset: SizeF
        get() = SizeF(super.mGravityOffsetX, super.mGravityOffsetY)
        set(value) { setGravityOffset(value.width, value.height, false) }

    var dragable: Boolean
        get() = super.isDraggable()
        set(value) { super.setDraggable(value) }

    var dragRadius: Float
        get() = super.mFinalDragDistance
        set(value) { super.mFinalDragDistance = value }

    val dragState: DragState
        get() = DragState.values()[super.getDragState().ordinal]

    var borderColor: Int
        get() = super.mColorBackgroundBorder
        set(value) { super.setColorBackgroundBorder(value) }

    var borderWidth: Float
        get() = super.mBackgroundBorderWidth
        set(value) { super.setBackgroundBorderWidth(value) }

    var fillColor: Int
        get() = super.mColorBackground
        set(value) { super.setBadgeBackgroundColor(value) }

    var padding: Float
        get() = super.mBadgePadding
        set(value) { super.setBadgePadding(value, false) }

    @StyleRes
    var textAppearance = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncAppearance()
        }

    fun setOnDragStateChangeListener(listener: OnDragStateChangeListener?) {
        if (listener == null)
            super.setOnDragStateChangedListener(null)
        else
            super.setOnDragStateChangedListener() { _: BadgeView.DragState, _: BadgeView, _: View ->
                listener.onDragStateChanged(this, this.dragState)
            }
    }

    init {
        val style = if (defStyleAttr == 0) R.attr.badgeViewStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZBadgeView, style, 0)
        number = a.getInt(R.styleable.ZBadgeView_number, number)
        maximum = a.getInt(R.styleable.ZBadgeView_maximum, maximum)
        val text = a.getText(R.styleable.ZBadgeView_text)
        if (text != null)
            this.text = text
        textAppearance = a.getResourceId(R.styleable.ZBadgeView_textAppearance, 0)
        dragable = a.getBoolean(R.styleable.ZBadgeView_dragable, false)
        dragRadius = a.getDimension(R.styleable.ZBadgeView_dragRadius, 0f)
        padding = a.getDimension(R.styleable.ZBadgeView_padding, 0f)
        fillColor = a.getColor(R.styleable.ZBadgeView_fillColor, 0)
        borderColor = a.getColor(R.styleable.ZBadgeView_borderColor, 0)
        borderWidth = a.getDimension(R.styleable.ZBadgeView_borderWidth, 0f)
        a.recycle()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        syncAppearance()
    }

    companion object {
        private const val TAG = "ZBadgeView"
    }

    @SuppressLint("CustomViewStyleable")
    private fun syncAppearance() {
        if (textAppearance == 0)
            return
        val a = context.obtainStyledAttributes(textAppearance, R.styleable.TextAppearance)
        badgeTextColor = a.getColor(R.styleable.TextAppearance_android_textColor, 0)
        setBadgeTextSize(a.getDimension(R.styleable.TextAppearance_android_textSize, 0f), false)
        a.recycle()
    }

}
