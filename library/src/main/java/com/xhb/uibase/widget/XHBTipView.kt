package com.xhb.uibase.widget

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.xhb.uibase.R
import com.xhb.uibase.resources.ShapeDrawables
import java.lang.ref.WeakReference

class XHBTipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, if (defStyleAttr == 0) R.attr.tipViewStyle else defStyleAttr) {

    enum class Location {
        TopLeft,
        TopCenter,
        TopRight,
        BottomLeft,
        BottomCenter,
        BottomRight,
        AutoToast,
        ManualLayout,
    }

    interface TipViewListener {
        fun tipViewIconTapped(view: XHBTipView, index: Int) {}
        fun tipViewDismissed(view: XHBTipView, timeout: Boolean) {}
    }

    var location: Location? = Location.TopRight
        set(value) {
            if (value == null)
                return
            field = value
            if (value.ordinal >= Location.AutoToast.ordinal) {
                textSize = context.resources.getDimensionPixelOffset(R.dimen.tip_view_small_text_size).toFloat()
                if (value == Location.ManualLayout) {
                    frameRadius = 0f
                }
            }
            requestLayout()
        }

    var arrowSize = 0
        set(value) {
            field = value
            requestLayout()
        }

    var arrowOffset = 0
        set(value) {
            field = value
            requestLayout()
        }

    var maxWidth = 0
        set(value) {
            field = value
            requestLayout()
        }

    var message: CharSequence?
        get() = textView.text
        set(value) {
            textView.text = value
        }

    var dismissDelay: Long = 0

    var textSize: Float
        get() = textView.textSize
        set(value) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }

    var textColor: Int
        get() = textView.currentTextColor
        set(value) {
            textView.setTextColor(value)
        }

    var frameRadius = 0f
        set(value) {
            field = value
            frameDrawable.cornerRadius = value
            invalidate()
        }

    var frameColor = 0
        set(value) {
            field = value
            frameDrawable.setColor(value)
            arrowDrawable.setTint(value)
            invalidate()
        }

    var singleLine: Boolean
        get() = textView.isSingleLine
        set(value) {
            textView.isSingleLine = value
        }

    @DrawableRes
    var leftIcon: Int = 0
        set(value) {
            if (field == value) return
            field = value
            if (value == 0) {
                leftImageView.visibility = View.GONE
            } else {
                leftImageView.setImageDrawable(context.getDrawable(value))
                leftImageView.visibility = View.VISIBLE
            }
        }

    @DrawableRes
    var rightIcon: Int = 0
        set(value) {
            if (field == value) return
            field = value
            if (value == 0) {
                rightImageView.visibility = View.GONE
            } else {
                rightImageView.setImageDrawable(context.getDrawable(value))
                rightImageView.visibility = View.VISIBLE
            }
        }

    @DrawableRes
    var icon: Int = 0
        set(value) {
            if (field == value) return
            field = value
            if (value == 0) {
                imageView.visibility = View.GONE
            } else {
                imageView.setImageDrawable(context.getDrawable(value))
                imageView.visibility = View.VISIBLE
            }
        }

    private var textView: TextView
    private var imageView: ImageView
    private var leftImageView: ImageView
    private var rightImageView: ImageView

    private val frameDrawable: GradientDrawable
    private val arrowDrawable: ShapeDrawable
    private val layerDrawable: LayerDrawable

    private var target: View? = null
    private var listener: TipViewListener? = null
    private var location2 = Location.TopRight

    init {
        LayoutInflater.from(context).inflate(R.layout.tip_view, this)
        leftImageView = findViewById(R.id.leftImageView)
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        rightImageView = findViewById(R.id.rightImageView)

        frameDrawable = ShapeDrawables.getDrawable(context, frameConfig)
        arrowDrawable = ShapeDrawable()
        layerDrawable = LayerDrawable(arrayOf(frameDrawable, arrowDrawable))

        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBTipView, R.attr.tipViewStyle, 0)
        maxWidth = a.getDimensionPixelSize(R.styleable.XHBTipView_maxWidth, maxWidth)
        leftIcon = a.getResourceId(R.styleable.XHBTipView_leftIcon, 0)
        icon = a.getResourceId(R.styleable.XHBTipView_icon, 0)
        rightIcon = a.getResourceId(R.styleable.XHBTipView_rightIcon, 0)
        if (a.hasValue(R.styleable.XHBTipView_android_text))
            message = a.getText(R.styleable.XHBTipView_android_text)
        dismissDelay = a.getInt(R.styleable.XHBTipView_dismissDelay, dismissDelay.toInt()).toLong()
        textColor = a.getColor(R.styleable.XHBTipView_android_textColor, textColor)
        textSize = a.getDimensionPixelSize(R.styleable.XHBTipView_android_textSize, textSize.toInt()).toFloat()
        frameColor = a.getColor(R.styleable.XHBTipView_frameColor, frameColor)
        frameRadius = a.getDimension(R.styleable.XHBTipView_frameRadius, frameRadius)
        arrowSize = a.getDimensionPixelSize(R.styleable.XHBTipView_arrowSize, arrowSize)
        arrowOffset = a.getDimensionPixelSize(R.styleable.XHBTipView_arrowOffset, arrowOffset)
        a.recycle()
    }

    fun popAt(target: View, listener: TipViewListener? = null) {
        this.target = target
        this.listener = listener
        if (listener != null) {
            val onClick: (View) -> Unit = {view ->
                listener.tipViewIconTapped(this, if (view == leftImageView) 0 else 1)
            }
            leftImageView.setOnClickListener(onClick)
            rightImageView.setOnClickListener(onClick)
        } else {
            leftImageView.setOnClickListener(null)
            rightImageView.setOnClickListener(null)
        }
        var mWidth = maxWidth
        if (mWidth < 0) {
            mWidth += target.rootView.width
        }
        if (location == Location.ManualLayout) {
            mWidth = target.width
        }
        val size = calcSize(mWidth)
        val loc = calcLocation(target, size)
        updateArrow()
        // pop
        if (location2 == Location.ManualLayout) {
            //val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.leftMargin = loc.x
            lp.topMargin = loc.y
            (target.context as? Activity)?.window?.addContentView(this, lp)
            if (dismissDelay > 0)
                postDelayed(DismissRunnable(this), dismissDelay)
        }
    }

    class DismissRunnable(view: XHBTipView) : Runnable {
        private val view: WeakReference<XHBTipView> = WeakReference(view)
        override fun run() {
            view.get()?.dismiss()
        }
    }

    fun dismiss(timeout: Boolean = false) {
        if (location2 == Location.ManualLayout) {
            visibility = View.GONE
        } else {
            visibility = View.GONE
            // TODO: remove
        }
        listener?.tipViewDismissed(this, timeout)
    }

    private fun calcSize(maxWidth: Int) : Size {
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        measure(widthMeasureSpec, heightMeasureSpec)
        return Size(measuredWidth, measuredHeight)
    }

    private fun updateArrow() {
        val path = Path()
        val y = location2.ordinal >= 3
        val s = arrowSize.toFloat()
        if (!y) { // down
            path.moveTo(0f, 0f)
            path.lineTo(s * 2, 0f)
            path.lineTo(s, s)
        } else {
            path.moveTo(0f, s)
            path.lineTo(s * 2, s)
            path.lineTo(s, 0f)
        }
        path.close()
        val shape = PathShape(path,s * 2, s)
        arrowDrawable.shape = shape
    }

    private fun layoutBackground() {
        if (location2.ordinal >= Location.AutoToast.ordinal) {
            background = frameDrawable
        } else {
            background = layerDrawable
            val x = location2.ordinal % 3
            val y = location2.ordinal >= 3
            if (!y) {
                layerDrawable.setLayerInset(0, 0, 0, 0, arrowSize)
            } else {
                layerDrawable.setLayerInset(0, 0, arrowSize, 0, 0)
            }
            val loc = Point()
            loc.x = when (x) {
                0 -> width - arrowOffset - arrowSize
                1 -> width / 2 - arrowSize
                else -> arrowOffset - arrowSize
            }
            loc.y = if (y) 0 else height - arrowSize
            layerDrawable.setLayerInset(1, loc.x, loc.y, width - loc.x - arrowSize * 2, height - loc.y - arrowSize)
        }
    }

    companion object {

        private val TAG = "XHBTipView"

        val frameConfig = ShapeDrawables.Config(GradientDrawable.RECTANGLE,
            R.dimen.tip_view_frame_radius, R.color.tip_view_frame_color,
            0, 0,
            0, 0
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure: " + MeasureSpec.toString(widthMeasureSpec) + " " + MeasureSpec.toString(heightMeasureSpec))
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        if (maxWidth in 1 until width) {
            width = maxWidth
        }
        if (location2.ordinal < Location.BottomRight.ordinal && heightMode != MeasureSpec.UNSPECIFIED)
            height -= arrowSize
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode), MeasureSpec.makeMeasureSpec(height, heightMode))
        if (location2.ordinal < Location.BottomRight.ordinal)
            setMeasuredDimension(measuredWidth, measuredHeight + arrowSize)
        Log.d(TAG, "onMeasure: $measuredWidth $measuredHeight")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var tt = t
        var bb = b
        if (location2.ordinal <= Location.TopRight.ordinal)
            bb -= arrowSize
        else if (location2.ordinal <= Location.BottomRight.ordinal)
            tt += arrowSize
        super.onLayout(changed, l, t, r, bb)
        layoutBackground()
    }

    private var toastCount = 0
    private var toastY = 0

    private fun calcLocation(target: View, size: Size): Point {
        val location = this.location!!
        if (location == Location.ManualLayout) {
            return Point()
        }
        val wbounds = Rect()
        target.rootView.getLocalVisibleRect(wbounds)
        // for toast location
        if (location == Location.AutoToast) {
            if (toastCount <= 0) {
                toastY = wbounds.bottom - wbounds.width() / 8
            } else {
                toastY -= size.height + 20
            }
            toastCount += 1
            location2 = location
            return Point(wbounds.centerX() - size.width / 2, toastY - size.height / 2)
        }
        // for arrow locations
        val frame = Rect(0, 0, size.width, size.height)
        val loc = intArrayOf(0, 0)
        target.getLocationInWindow(loc)
        val tbounds = Rect(loc[0], loc[1], loc[0] + target.width, loc[1] + target.height)
        val checkX: (Int) -> Int = { x ->
            when (x) {
                0 -> { // Left
                    frame.right = tbounds.centerX() + arrowOffset
                    frame.left = frame.right - size.width
                }
                1 -> {
                    frame.left = tbounds.centerX() - frame.width() / 2
                    frame.right = frame.left + size.width
                }
                2 -> {
                    frame.left = tbounds.centerX() - arrowOffset
                    frame.right = frame.left + size.width
                }
            }
            if ((frame.left >= wbounds.left && frame.right <= wbounds.right) || (frame.left < wbounds.left && frame.right > wbounds.right)) {
                0
            } else if (frame.left < wbounds.left) {
                1
            } else {
                -1
            }
        }

        val checkY: (Boolean) -> Boolean = { y ->
            if (y) {
                frame.top = tbounds.bottom
                frame.bottom = frame.top + size.height
            } else {
                frame.bottom = tbounds.top
                frame.top = frame.bottom - size.height
            }
            (frame.top >= wbounds.top && frame.bottom <= wbounds.bottom) || (frame.top < wbounds.top && frame.bottom > wbounds.bottom)
        }

        var x = location.ordinal % 3
        var y = location.ordinal >= 3

        var d = checkX(x)
        while (d != 0) {
            x += d
            if (x < 0 || x >= 3) {
                x -= d
                break
            }
            val d1 = checkX(x)
            if (d1 != 0 && d1 != d) {
                x -= d
                checkX(x)
                break
            }
            d = d1
        }
        if (!checkY(y)) {
            if (!checkY(!y)) {
                checkY(y)
            } else {
                y = !y
            }
        }

        location2 = Location.values()[x + if (y) 3 else 0]
        return Point(frame.left, frame.top)
    }

}


