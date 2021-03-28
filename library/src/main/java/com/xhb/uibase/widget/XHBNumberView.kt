package com.xhb.uibase.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
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
import com.xhb.uibase.R
import com.xhb.uibase.resources.ShapeDrawables

class XHBNumberView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null)
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
                etAmount.setText(value.toString())
            btnDecrease.isEnabled = value > minimum
            btnIncrease.isEnabled = (maximum == 0) || (value < maximum)
            mListener?.onAmountChanged(this, value)
        }

    private var mListener: OnAmountChangeListener? = null
    private val etAmount: EditText
    private val btnDecrease: Button
    private val btnIncrease: Button
    private var inCallbacks = false

    fun setOnAmountChangeListener(listener: OnAmountChangeListener?) {
        mListener = listener
    }

    override fun onClick(v: View) {
        val i = v.id
        etAmount.clearFocus()
        if (i == R.id.btnDecrease) {
            if (amount > 0) {
                amount--
            }
        } else if (i == R.id.btnIncrease) {
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

    companion object {

        private const val TAG = "XHBAmountView"

        val backgroundDrawable = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
                R.dimen.number_view_radius, R.color.number_view_background_color,
                0, 0,
                0, 0
        )

        val buttonBackgroundDrawable = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
                R.dimen.number_view_button_radius, R.color.number_view_button_background_color,
                0, 0,
                0, 0
        )

    }

    init {
        LayoutInflater.from(context).inflate(R.layout.number_view, this)
        etAmount = findViewById<View>(R.id.etAmount) as EditText
        btnDecrease = findViewById<View>(R.id.btnDecrease) as Button
        btnIncrease = findViewById<View>(R.id.btnIncrease) as Button
        btnDecrease.setOnClickListener(this)
        btnIncrease.setOnClickListener(this)
        etAmount.addTextChangedListener(this)

        background = ShapeDrawables.getDrawable(context!!, backgroundDrawable)
        val buttonBackground = ShapeDrawables.getDrawable(context, buttonBackgroundDrawable)
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
        btnDecrease.background = drawableDec
        btnIncrease.background = drawableInc
    }
}