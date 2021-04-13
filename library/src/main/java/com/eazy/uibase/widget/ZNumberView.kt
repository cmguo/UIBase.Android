package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.RoundDrawable

class ZNumberView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs, R.attr.numberViewStyle), View.OnClickListener, TextWatcher {

    @FunctionalInterface
    interface OnAmountChangeListener {
        fun onAmountChanged(view: View, amount: Int)
    }

    var minimum = 0
        set(value) {
            if (field == value || value < 0 || value > maximum) return
            field = value
            if (amount < value)
                amount = value
            else
                _buttonDec.isEnabled = amount >= value + step
        }

    var maximum = 0
        set(value) {
            if (field == value || value < minimum) return
            field = value
            if (value in 1 until amount)
                amount = value
            else
                _buttonInc.isEnabled = (value == 0) || (amount + step <= value)
        }

    var step = 1
        set(value) {
            field = value
            _buttonDec.isEnabled = amount >= minimum + value
            _buttonInc.isEnabled = (maximum == 0) || (amount + value <= maximum)
        }

    var amount = 0 //数量
        set(value) {
            if (field == value || (maximum in 1 until value) || value < minimum)
                return
            field = value
            if (!inCallbacks)
                _editText.setText(value.toString())
            _buttonDec.isEnabled = value >= minimum + step
            _buttonInc.isEnabled = (maximum == 0) || (value + step <= maximum)
            mListener?.onAmountChanged(this, value)
        }

    private var mListener: OnAmountChangeListener? = null
    private val _editText: EditText
    private val _buttonDec: Button
    private val _buttonInc: Button
    private var inCallbacks = false

    fun setOnAmountChangeListener(listener: OnAmountChangeListener?) {
        mListener = listener
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.number_view, this)
        _editText = findViewById<View>(R.id.editText) as EditText
        _buttonDec = findViewById<View>(R.id.buttonDec) as Button
        _buttonInc = findViewById<View>(R.id.buttonInc) as Button
        _buttonDec.setOnClickListener(this)
        _buttonInc.setOnClickListener(this)
        _editText.addTextChangedListener(this)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZNumberView, R.attr.numberViewStyle, 0)
        minimum = a.getInt(R.styleable.ZNumberView_minimum, minimum)
        maximum = a.getInt(R.styleable.ZNumberView_maximum, maximum)
        step = a.getInt(R.styleable.ZNumberView_step, step)
        amount = a.getInt(R.styleable.ZNumberView_amount, amount)
        a.recycle()
        syncButtonBackground()
    }

    override fun onClick(v: View) {
        val i = v.id
        _editText.clearFocus()
        if (i == R.id.buttonDec) {
            if (amount >= minimum + step) {
                amount -= step
            }
        } else if (i == R.id.buttonInc) {
            if (maximum == 0 || amount + step <= maximum) {
                amount += step
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        if (s.toString().isEmpty()) return
        val value = Integer.valueOf(s.toString())
        inCallbacks = true
        amount = value
        inCallbacks = false
        if (amount != value) {
            s.replace(0, s.length, amount.toString())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        syncButtonBackground()
    }

    companion object {
        private const val TAG = "ZNumberView"
    }

    private fun syncButtonBackground() {
        background = RoundDrawable(context, R.style.ZNumberView_Background)
        _buttonDec.background = createButtonDrawable(R.drawable.icon_minus)
        _buttonInc.background = createButtonDrawable(R.drawable.icon_plus)
    }

    fun createButtonDrawable(foregroundId: Int): Drawable {
        val buttonBackground = RoundDrawable(context, R.style.ZNumberView_ButtonBackground)
        val buttonForeground = ContextCompat.getDrawable(context, foregroundId)
        val buttonForegroundColor = ContextCompat.getColorStateList(context, R.color.number_view_button_foreground_color)
        buttonForeground!!.setTintList(buttonForegroundColor)
        val buttonDrawable = LayerDrawable(arrayOf(buttonBackground, buttonForeground))
        val buttonPadding = context.resources.getDimensionPixelSize(R.dimen.number_view_button_padding)
        buttonDrawable.setLayerInset(1, buttonPadding, buttonPadding, buttonPadding, buttonPadding)
        return buttonDrawable
    }

}