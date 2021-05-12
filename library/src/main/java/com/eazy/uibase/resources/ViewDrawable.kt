package com.eazy.uibase.resources

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes

class ViewDrawable(private val view: View) : Drawable() {

    constructor(context: Context, @LayoutRes id: Int)
        : this(LayoutInflater.from(context).inflate(id, FrameLayout(context), false)) {
    }

    private val holder = ViewHolder(this, view)

    init {
        holder.measure(0, 0)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    fun getView() : View {
        return holder.getChildAt(0)
    }

    override fun draw(canvas: Canvas) {
        view.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        view.alpha = alpha / 255f
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("Not yet implemented")
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        return view.measuredWidth
    }

    override fun getIntrinsicHeight(): Int {
        return view.measuredHeight
    }

    class ViewHolder(private val drawable: ViewDrawable, private val view: View) : FrameLayout(view.context) {

        init {
            addView(view)
        }

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        }

        override fun requestLayout() {
            super.requestLayout()
            if (view == null)
                return
            measure(0, 0)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            drawable.invalidateSelf()
        }

    }

}
