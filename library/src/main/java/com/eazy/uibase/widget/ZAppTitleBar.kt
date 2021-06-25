package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.TextViewCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.Drawables
import com.eazy.uibase.resources.GradientColorList
import com.eazy.uibase.resources.ViewDrawable
import com.eazy.uibase.resources.toGradient

class ZAppTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.appTitleBarStyle
) : FrameLayout(context, attrs, defStyleAttr) {

    @RawRes
    var icon: Int = 0
        set(value) {
            if (field == value) return
            field = value
            if (value == 0) {
                _imageView.visibility = View.GONE
            } else {
                _imageView.setImageDrawable(getDrawable(value))
                _imageView.visibility = View.VISIBLE
            }
        }

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
            if (value > 0) {
                TextViewCompat.setTextAppearance(_textView, value)
                _textView.setTextColor(_textView.textColors.toGradient(_textView))
            }
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

    val extensionBody get() = _content

    interface TitleBarListener {
        fun titleBarButtonClicked(bar: ZAppTitleBar, btnId: Int)
    }

    var listener: TitleBarListener? = null

    private val _imageView: ImageView
    private val _textView: TextView
    private val _leftButton: ZButton
    private val _rightButton: ZButton
    private val _rightButton2: ZButton
    private var _content: View? = null

    private var _inited = false

    init {
        LayoutInflater.from(context).inflate(R.layout.app_title_bar, this)
        GradientColorList.prepare(this)

        _leftButton = findViewById(R.id.leftButton)
        _imageView = findViewById(R.id.imageView)
        _textView = findViewById(R.id.textView)
        _rightButton = findViewById(R.id.rightButton)
        _rightButton2 = findViewById(R.id.rightButton2)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZAppTitleBar, defStyleAttr, 0)
        applyStyle(a)
        a.recycle()

        background = background.toGradient(this)

        _inited = true
        updateLayout()
    }

    fun gradientWith(view: View, length1: Int, length2: Int, horizontal: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setOnScrollChangeListener { _, scrollX, scrollY, _, _ ->
                val position = if (horizontal) scrollX else scrollY
                val progress = when {
                    position < 0 -> -1f
                    position < length1 -> -position.toFloat() / length1
                    position < (length1 + length2) -> (position - length1).toFloat() / length2
                    else -> 1f
                }
                GradientColorList.setProgress(this, progress)
            }
        }
    }

    override fun addView(child: View) {
        addView(child, child.layoutParams)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams?) {
        if (!_inited) return super.addView(child, params)
        if (_content != null) {
            throw RuntimeException("Already has a extension content!")
        }
        val lp = params as? LayoutParams
            ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER_HORIZONTAL
        val titleView = _textView.parent as LinearLayoutCompat
        addView(child, indexOfChild(titleView) + 1, lp)
        _content = child
    }

    override fun removeView(view: View) {
        super.removeView(view)
        if (view == _content)
            _content = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var l = _textView.left
        var r = (_rightButton.parent as View).left
        val padding = resources.getDimensionPixelSize(R.dimen.app_title_bar_text_padding)
        if (leftButton != 0) {
            l = _leftButton.right
            val c = left + right
            if (l + r > c)
                r = c - l
            else
                l = c - r
            l += padding
        }
        // TODO: consider icon
        val w = r - l - padding
        if (w < _textView.width)
            _textView.maxWidth = w
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateLayout()
        if (_imageView.drawable is ViewDrawable) {
            _imageView.drawable.invalidateSelf()
        }
        background = background.toGradient(this)
        _textView.setTextColor(_textView.textColors.toGradient(_textView))
    }

    /* private */

    companion object {
        private const val TAG = "ZAppTitleBar"
    }

    private fun applyStyle(a: TypedArray) {
        content = a.getResourceId(R.styleable.ZAppTitleBar_content, 0)
        leftButton = a.getResourceId(R.styleable.ZAppTitleBar_leftButton, 0)
        rightButton = a.getResourceId(R.styleable.ZAppTitleBar_rightButton, 0)
        rightButton2 = a.getResourceId(R.styleable.ZAppTitleBar_rightButton2, 0)
        icon = a.getResourceId(R.styleable.ZAppTitleBar_icon, 0)
        title = a.getText(R.styleable.ZAppTitleBar_title)
        textAppearance = a.getResourceId(R.styleable.ZAppTitleBar_textAppearance, textAppearance)
    }

    private fun syncButton(button: ZButton, content: Int) {
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
        val titleView = _textView.parent as LinearLayoutCompat
        val lp = titleView.layoutParams as LayoutParams
        val ta: Int
        val tg: Int
        if (leftButton == 0 && (rightButton != 0 || rightButton2 != 0)) {
            lp.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            tg = Gravity.START or Gravity.CENTER_VERTICAL
            ta = R.style.TextAppearance_Z_Head0
        } else {
            lp.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
            tg = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
            ta = R.style.TextAppearance_Z_Head2
        }
        titleView.layoutParams = lp
        titleView.gravity = tg
        if (textAppearance == 0)
            TextViewCompat.setTextAppearance(_textView, ta)
    }

    private fun syncContent() {
        if (_content != null) {
            removeView(_content!!)
        }
        if (content == 0)
            return
        when (resources.getResourceTypeName(content)) {
            "string" -> {
                leftButton = 0
                rightButton = 0
                rightButton2 = 0
                title = resources.getText(content)
            }
            "layout" -> {
                val view = LayoutInflater.from(context).inflate(content, this, false)
                addView(view)
            }
            "style" -> {
                leftButton = 0
                rightButton = 0
                rightButton2 = 0
                title = null
                val typedArray = context.obtainStyledAttributes(content, R.styleable.ZAppTitleBar)
                applyStyle(typedArray)
                typedArray.recycle()
            }
        }
    }

    private fun getDrawable(value: Int): Drawable? {
        return when (context.resources.getResourceTypeName(value)) {
            "drawable" -> Drawables.getDrawable(context, value)
            "layout" -> ViewDrawable(context, value)
            else -> null
        }
    }

}


