package com.xhb.uibase.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.xhb.uibase.R
import java.lang.reflect.Method

class XHBAvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, R.attr.avatarViewStyle) {

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
                val drawable = this.drawable
                if (drawable != null) {
                    setXfermode?.invoke(drawable, PorterDuffXfermode(
                        if (value == ClipType.None) PorterDuff.Mode.SRC_OVER else PorterDuff.Mode.DST_OVER))
                }
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

    private var setXfermode: Method? = null

    init {
        xferPaint.isAntiAlias = true
        xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        xferPaint.color = 0
        xferPaint.style = Paint.Style.FILL
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE

        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBAvatarView, R.attr.avatarViewStyle, 0)
        val cr = a.getInt(R.styleable.XHBAvatarView_clipRegion, -1)
        if (cr >= 0) clipRegion = ClipRegion.values()[cr]
        val ct = a.getInt(R.styleable.XHBAvatarView_clipType, -1)
        if (ct >= 0) clipType = ClipType.values()[ct]
        borderWidth = a.getDimensionPixelSize(R.styleable.XHBAvatarView_borderWidth, borderWidth.toInt()).toFloat()
        borderColor = a.getColor(R.styleable.XHBAvatarView_borderColor, borderColor)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setXfermode = drawable?.javaClass?.getMethod("setXfermode", Xfermode::class.java)
        setXfermode?.isAccessible = true
        setXfermode?.invoke(drawable, PorterDuffXfermode(
            if (clipType == ClipType.None) PorterDuff.Mode.SRC_OVER else PorterDuff.Mode.DST_OVER))
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        return super.setFrame(l, t, r, b)
        computeRoundBounds()
    }

    override fun onDraw(canvas: Canvas) {
        if (clipType == ClipType.None) {
            super.onDraw(canvas)
            return
        }
        if (drawable == null && clipRegion == ClipRegion.Drawable) {
            super.onDraw(canvas)
            return
        }
        computeRoundBounds()
        drawMask(canvas)
        super.onDraw(canvas)
        drawBorder(canvas)
    }

//    private var src = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    private fun drawMask(canvas: Canvas) {
//        if (width != src.width || height != src.height) {
//            src.recycle()
//            src = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//            src.eraseColor(0)
//            val srcCanvas = Canvas(src)
//            computeRoundBounds()
//            srcCanvas.drawOval(bounds, maskPaint)
//            val l = src.getPixel(0, 0)
//            val r = src.getPixel(0, 1)
//        }
        canvas.save()
        canvas.clipRect(clipBounds)
        canvas.drawOval(circleBounds, xferPaint)
        canvas.restore()
    }

    private fun drawBorder(canvas: Canvas) {
        if (borderWidth > 0) {
            canvas.drawOval(circleBounds, borderPaint)
        }
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

//    private fun adjustCanvas(canvas: Canvas) {
//        if (roundMode == RoundMode.Drawable) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                if (cropToPadding) {
//                    val scrollX = scrollX
//                    val scrollY = scrollY
//                    canvas.clipRect(scrollX + paddingLeft, scrollY + paddingTop,
//                        scrollX + right - left - paddingRight,
//                        scrollY + bottom - top - paddingBottom)
//                }
//            }
//            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
//            if (imageMatrix != null) {
//                val m = Matrix(imageMatrix)
//                canvas.concat(m)
//            }
//        }
//    }

}


