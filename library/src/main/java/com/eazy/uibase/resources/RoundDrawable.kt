package com.eazy.uibase.resources

import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable

open class RoundDrawable(private val size: Int = 0) : Drawable() {

    var fillColor: ColorStateList? = null

    var borderColor: ColorStateList? = null

    var borderSize = 0f

    private val paint = Paint()

    init {
    }

    override fun draw(canvas: Canvas) {
        val bounds = RectF(this.bounds)
        val rx = bounds.width() / 2
        val ry = bounds.height() / 2
        if (fillColor != null) {
            paint.style = Paint.Style.FILL
            paint.color = fillColor!!.getColorForState(state, 0)
            canvas.drawRoundRect(bounds, rx, ry, paint)
        }
        if (borderSize > 0) {
            paint.style = Paint.Style.STROKE
            paint.color = borderColor?.getColorForState(state, 0) ?: 0
            paint.strokeWidth = borderSize
            bounds.inset(borderSize / 2f, borderSize / 2f)
            canvas.drawRoundRect(bounds, rx, ry, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("Not yet implemented")
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        return size
    }

    override fun getIntrinsicHeight(): Int {
        return size
    }

}
