package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.RoundDrawable
import com.eazy.uibase.view.findViewByType

class ZSearchBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.searchBoxStyle
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    @DrawableRes
    var rightButton: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncButton(_rightButton, value)
        }

    var text
        get() = _textView?.text
        set(value) {
            _textView?.text = value
        }

    interface SearchBoxListener {
        fun searchBoxFocused(bar: ZSearchBox) {}
        fun searchBoxTextChanged(bar: ZSearchBox, text: String) {}
        fun searchBoxButtonClicked(bar: ZSearchBox, btnId: Int) {}
    }

    var listener: SearchBoxListener? = null

    private val _inputContainer: ViewGroup
    private var _textView: EditText? = null
    private var _rightButton: ZButton

    init {
        LayoutInflater.from(context).inflate(R.layout.search_box, this)
        _inputContainer = findViewById(R.id.inputContainer)
        _rightButton = findViewById(R.id.rightButton)

        _inputContainer.background = RoundDrawable(context, R.style.RoundDrawable_ZSearchBox_InputBackground)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZSearchBox, defStyleAttr, 0)
        readStyle(a)
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
                listener?.searchBoxTextChanged(this@ZSearchBox, s.toString())
            }
        })
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                listener?.searchBoxFocused(this@ZSearchBox)
        }
        _textView = editText
        child.background = null
        _inputContainer.addView(child)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        _inputContainer.background = RoundDrawable(context, R.style.RoundDrawable_ZSearchBox_InputBackground)
    }

    /* private */

    companion object {
        private const val TAG = "ZSearchBox"
    }

    private fun readStyle(a: TypedArray) {
        rightButton = a.getResourceId(R.styleable.ZSearchBox_rightButton, 0)
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
                listener?.searchBoxButtonClicked(this, it.id)
            }
        }
    }
}


