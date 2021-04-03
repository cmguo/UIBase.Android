package com.eazy.uibase.widget

import android.content.Context
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
        }

    var maximum = 0
        set(value) {
            if (field == value || value < minimum) return
            field = value
            if (value in 1 until amount)
                amount = value
        }

    var amount = 0 //数量
        set(value) {
            if (field == value || (maximum in 1 until value) || value < minimum)
                return
            field = value
            if (!inCallbacks)
                _editText.setText(value.toString())
            _buttonDec.isEnabled = value > minimum
            _buttonInc.isEnabled = (maximum == 0) || (value < maximum)
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

    override fun onClick(v: View) {
        val i = v.id
        _editText.clearFocus()
        if (i == R.id.buttonDec) {
            if (amount > 0) {
                amount--
            }
        } else if (i == R.id.buttonInc) {
            if (maximum == 0 || amount < maximum) {
                amount++
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

    init {
        LayoutInflater.from(context).inflate(R.layout.number_view, this)
        _editText = findViewById<View>(R.id.editText) as EditText
        _buttonDec = findViewById<View>(R.id.buttonDec) as Button
        _buttonInc = findViewById<View>(R.id.buttonInc) as Button
        _buttonDec.setOnClickListener(this)
        _buttonInc.setOnClickListener(this)
        _editText.addTextChangedListener(this)

        background = RoundDrawable(context, R.style.ZNumberView_Background)
        val buttonBackground = RoundDrawable(context, R.style.ZNumberView_ButtonBackground)
        val buttonForegroundDec = ContextCompat.getDrawable(context, R.drawable.icon_minus)
        val buttonForegroundInc = ContextCompat.getDrawable(context, R.drawable.icon_plus)
        val buttonForegroundColor = ContextCompat.getColorStateList(context, R.color.number_view_button_foreground_color)
        buttonForegroundDec!!.setTintList(buttonForegroundColor)
        buttonForegroundInc!!.setTintList(buttonForegroundColor)
        val drawableDec = LayerDrawable(arrayOf(buttonBackground, buttonForegroundDec))
        val drawableInc = LayerDrawable(arrayOf(buttonBackground, buttonForegroundInc))
        val buttonPadding = context.resources.getDimensionPixelSize(R.dimen.number_view_button_padding)
        drawableDec.setLayerInset(1, buttonPadding, buttonPadding, buttonPadding, buttonPadding)
        drawableInc.setLayerInset(1, buttonPadding, buttonPadding, buttonPadding, buttonPadding)
        _buttonDec.background = drawableDec
        _buttonInc.background = drawableInc
    }

    companion object {
        private const val TAG = "ZNumberView"
    }

}