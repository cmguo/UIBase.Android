package com.eazy.uibase.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.eazy.uibase.R

class ZPanel @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, if (defStyleAttr == 0) R.attr.panelStyle else defStyleAttr) {

    var titleBar: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncTitleBar()
        }

    @DrawableRes
    var bottomButton: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncButton(_bottomButton, value)
        }

    var content: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncContent()
        }

    var borderRadius = 0f
        set(value) {
            field = value
            syncBackground()
        }

    @FunctionalInterface
    interface PanelListener {
        fun panelButtonClicked(panel: ZPanel, viewId: Int) {}
        fun panelDismissed(panel: ZPanel)
    }

    var listener: PanelListener? = null

    private var _titleBar: ZAppTitleBar
    private var _bottomButton: ZButton
    private var _content: View? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.panel, this)
        _titleBar = findViewById(R.id.titleBar)
        _bottomButton = findViewById(R.id.bottomButton)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZPanel, R.attr.panelStyle, 0)
        applyStyle(a)
        a.recycle()
    }

    fun popUp() {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener?.panelDismissed(this)
    }

    companion object {
        private const val TAG = "ZPanel"
    }

    /* private */

    private fun applyStyle(a: TypedArray) {
        titleBar = a.getResourceId(R.styleable.ZPanel_titleBar, 0)
        bottomButton = a.getResourceId(R.styleable.ZPanel_bottomButton, 0)
        content = a.getResourceId(R.styleable.ZPanel_content, 0)
        borderRadius = a.getDimension(R.styleable.ZPanel_borderRadius, borderRadius)
    }

    private fun syncTitleBar() {
        val parent = _titleBar.parent as View
        if (titleBar == 0) {
            parent.visibility = GONE
            return
        }
        _titleBar.content = titleBar
        parent.visibility = VISIBLE
    }

    private fun syncButton(button: ZButton, content: Int) {
        val parent = button.parent as View
        val parent2 = parent.parent as View
        if (content == 0) {
            parent2.visibility = GONE
        } else {
            button.content = content
            parent2.visibility = VISIBLE
        }
    }

    private fun syncContent() {
        if (_content != null)
            removeView(_content)
        if (content == 0)
            return
        val type = resources.getResourceTypeName(content)
        if (type == "layout") {
            val view = LayoutInflater.from(context).inflate(content, this, false)
            val lp = view.layoutParams as LayoutParams
            lp.gravity = Gravity.CENTER_HORIZONTAL
            lp.weight=1f
            addView(view, indexOfChild(_titleBar.parent as View) + 1, lp)
            _content = view
        } else if (type == "style") {
            val typedArray = context.obtainStyledAttributes(content, R.styleable.ZPanel)
            applyStyle(typedArray)
            typedArray.recycle()
        }
    }

    private fun syncBackground() {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.color = ColorStateList.valueOf(Color.WHITE)
        drawable.cornerRadii = floatArrayOf(borderRadius, borderRadius, borderRadius, borderRadius, 0f, 0f, 0f, 0f)
        background = drawable
    }

}


