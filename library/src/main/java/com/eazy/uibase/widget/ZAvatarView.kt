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
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.avatarViewStyle
) : AppCompatImageView(context, attrs, defStyleAttr) {

    enum class ClipType {
        None,
        Circle,
        Ellipse,
        RoundSquare,
        RoundRect,
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

    var cornerRadius = 0f
        set(value) {
            field = value
            invalidate()
        }

    var cornerRadii: FloatArray? = null
        set(value) {
            field = value
            invalidate()
        }

    var borderColor = 0
        set(value) {
            if (field != value) {
                field = value
                _borderPaint.color = value
                invalidate()
            }
        }

    var borderWidth = 0f
        set(value) {
            if (field != value) {
                field = value
                _borderPaint.strokeWidth = value
                computeRoundBounds()
                invalidate()
            }
        }

    private var _inited = false

    private var _xferPaint: Paint = Paint()
    private var _borderPaint: Paint = Paint()
    private val _circleBounds = RectF()
    private var _roundPath: Path? = null
    private val _clipBounds = RectF()

    private var _dstImage = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var _srcImage = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private var _setXfermode: Method? = null

    init {
        _xferPaint.isAntiAlias = true
        _xferPaint.color = Color.WHITE
        _xferPaint.style = Paint.Style.FILL
        _borderPaint.isAntiAlias = true
        _borderPaint.color = borderColor
        _borderPaint.alpha = 255
        _borderPaint.strokeWidth = borderWidth
        _borderPaint.style = Paint.Style.STROKE

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZAvatarView, defStyleAttr, 0)
        val cr = a.getInt(R.styleable.ZAvatarView_clipRegion, -1)
        if (cr >= 0) clipRegion = ClipRegion.values()[cr]
        val ct = a.getInt(R.styleable.ZAvatarView_clipType, -1)
        if (ct >= 0) clipType = ClipType.values()[ct]
        borderWidth = a.getDimensionPixelSize(R.styleable.ZAvatarView_borderWidth, borderWidth.toInt()).toFloat()
        borderColor = a.getColor(R.styleable.ZAvatarView_borderColor, borderColor)
        a.recycle()

        _inited = true
        computeRoundBounds()
    }


    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (xfermodeClass != null) {
            _setXfermode = try {
                drawable?.javaClass?.getDeclaredMethod("setXfermode", xfermodeClass)
            } catch (e: Throwable) {
                // HUAWEI SCL-TL00H SDK 22 -> ok
                // HUAWEI BZT-W09 SDK 26 -> ok
                // HUAWEI ALP-AL00 SDK 29 -> not such method
                null
            }
            _setXfermode?.isAccessible = true
        }
        computeRoundBounds()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val result = super.setFrame(l, t, r, b)
        computeRoundBounds()
        if ((width != _dstImage.width || height != _dstImage.height)
            && width > 0 && height > 0) {
            _dstImage.recycle()
            _dstImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            _dstImage.eraseColor(0)
            if (_setXfermode == null) {
                _srcImage.recycle()
                _srcImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                _srcImage.eraseColor(0)
            }
        }
        return result
    }

    override fun invalidate() {
        super.invalidate()
        if (_dstImage != null)
            _dstImage.eraseColor(0)
        if (_srcImage != null)
            _srcImage.eraseColor(0)
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
        val dstCanvas = Canvas(_dstImage)
        dstCanvas.clipRect(_clipBounds)
        // prepare mask
        _xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        if (clipType == ClipType.Circle || clipType == ClipType.Ellipse) {
            dstCanvas.drawOval(_circleBounds, _xferPaint)
        } else if (cornerRadii != null) {
            dstCanvas.drawPath(_roundPath!!, _xferPaint)
        } else {
            dstCanvas.drawRoundRect(_circleBounds, cornerRadius, cornerRadius, _xferPaint)
        }
        // take photo of src
        if (_setXfermode != null) {
            val callback = drawable.callback
            drawable.callback = null
            _setXfermode!!.invoke(drawable, PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
            super.onDraw(dstCanvas)
            _setXfermode!!.invoke(drawable, PorterDuffXfermode(PorterDuff.Mode.SRC_OVER))
            drawable.callback = callback
        } else {
            val srcCanvas = Canvas(_srcImage)
            super.onDraw(srcCanvas) // SRC_OVER
            // paint photo on mask
            _xferPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            dstCanvas.drawBitmap(_srcImage, 0f, 0f, _xferPaint)
        }
        canvas.drawBitmap(_dstImage, 0f, 0f, _borderPaint)
        // border
        if (borderWidth > 0) {
            if (clipType == ClipType.Circle || clipType == ClipType.Ellipse) {
                canvas.drawOval(_circleBounds, _borderPaint)
            } else if (cornerRadii != null) {
                canvas.drawPath(_roundPath!!, _borderPaint)
            } else {
                canvas.drawRoundRect(_circleBounds, cornerRadius, cornerRadius, _borderPaint)
            }
        }
    }


    private fun computeRoundBounds() {
        if (clipType == ClipType.None || drawable == null || !_inited)
            return
        val viewBounds = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val imageBounds = RectF(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
        imageMatrix.mapRect(imageBounds)
        if (clipRegion == ClipRegion.WholeView) {
            _circleBounds.set(viewBounds)
        } else {
            _circleBounds.set(imageBounds)
        }
        if (clipType == ClipType.Circle || clipType == ClipType.RoundSquare) {
            if (_circleBounds.width() > _circleBounds.height()) {
                val c = _circleBounds.centerX()
                _circleBounds.left = c - _circleBounds.height() / 2
                _circleBounds.right = c + _circleBounds.height() / 2
            } else {
                val c = _circleBounds.centerY()
                _circleBounds.top = c - _circleBounds.width() / 2
                _circleBounds.bottom = c + _circleBounds.width() / 2
            }
        }
        if (borderWidth > 0)
            _circleBounds.inset(borderWidth / 2f, borderWidth / 2f)
        if (cornerRadii != null) {
            val path = Path()
            path.addRoundRect(_circleBounds, cornerRadii!!, Path.Direction.CW)
            _roundPath = path
        }
        _clipBounds.set(imageBounds)
    }

    companion object {

        private const val TAG = "ZAvatarView"

        val xfermodeClass: Class<*>? = try {
            Class.forName("android.graphics.Xfermode")
        } catch (e: Throwable) {
            null
        }
    }

}


