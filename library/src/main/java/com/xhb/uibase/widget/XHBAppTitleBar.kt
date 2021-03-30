package com.xhb.uibase.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.widget.TextViewCompat
import com.xhb.uibase.R

class XHBAppTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, if (defStyleAttr == 0) R.attr.appTitleBarStyle else defStyleAttr) {

    var title: CharSequence?
        get() = _textView.text
        set(value) {
            _textView.text = value
        }

    var textAppearance: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            if (value > 0)
                TextViewCompat.setTextAppearance(_textView, value)
            else if (_inited)
                updateLayout()
        }

    @DrawableRes
    var leftButton: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncButton(_leftButton, value)
        }

    @DrawableRes
    var rightButton: Int = 0
        set(value) {
            if (field == value) return
            field = value
            syncButton(_rightButton, value)
        }

    @DrawableRes
    var rightButton2: Int = 0
        set(value) {
            if (field == value) return
            field = value
            syncButton(_rightButton2, value)
        }

    var content: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncContent()
        }

    @FunctionalInterface
    interface TitleBarListener {
        fun titleBarButtonClicked(bar: XHBAppTitleBar, viewId: Int)
    }

    var listener: TitleBarListener? = null

    private var _textView: TextView
    private var _leftButton: XHBButton
    private var _rightButton: XHBButton
    private var _rightButton2: XHBButton
    private var _content: View? = null

    private var _inited = false

    init {
        LayoutInflater.from(context).inflate(R.layout.app_title_bar, this)
        _leftButton = findViewById(R.id.leftButton)
        _textView = findViewById(R.id.textView)
        _rightButton = findViewById(R.id.rightButton)
        _rightButton2 = findViewById(R.id.rightButton2)

        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBAppTitleBar, R.attr.appTitleBarStyle, 0)
        applyStyle(a)
        a.recycle()

        _inited = true
        updateLayout()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var l = _textView.left
        var r = (_rightButton.parent as View).left
        val padding = resources.getDimensionPixelSize(R.dimen.xhb_app_title_bar_text_padding)
        if (leftButton != 0) {
            l = _leftButton.right
            val c = left + right
            if (l + r > c)
                r = c - l
            else
                l = c - r
            l += padding
        }
        val w = r - l - padding
        if (w < _textView.width)
            _textView.maxWidth = w
    }

    companion object {
        private const val TAG = "XHBAppTitleBar"
    }

    /* private */

    private fun applyStyle(a: TypedArray) {
        leftButton = a.getResourceId(R.styleable.XHBAppTitleBar_leftButton, 0)
        rightButton = a.getResourceId(R.styleable.XHBAppTitleBar_rightButton, 0)
        rightButton2 = a.getResourceId(R.styleable.XHBAppTitleBar_rightButton2, 0)
        title = a.getText(R.styleable.XHBAppTitleBar_title)
        textAppearance = a.getResourceId(R.styleable.XHBAppTitleBar_textAppearance, 0)
    }

    private fun syncButton(button: XHBButton, content: Int) {
        if (content == 0) {
            button.visibility = GONE
        } else {
            button.content = content
            button.visibility = VISIBLE
            button.setOnClickListener {
                listener?.titleBarButtonClicked(this, it.id)
            }
        }
        if (_inited) {
            if (_textView.maxWidth < width)
                _textView.maxWidth = width
            updateLayout()
        }
    }

    private fun updateLayout() {
        val lp = _textView.layoutParams as LayoutParams
        var ta: Int
        var tg = Gravity.START or Gravity.CENTER_VERTICAL
        if (leftButton == 0 && (rightButton != 0 || rightButton2 != 0)) {
            lp.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            ta = R.style.TextAppearance_XHB_Head0
        } else {
            lp.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
            ta = R.style.TextAppearance_XHB_Head2
            tg = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        }
        if (textAppearance > 0)
            ta = textAppearance
        _textView.layoutParams = lp
        _textView.gravity = tg
        TextViewCompat.setTextAppearance(_textView, ta)
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
            addView(view, indexOfChild(_textView) + 1, lp)
            _content = view
        } else if (type == "style") {
            val typedArray = context.obtainStyledAttributes(content, R.styleable.XHBAppTitleBar)
            applyStyle(typedArray)
            typedArray.recycle()
        }
    }

}


