package com.xhb.uibase.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.xhb.uibase.R
import com.xhb.uibase.resources.RoundDrawable
import com.xhb.uibase.view.contentView
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
        fun tipViewButtonClicked(view: XHBTipView, btnId: Int) {}
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
                    frameColor = ContextCompat.getColor(context, R.color.bluegrey_05)
                    textColor = ContextCompat.getColor(context, R.color.bluegrey_800)
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
        get() = _textView.text
        set(value) {
            _textView.text = value
        }

    var dismissDelay: Long = 0

    var textSize: Float
        get() = _textView.textSize
        set(value) {
            _textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }

    var textColor: Int
        get() = _textView.currentTextColor
        set(value) {
            _textView.setTextColor(value)
        }

    var frameRadius = 0f
        set(value) {
            field = value
            _frameDrawable.borderRadius = value
            invalidate()
        }

    var frameColor = 0
        set(value) {
            field = value
            _frameDrawable.fillColor = ColorStateList.valueOf(value)
            _arrowDrawable.setTint(value)
            invalidate()
        }

    var singleLine: Boolean
        get() = _textView.isSingleLine
        set(value) {
            _textView.isSingleLine = value
        }

    @DrawableRes
    var leftButton: Int = 0
        set(value) {
            if (field == value) return
            field = value
            syncButton(_leftButton, value)
        }

    @DrawableRes
    var rightButton: Int = 0
        set(value) {
            if (field == value) return
            field = value
            syncButton(_rightButton, value)
        }

    @DrawableRes
    var icon: Int = 0
        set(value) {
            if (field == value) return
            field = value
            if (value == 0) {
                _imageView.visibility = View.GONE
            } else {
                _imageView.setImageDrawable(ContextCompat.getDrawable(context, value))
                _imageView.visibility = View.VISIBLE
            }
        }

    private var _textView: TextView
    private var _imageView: ImageView
    private var _leftButton: XHBButton
    private var _rightButton: XHBButton

    private val _frameDrawable: RoundDrawable
    private val _arrowDrawable: ShapeDrawable
    private val _layerDrawable: LayerDrawable

    private var _target: View? = null
    private var _listener: TipViewListener? = null
    private var _location = Location.TopRight

    init {
        LayoutInflater.from(context).inflate(R.layout.tip_view, this)
        _leftButton = findViewById(R.id.leftButton)
        _imageView = findViewById(R.id.imageView)
        _textView = findViewById(R.id.textView)
        _rightButton = findViewById(R.id.rightButton)

        _frameDrawable = RoundDrawable(context, R.style.XHBTipView_Frame)
        _arrowDrawable = ShapeDrawable()
        _layerDrawable = LayerDrawable(arrayOf(_frameDrawable, _arrowDrawable))

        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBTipView, R.attr.tipViewStyle, 0)
        maxWidth = a.getDimensionPixelSize(R.styleable.XHBTipView_maxWidth, maxWidth)
        leftButton = a.getResourceId(R.styleable.XHBTipView_leftButton, 0)
        icon = a.getResourceId(R.styleable.XHBTipView_icon, 0)
        rightButton = a.getResourceId(R.styleable.XHBTipView_rightButton, 0)
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
        this._target = target
        this._listener = listener
        if (listener != null) {
            val onClick: (View) -> Unit = { view ->
                listener.tipViewButtonClicked(this, view.id)
            }
            _leftButton.setOnClickListener(onClick)
            _rightButton.setOnClickListener(onClick)
        } else {
            _leftButton.setOnClickListener(null)
            _rightButton.setOnClickListener(null)
        }
        var mWidth = maxWidth
        if (mWidth <= 0) {
            mWidth += target.rootView.width
        }
        if (location == Location.ManualLayout) {
            mWidth = target.width
        }
        val size = calcSize(mWidth)
        val loc = calcLocation(target, size)
        updateArrow()
        // pop
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.leftMargin = loc.x
        lp.topMargin = loc.y
        if (_location == Location.ManualLayout) {
            lp.width = mWidth
        }
        var context = target.context
        while (context is ContextWrapper) {
            if (context is Activity)
                break
            context = context.baseContext
        }
        if (_location == Location.AutoToast) {
            ++toastCount
        }
        val content = target.contentView
        if (content == null) {
            Log.w(TAG, "popAt: not found window!")
            return
        }
        if (dismissDelay > 0)
            postDelayed(DismissRunnable(this), dismissDelay)
        if (_location < Location.AutoToast) {
            overlayFrame(content, true)?.attach(this)
        }
        content.addView(this, lp)
        _textView.requestFocus()
    }

    fun popUp(duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast(context)
        toast.duration = duration
        _location = Location.ManualLayout
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 40)
        toast.view = this
        toast.show()
    }

    class DismissRunnable(view: XHBTipView) : Runnable {
        private val view: WeakReference<XHBTipView> = WeakReference(view)
        override fun run() {
            view.get()?.dismiss()
        }
    }

    fun dismiss(timeout: Boolean = false) {
        overlayFrame(contentView)?.detach(this)
        (parent as? ViewGroup)?.removeView(this)
        _listener?.tipViewDismissed(this, timeout)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (_location == Location.AutoToast) {
            --toastCount
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure: " + MeasureSpec.toString(widthMeasureSpec) + " " + MeasureSpec.toString(heightMeasureSpec))
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var mWidth = maxWidth
        if (mWidth <= 0) {
            mWidth += rootView.width
        }
        if (_location != Location.ManualLayout && mWidth in 1 until width) {
            width = mWidth
        }
        if (_location.ordinal < Location.BottomRight.ordinal && heightMode != MeasureSpec.UNSPECIFIED)
            height -= arrowSize
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode), MeasureSpec.makeMeasureSpec(height, heightMode))
        if (_location.ordinal < Location.BottomRight.ordinal)
            setMeasuredDimension(measuredWidth, measuredHeight + arrowSize)
        Log.d(TAG, "onMeasure: $measuredWidth $measuredHeight")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var tt = t
        var bb = b
        if (_location.ordinal <= Location.TopRight.ordinal)
            bb -= arrowSize
        else if (_location.ordinal <= Location.BottomRight.ordinal)
            tt += arrowSize
        super.onLayout(changed, l, t, r, bb)
        layoutBackground()
    }

    /* private */

    companion object {

        private const val TAG = "XHBTipView"

        private var toastCount = 0
        private var toastY = 0

        @SuppressLint("ViewConstructor")
        class OverlayFrame(content: FrameLayout) : FrameLayout(content.context) {

            val list = mutableListOf<XHBTipView>()

            init {
                id = R.id.tip_view_overly_frame
                content.addView(this, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            }

            fun attach(view: XHBTipView) {
                if (!list.contains(view))
                    list.add(view)
                visibility = VISIBLE
            }

            fun detach(view: XHBTipView) {
                if (list.remove(view) && list.isEmpty()) {
                    visibility = GONE
                }
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouchEvent(event: MotionEvent?): Boolean {
                val listCopy = ArrayList(list)
                for (v in listCopy)
                    v.dismiss()
                return super.onTouchEvent(event)
            }
        }

        fun overlayFrame(content: FrameLayout?, create: Boolean = false): OverlayFrame? {
            var frame = content?.findViewById<OverlayFrame>(R.id.tip_view_overly_frame)
            if (frame == null && create && content != null)
                frame = OverlayFrame(content)
            return frame
        }
    }

    private fun syncButton(button: XHBButton, content: Int) {
        if (content == 0) {
            button.visibility = GONE
        } else {
            button.content = content
            button.visibility = VISIBLE
            button.setOnClickListener {
                _listener?.tipViewButtonClicked(this, it.id)
            }
        }
    }

    private fun updateArrow() {
        val path = Path()
        val y = _location.ordinal >= 3
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
        _arrowDrawable.shape = shape
    }

    private fun layoutBackground() {
        if (_location.ordinal >= Location.AutoToast.ordinal) {
            background = _frameDrawable
        } else {
            background = _layerDrawable
            val x = _location.ordinal % 3
            val y = _location.ordinal >= 3
            if (!y) {
                _layerDrawable.setLayerInset(0, 0, 0, 0, arrowSize)
            } else {
                _layerDrawable.setLayerInset(0, 0, arrowSize, 0, 0)
            }
            val loc = Point()
            loc.x = when (x) {
                0 -> width - arrowOffset - arrowSize
                1 -> width / 2 - arrowSize
                else -> arrowOffset - arrowSize
            }
            loc.y = if (y) 0 else height - arrowSize
            _layerDrawable.setLayerInset(1, loc.x, loc.y, width - loc.x - arrowSize * 2, height - loc.y - arrowSize)
        }
    }

    private fun calcSize(maxWidth: Int) : Size {
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        measure(widthMeasureSpec, heightMeasureSpec)
        return Size(measuredWidth, measuredHeight)
    }

    private fun calcLocation(target: View, size: Size): Point {
        val location = this.location!!
        val loc = intArrayOf(0, 0)
        if (location == Location.ManualLayout) {
            target.getLocationInWindow(loc)
            _location = location
            return Point(loc[0], loc[1])
        }
        val rootView = target.rootView
        rootView.getLocationInWindow(loc)
        val wBounds = Rect(loc[0], loc[1], rootView.width, rootView.height)
        // for toast location
        if (location == Location.AutoToast) {
            if (toastCount <= 0) {
                toastY = wBounds.bottom
            }
            toastY -= size.height + 20
            _location = location
            return Point(wBounds.centerX() - size.width / 2, toastY)
        }
        // for arrow locations
        val frame = Rect(0, 0, size.width, size.height)
        target.getLocationInWindow(loc)
        val tBounds = Rect(loc[0], loc[1], loc[0] + target.width, loc[1] + target.height)
        val checkX: (Int) -> Int = { x ->
            when (x) {
                0 -> { // Left
                    frame.right = tBounds.centerX() + arrowOffset
                    frame.left = frame.right - size.width
                }
                1 -> {
                    frame.left = tBounds.centerX() - frame.width() / 2
                    frame.right = frame.left + size.width
                }
                2 -> {
                    frame.left = tBounds.centerX() - arrowOffset
                    frame.right = frame.left + size.width
                }
            }
            if ((frame.left >= wBounds.left && frame.right <= wBounds.right) || (frame.left < wBounds.left && frame.right > wBounds.right)) {
                0
            } else if (frame.left < wBounds.left) {
                1
            } else {
                -1
            }
        }

        val checkY: (Boolean) -> Boolean = { y ->
            if (y) {
                frame.top = tBounds.bottom
                frame.bottom = frame.top + size.height
            } else {
                frame.bottom = tBounds.top
                frame.top = frame.bottom - size.height
            }
            (frame.top >= wBounds.top && frame.bottom <= wBounds.bottom) || (frame.top < wBounds.top && frame.bottom > wBounds.bottom)
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

        _location = Location.values()[x + if (y) 3 else 0]
        return Point(frame.left, frame.top)
    }

}


