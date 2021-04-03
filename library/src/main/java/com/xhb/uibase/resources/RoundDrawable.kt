package com.xhb.uibase.resources

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.StyleRes
import com.xhb.uibase.R

open class RoundDrawable : Drawable {

    var fillColor: ColorStateList? = null

    var borderColor: ColorStateList? = null

    var borderSize = 0f

    var borderRadius = 0f

    var width = 0

    var height = 0

    constructor()

    constructor(context: Context, @StyleRes style: Int) {
        val a = context.obtainStyledAttributes(style, R.styleable.RoundDrawable)
        fillColor = a.getColorStateList(R.styleable.RoundDrawable_fillColor)
        borderColor = a.getColorStateList(R.styleable.RoundDrawable_borderColor)
        borderSize = a.getDimension(R.styleable.RoundDrawable_borderSize, borderSize)
        borderRadius = a.getDimension(R.styleable.RoundDrawable_borderRadius, borderRadius)
        width = a.getDimensionPixelSize(R.styleable.RoundDrawable_android_width, width)
        height = a.getDimensionPixelSize(R.styleable.RoundDrawable_android_height, height)
        a.recycle()
    }

    constructor(fillColor: ColorStateList, borderRadius: Float) {
        this.fillColor = fillColor
        this.borderRadius = borderRadius
    }

    constructor(fillColor: ColorStateList, borderRadius: Float, borderColor: ColorStateList, borderSize: Float)
        : this(fillColor, borderRadius)
    {
        this.borderColor = borderColor
        this.borderSize = borderSize
    }

    private val paint = Paint()

    override fun draw(canvas: Canvas) {
        val bounds = RectF(this.bounds)
        if (fillColor != null) {
            paint.style = Paint.Style.FILL
            paint.color = fillColor!!.getColorForState(state, 0)
            bounds.inset(borderSize, borderSize)
            borderRadius -= borderSize
            if (borderRadius > 0)
                canvas.drawRoundRect(bounds, borderRadius, borderRadius, paint)
            else
                canvas.drawRect(bounds, paint)
            borderRadius += borderSize
            bounds.inset(-borderSize, -borderSize)
        }
        if (borderSize > 0 && borderColor != null) {
            paint.style = Paint.Style.STROKE
            paint.color = borderColor?.getColorForState(state, 0) ?: 0
            paint.strokeWidth = borderSize
            bounds.inset(borderSize / 2f, borderSize / 2f)
            borderRadius -= borderSize /2f
            if (borderRadius > 0)
                canvas.drawRoundRect(bounds, borderRadius, borderRadius, paint)
            else
                canvas.drawRect(bounds, paint)
            borderRadius += borderSize /2f
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        return width
    }

    override fun getIntrinsicHeight(): Int {
        return height
    }

    override fun isStateful(): Boolean {
        return (fillColor != null && fillColor!!.isStateful)
            || (borderColor != null && borderColor!!.isStateful)
    }

    override fun onStateChange(state: IntArray?): Boolean {
        return true
    }

}
