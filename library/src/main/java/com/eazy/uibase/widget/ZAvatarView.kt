package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

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


    private var borderPaint: Paint
    private var fillPaint: Paint
    private var imagePaint: Paint
    private var portPaint: Paint
    private val bounds = Rect()
    private var radius = 0f
    private var cx = 0f
    private var cy = 0f

    init {
        portPaint = Paint()
        portPaint.isAntiAlias = true
        borderPaint = Paint()
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE
        fillPaint = Paint()
        fillPaint.isAntiAlias = true
        fillPaint.color = fillColor
        fillPaint.style = Paint.Style.FILL
        imagePaint = Paint()
        imagePaint.isAntiAlias = true
        imagePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    override fun onDraw(canvas: Canvas) {
        if (clipType == ClipType.None) {
            super.onDraw(canvas)
            return
        }
        if (drawable == null && roundMode == RoundMode.Drawable) {
            super.onDraw(canvas)
            return
        }
        computeRoundBounds()
        drawCircle(canvas)
        drawImage(canvas)
    }

    @SuppressLint("WrongCall")
    private fun drawImage(canvas: Canvas) {
        val src = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        super.onDraw(Canvas(src))
        val port = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        val portCanvas = Canvas(port)
        val saveCount = portCanvas.saveCount
        portCanvas.save()
        adjustCanvas(portCanvas)
        portCanvas.drawCircle(cx, cy, radius, portPaint!!)
        portCanvas.restoreToCount(saveCount)
        portCanvas.drawBitmap(src, 0f, 0f, imagePaint)
        src.recycle()
        canvas.drawBitmap(port, 0f, 0f, null)
        port.recycle()
    }

    private fun drawCircle(canvas: Canvas) {
        val saveCount = canvas.saveCount
        canvas.save()
        adjustCanvas(canvas)
        canvas.drawCircle(cx, cy, radius, fillPaint!!)
        if (borderWidth > 0) {
            canvas.drawCircle(cx, cy, radius - borderWidth / 2f, borderPaint!!)
        }
        canvas.restoreToCount(saveCount)
    }

    private fun computeRoundBounds() {
        if (roundMode == RoundMode.View) {
            bounds.left = paddingLeft
            bounds.top = paddingTop
            bounds.right = width - paddingRight
            bounds.bottom = height - paddingBottom
        } else if (roundMode == RoundMode.Drawable) {
            drawable.copyBounds(bounds)
        } else {
            throw RuntimeException("unknown round mode:$roundMode")
        }
        radius = Math.min(bounds.width(), bounds.height()) / 2f
        cx = bounds.left + bounds.width() / 2f
        cy = bounds.top + bounds.height() / 2f
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


