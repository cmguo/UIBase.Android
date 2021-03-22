package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

class ZAvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs) {

    enum class ClipType {
        None,
        Circle,
        Ellipse,
    }

    enum class RoundMode {
        View,
        Drawable
    }

    var clipType = ClipType.Circle
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var roundMode = RoundMode.Drawable
        set(value) {
            if (field != value) {
                field = value
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

    var fillColor = 0
        set(value) {
            if (field != value) {
                field = value
                fillPaint.color = value
                invalidate()
            }
        }


    private var maskPaint: Paint = Paint()
    private var xferPaint: Paint = Paint()
    private var fillPaint: Paint = Paint()
    private var borderPaint: Paint = Paint()
    private val bounds = RectF()

    init {
        maskPaint.isAntiAlias = true
        xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        fillPaint.color = fillColor
        fillPaint.style = Paint.Style.FILL
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (clipType == ClipType.None) {
            return
        }
        if (drawable == null && roundMode == RoundMode.Drawable) {
            return
        }
        drawCircle(canvas)
    }

    private var src = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    private fun drawCircle(canvas: Canvas) {
        drawable.colorFilter = ColorMatrixColorFilter()
        if (width != src.width || height != src.height) {
            src.recycle()
            src = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            src.eraseColor(0)
            val srcCanvas = Canvas(src)
            computeRoundBounds()
            srcCanvas.drawOval(bounds, maskPaint)
            val l = src.getPixel(0, 0)
            val r = src.getPixel(0, 1)
        }
        canvas.drawBitmap(src, 0f, 0f, xferPaint)
        if (borderWidth > 0) {
            canvas.drawOval(bounds, borderPaint)
        }
    }

    private fun computeRoundBounds() {
        if (roundMode == RoundMode.View) {
            bounds.left = 0f
            bounds.top = 0f
            bounds.right = width.toFloat()
            bounds.bottom = height.toFloat()
        } else if (roundMode == RoundMode.Drawable) {
            bounds.left = 0f
            bounds.top = 0f
            bounds.right = drawable.intrinsicWidth.toFloat()
            bounds.bottom = drawable.intrinsicHeight.toFloat()
            imageMatrix.mapRect(bounds)
        }
        if (clipType == ClipType.Circle) {
            if (bounds.width() > bounds.height()) {
                val c = bounds.centerX()
                bounds.left = c - bounds.height() / 2
                bounds.right = c + bounds.height() / 2
            } else {
                val c = bounds.centerY()
                bounds.top = c - bounds.width() / 2
                bounds.bottom = c + bounds.width() / 2
            }
        }
    }

    private fun adjustCanvas(canvas: Canvas) {
        if (roundMode == RoundMode.Drawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (cropToPadding) {
                    val scrollX = scrollX
                    val scrollY = scrollY
                    canvas.clipRect(scrollX + paddingLeft, scrollY + paddingTop,
                        scrollX + right - left - paddingRight,
                        scrollY + bottom - top - paddingBottom)
                }
            }
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            if (imageMatrix != null) {
                val m = Matrix(imageMatrix)
                canvas.concat(m)
            }
        }
    }

}


