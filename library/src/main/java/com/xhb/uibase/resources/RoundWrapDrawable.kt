package com.xhb.uibase.resources

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Size
import com.xhb.uibase.view.size

class RoundWrapDrawable(private val drawable: Drawable, private val size: Int = 0) : RoundDrawable() {

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        var size = bounds.size
        if (size.width == 0 || size.height == 0) {
            size = Size(intrinsicWidth, intrinsicHeight)
        }
        var size2 = drawable.bounds.size
        if (size2.width == 0 || size2.height == 0) {
            size2 = Size(drawable.intrinsicWidth, drawable.intrinsicHeight)
        }
        canvas.save()
        canvas.translate((size.width - size2.width) / 2f, (size.height - size2.height) / 2f)
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        drawable.alpha = alpha
    }

    override fun getIntrinsicWidth(): Int {
        return drawable.intrinsicWidth.coerceAtLeast(size)
    }

    override fun getIntrinsicHeight(): Int {
        return drawable.intrinsicHeight.coerceAtLeast(size)
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        borderRadius = bounds.width() / 2f
    }
}
