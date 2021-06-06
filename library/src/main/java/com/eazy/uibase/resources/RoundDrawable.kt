package com.eazy.uibase.resources

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.StyleRes
import com.eazy.uibase.R

open class RoundDrawable : Drawable {

    var fillColor: ColorStateList? = null

    var borderColor: ColorStateList? = null

    var borderSize = 0f

    var cornerRadius = 0f

    var cornerRadii: FloatArray? = null

    var padding: RectF? = null

    var width = 0

    var height = 0

    private val paint = Paint()
    private var alpha = 255

    constructor() {
        paint.isAntiAlias = true
    }

    constructor(context: Context, @StyleRes style: Int) : this() {
        val a = context.obtainStyledAttributes(style, R.styleable.RoundDrawable)
        fillColor = a.getColorStateList(R.styleable.RoundDrawable_fillColor)
        borderColor = a.getColorStateList(R.styleable.RoundDrawable_borderColor)
        borderSize = a.getDimension(R.styleable.RoundDrawable_borderSize, borderSize)
        cornerRadius = a.getDimension(R.styleable.RoundDrawable_cornerRadius, borderSize)
        val topLeftRadius = a.getDimension(
            R.styleable.RoundDrawable_android_topLeftRadius, cornerRadius)
        val topRightRadius = a.getDimension(
            R.styleable.RoundDrawable_android_topRightRadius, cornerRadius)
        val bottomLeftRadius = a.getDimension(
            R.styleable.RoundDrawable_android_bottomLeftRadius, cornerRadius)
        val bottomRightRadius = a.getDimension(
            R.styleable.RoundDrawable_android_bottomRightRadius, cornerRadius)
        if (topLeftRadius != cornerRadius || topRightRadius != cornerRadius
            ||bottomLeftRadius != cornerRadius || bottomRightRadius != cornerRadius) {
            cornerRadii = floatArrayOf(topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius)
        }
        width = a.getDimensionPixelSize(R.styleable.RoundDrawable_android_width, width)
        height = a.getDimensionPixelSize(R.styleable.RoundDrawable_android_height, height)
        a.recycle()
    }

    constructor(fillColor: ColorStateList, cornerRadius: Float) : this() {
        this.fillColor = fillColor
        this.cornerRadius = cornerRadius
    }

    constructor(fillColor: ColorStateList, cornerRadius: Float, borderColor: ColorStateList, borderSize: Float)
        : this(fillColor, cornerRadius)
    {
        this.borderColor = borderColor
        this.borderSize = borderSize
    }

    override fun draw(canvas: Canvas) {
        val bounds = RectF(this.bounds)
        val padding = this.padding
        if (padding != null) {
            bounds.left += padding.left
            bounds.right -= padding.right
            bounds.top += padding.top
            bounds.bottom -= padding.bottom
        }
        if (fillColor != null) {
            paint.style = Paint.Style.FILL
            paint.color = fillColor!!.getColorForState(state, 0)
            paint.alpha = modulateAlpha(alpha, paint.alpha)
            bounds.inset(borderSize, borderSize)
            cornerRadius -= borderSize
            if (cornerRadius > 0)
                canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint)
            else
                canvas.drawRect(bounds, paint)
            cornerRadius += borderSize
            bounds.inset(-borderSize, -borderSize)
        }
        if (borderSize > 0 && borderColor != null) {
            paint.style = Paint.Style.STROKE
            paint.color = borderColor?.getColorForState(state, 0) ?: 0
            paint.alpha = modulateAlpha(alpha, paint.alpha)
            paint.strokeWidth = borderSize
            bounds.inset(borderSize / 2f, borderSize / 2f)
            cornerRadius -= borderSize /2f
            when {
                cornerRadii != null -> {
                    val path = Path()
                    path.addRoundRect(bounds, cornerRadii!!, Path.Direction.CW)
                    canvas.drawPath(path, paint)
                }
                cornerRadius > 0 -> canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint)
                else -> canvas.drawRect(bounds, paint)
            }
            cornerRadius += borderSize /2f
        }
    }

    override fun getAlpha(): Int {
        return alpha
    }

    override fun setAlpha(alpha: Int) {
        this.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        val padding = this.padding ?: return width
        return width + (padding.left + padding.right).toInt()
    }

    override fun getIntrinsicHeight(): Int {
        val padding = this.padding ?: return height
        return height + (padding.top + padding.bottom).toInt()
    }

    override fun isStateful(): Boolean {
        return (fillColor != null && fillColor!!.isStateful)
            || (borderColor != null && borderColor!!.isStateful)
    }

    override fun onStateChange(state: IntArray?): Boolean {
        return true
    }

    private fun modulateAlpha(paintAlpha: Int, alpha: Int): Int {
        val scale = alpha + (alpha ushr 7) // convert to 0..256
        return paintAlpha * scale ushr 8
    }
}
