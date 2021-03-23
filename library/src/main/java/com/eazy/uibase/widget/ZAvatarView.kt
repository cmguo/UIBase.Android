package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.eazy.uibase.R
import java.lang.reflect.Method

class ZAvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, if (defStyleAttr == 0) R.attr.avatarViewStyle else defStyleAttr) {

    enum class ClipType {
        None,
        Circle,
        Ellipse,
    }

    enum class ClipRegion {
        WholeView,
        Drawable
    }

    var clipType = ClipType.Circle
        set(value) {
            if (field != value) {
                field = value
                computeRoundBounds()
                invalidate()
            }
        }

    var clipRegion = ClipRegion.Drawable
        set(value) {
            if (field != value) {
                field = value
                computeRoundBounds()
                invalidate()
            }
        }

    var borderColor = 0
        set(value) {
            if (field != value) {
                field = value
                borderPaint.color = value
                invalidate()
            }
        }

    var borderWidth = 0f
        set(value) {
            if (field != value) {
                field = value
                borderPaint.strokeWidth = value
                invalidate()
            }
        }

    private var xferPaint: Paint = Paint()
    private var borderPaint: Paint = Paint()
    private val circleBounds = RectF()
    private val clipBounds = RectF()

    private var dstImage = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var srcImage = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var setXfermode: Method? = null

    init {
        xferPaint.isAntiAlias = true
        xferPaint.color = Color.WHITE
        xferPaint.style = Paint.Style.FILL
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZAvatarView, R.attr.avatarViewStyle, 0)
        val cr = a.getInt(R.styleable.ZAvatarView_clipRegion, -1)
        if (cr >= 0) clipRegion = ClipRegion.values()[cr]
        val ct = a.getInt(R.styleable.ZAvatarView_clipType, -1)
        if (ct >= 0) clipType = ClipType.values()[ct]
        borderWidth = a.getDimensionPixelSize(R.styleable.ZAvatarView_borderWidth, borderWidth.toInt()).toFloat()
        borderColor = a.getColor(R.styleable.ZAvatarView_borderColor, borderColor)
        a.recycle()
    }


    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (xfermodeClass != null) {
            setXfermode = try { drawable?.javaClass?.getMethod("setXfermode", xfermodeClass) } catch (e: Throwable) { null }
            setXfermode?.isAccessible = true
        }
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val result = super.setFrame(l, t, r, b)
        computeRoundBounds()
        if (width != dstImage.width || height != dstImage.height) {
            dstImage.recycle()
            dstImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            dstImage.eraseColor(0)
            if (setXfermode == null) {
                srcImage.recycle()
                srcImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                srcImage.eraseColor(0)
            }
        }
        return result
    }

    override fun invalidate() {
        super.invalidate()
        if (dstImage != null)
            dstImage.eraseColor(0)
        if (srcImage != null)
            srcImage.eraseColor(0)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (clipType == ClipType.None) {
            super.onDraw(canvas)
            return
        }
        if (drawable == null && clipRegion == ClipRegion.Drawable) {
            super.onDraw(canvas)
            return
        }
        val dstCanvas = Canvas(dstImage)
        dstCanvas.clipRect(clipBounds)
        // prepare mask
        xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        dstCanvas.drawOval(circleBounds, xferPaint)
        // take photo of src
        if (setXfermode != null) {
            setXfermode!!.invoke(drawable, PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
            super.onDraw(canvas)
            setXfermode!!.invoke(drawable, PorterDuffXfermode(PorterDuff.Mode.SRC_OVER))
        } else {
            val srcCanvas = Canvas(srcImage)
            super.onDraw(srcCanvas) // SRC_OVER
            // paint photo on mask
            xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            dstCanvas.drawBitmap(srcImage, 0f, 0f, xferPaint)
        }
        // border
        if (borderWidth > 0) {
            dstCanvas.drawOval(circleBounds, borderPaint)
        }
        canvas.drawBitmap(dstImage, 0f, 0f, borderPaint)
    }


    private fun computeRoundBounds() {
        if (clipType == ClipType.None)
            return
        val viewBounds = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val imageBounds = RectF(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
        imageMatrix.mapRect(imageBounds)
        if (clipRegion == ClipRegion.WholeView) {
            circleBounds.set(viewBounds)
        } else {
            circleBounds.set(imageBounds)
        }
        if (clipType == ClipType.Circle) {
            if (circleBounds.width() > circleBounds.height()) {
                val c = circleBounds.centerX()
                circleBounds.left = c - circleBounds.height() / 2
                circleBounds.right = c + circleBounds.height() / 2
            } else {
                val c = circleBounds.centerY()
                circleBounds.top = c - circleBounds.width() / 2
                circleBounds.bottom = c + circleBounds.width() / 2
            }
        }
        clipBounds.set(imageBounds)
    }

    companion object {
        val xfermodeClass: Class<*>? = try {
            Class.forName("android.graphics.Xfermode");
        } catch (e: Throwable) {
            null
        }
    }

}


