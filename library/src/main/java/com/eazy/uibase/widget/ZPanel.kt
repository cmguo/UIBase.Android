package com.eazy.uibase.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.eazy.uibase.R
import com.eazy.uibase.dialog.MaskDialog

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

    val body get() = _content

    fun interface PanelListener {
        fun panelButtonClicked(panel: ZPanel, btnId: Int) {}
        fun panelDismissed(panel: ZPanel)
    }

    var listener: PanelListener? = null

    private var _titleBar: ZAppTitleBar
    private var _bottomButton: ZButton
    private var _content: View? = null

    private var _inited = false

    init {
        LayoutInflater.from(context).inflate(R.layout.panel, this)
        _titleBar = findViewById(R.id.titleBar)
        _bottomButton = findViewById(R.id.bottomButton)

        _titleBar.listener = object : ZAppTitleBar.TitleBarListener {
            override fun titleBarButtonClicked(bar: ZAppTitleBar, btnId: Int) {
                listener?.panelButtonClicked(this@ZPanel, btnId)
            }
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZPanel, R.attr.panelStyle, 0)
        applyStyle(a)
        a.recycle()

        _inited = true
    }

    fun popUp(fragmentManager: FragmentManager) {
        val lp = layoutParams as? FrameLayout.LayoutParams ?: FrameLayout.LayoutParams(0, 0)
        lp.width = LayoutParams.MATCH_PARENT
        lp.height = LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        layoutParams = lp
        MaskDialog(this).show(fragmentManager, "")
    }

    fun dismiss() {
        MaskDialog.dismiss(this)
    }

    override fun addView(child: View) {
        addView(child, child.layoutParams)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams?) {
        if (!_inited)
            return super.addView(child, params)
        if (_content != null) {
            throw RuntimeException("Already has a body!")
        }
        val lp = params as? LayoutParams ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER_HORIZONTAL
        lp.weight=1f
        addView(child, indexOfChild(_titleBar.parent as View) + 1, lp)
        _content = child
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener?.panelDismissed(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        syncBackground()
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
            button.setOnClickListener {
                listener?.panelButtonClicked(this, it.id)
            }
        }
    }

    private fun syncContent() {
        if (_content != null) {
            removeView(_content)
            _content = null
        }
        if (content == 0)
            return
        val type = resources.getResourceTypeName(content)
        if (type == "layout") {
            val view = LayoutInflater.from(context).inflate(content, this, false)
            addView(view)
        } else if (type == "style") {
            val typedArray = context.obtainStyledAttributes(content, R.styleable.ZPanel)
            applyStyle(typedArray)
            typedArray.recycle()
        }
    }

    private fun syncBackground() {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.color = ContextCompat.getColorStateList(context, R.color.bluegrey_00)
        drawable.cornerRadii = floatArrayOf(borderRadius, borderRadius, borderRadius, borderRadius, 0f, 0f, 0f, 0f)
        background = drawable
    }

}


