package com.xhb.uibase.demo.view

import android.graphics.*
import android.graphics.drawable.Drawable

class GridDrawable : Drawable() {

    var gridOn = false
        set(value) {
            if (field == value) return
            field = value
            invalidateSelf()
        }

    var backgroundColor: Int = 0
        get() = paintBack.color
        set(value) {
            if (field == value) return
            field = value
            paintBack.color = value
            invalidateSelf()
        }

    private val paintBack = Paint()
    private val paintGrid = Paint()

    init {
        paintBack.style = Paint.Style.FILL
        paintBack.color = 0
        paintGrid.style = Paint.Style.STROKE
        paintGrid.color = Color.LTGRAY
        paintGrid.strokeWidth = 1f
    }

    override fun draw(canvas: Canvas) {
        val bounds = RectF(bounds)
        if (paintBack.color != 0)
            canvas.drawRect(bounds, paintBack)
        if (!gridOn)
            return
        val top = bounds.top
        bounds.top += 32
        while (bounds.top < bounds.bottom) {
            canvas.drawLine(bounds.left, bounds.top, bounds.right, bounds.top, paintGrid)
            bounds.top += 32
        }
        bounds.top = top
        bounds.left += 32
        while (bounds.left < bounds.right) {
            canvas.drawLine(bounds.left, bounds.top, bounds.left, bounds.bottom, paintGrid)
            bounds.left += 32
        }
    }

    override fun setAlpha(alpha: Int) {
        TODO("Not yet implemented")
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("Not yet implemented")
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}