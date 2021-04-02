package com.eazy.uibase.resources

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

class ViewDrawable(private val view: View) : Drawable() {

    constructor(context: Context, @LayoutRes id: Int)
        : this(LayoutInflater.from(context).inflate(id, null)) {
    }

    private val holder = ViewHolder(this, view)

    init {
        view.measure(0, 0)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
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

    class ViewHolder(private val drawable: ViewDrawable, private val view: View) : ViewGroup(view.context) {

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        }

        override fun requestLayout() {
            super.requestLayout()
            view.measure(0, 0)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            drawable.invalidateSelf()
        }

    }

}
