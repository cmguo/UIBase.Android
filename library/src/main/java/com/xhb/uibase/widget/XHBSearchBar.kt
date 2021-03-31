package com.xhb.uibase.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.xhb.uibase.R
import com.xhb.uibase.resources.ShapeDrawables
import com.xhb.uibase.view.findViewByType

class XHBSearchBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, if (defStyleAttr == 0) R.attr.searchBoxStyle else defStyleAttr) {

    @DrawableRes
    var rightButton: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncButton(_rightButton, value)
        }

    @FunctionalInterface
    interface SearchBoxListener {
        fun searchBoxFocused(bar: XHBSearchBox) {}
        fun searchBoxTextChanged(bar: XHBSearchBox, text: String) {}
        fun searchBoxButtonClicked(bar: XHBSearchBox, btnId: Int) {}
    }

    var listener: SearchBoxListener? = null

    private val _inputContainer: ViewGroup
    private var _textView: EditText? = null
    private var _rightButton: XHBButton

    init {
        LayoutInflater.from(context).inflate(R.layout.search_box, this)
        _inputContainer = findViewById(R.id.inputContainer)
        _rightButton = findViewById(R.id.rightButton)

        _inputContainer.background = ShapeDrawables.getDrawable(context, inputBackground)

        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBSearchBox, R.attr.searchBoxStyle, 0)
        applyStyle(a)
        a.recycle()
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        val editText = child.findViewByType(EditText::class.java) ?: return super.addView(child, params)
        if (_textView != null) {
            throw RuntimeException("Already has a edit text!")
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                listener?.searchBoxTextChanged(this@XHBSearchBox, s.toString())
            }
        })
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                listener?.searchBoxFocused(this@XHBSearchBox)
        }
        _textView = editText
        child.background = null
        _inputContainer.addView(child)
    }

    companion object {
        private const val TAG = "XHBSearchBox"

        private val inputBackground = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
            R.dimen.xhb_search_box_input_border_radius, R.color.xhb_search_box_input_background_color,
            0, 0,
            0, 0
        )
    }

    /* private */

    private fun applyStyle(a: TypedArray) {
        rightButton = a.getResourceId(R.styleable.XHBSearchBox_rightButton, 0)
    }

    private fun syncButton(button: XHBButton, content: Int) {
        val parent = button.parent as View
        val parent2 = parent.parent as View
        if (content == 0) {
            parent2.visibility = GONE
        } else {
            button.content = content
            parent2.visibility = VISIBLE
            button.setOnClickListener {
                listener?.searchBoxButtonClicked(this, it.id)
            }
        }
    }
}


