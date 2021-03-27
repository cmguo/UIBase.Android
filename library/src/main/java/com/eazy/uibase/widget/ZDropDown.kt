package com.eazy.uibase.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.R
import com.eazy.uibase.view.list.DeviderDecoration

class ZDropDown @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : LinearLayoutCompat(context, attrs, R.attr.dropDownStyle) {

    @FunctionalInterface
    interface DropDownListener {
        fun dropDownFinished(selection: Int?)
    }

    var titles: Iterable<Any> = ArrayList()

    var icons: Iterable<Any>? = null

    var shadowRadius = 0f
        set(value) {
            field = value
            val padding = value.toInt()
            setPadding(padding, padding, padding, padding)
        }

    var borderRadius = 0f

    private val listView: RecyclerView
    private val adapter = DropDownAdapter(this)
    private val bounds = RectF()
    private val backgroundPaint = Paint()

    companion object {
        private const val TAG = "ZDropDown"
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.drop_down, this)
        listView = findViewById(R.id.listView)
        listView.adapter = adapter
        listView.addItemDecoration(DeviderDecoration(context))
        listView.layoutManager = LinearLayoutManager(context)

        setWillNotDraw(false)
        //setLayerType(LAYER_TYPE_SOFTWARE, null)

        backgroundPaint.style = Paint.Style.FILL

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZDropDown, R.attr.dropDownStyle, 0)
        shadowRadius = a.getDimensionPixelSize(R.styleable.ZDropDown_shadowRadius, 0).toFloat()
        borderRadius = a.getDimensionPixelSize(R.styleable.ZDropDown_borderRadius, 0).toFloat()
        backgroundPaint.color = a.getColor(R.styleable.ZDropDown_backgroundColor, Color.WHITE)
        a.recycle()
    }

    fun popAt(target: View, listener: DropDownListener) {
        val size = calcSize()
        // pop
        val window = PopupWindow(this, size.width, size.height, true)
        adapter.listener = object : DropDownListener {
            override fun dropDownFinished(selection: Int?) {
                if (selection != null)
                    window.dismiss()
                listener.dropDownFinished(selection)
            }
        }
        window.showAsDropDown(target, 0, 0, Gravity.TOP or Gravity.START)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure: " + MeasureSpec.toString(widthMeasureSpec) + " " + MeasureSpec.toString(heightMeasureSpec))
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure: $measuredWidth $measuredHeight")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        bounds.inset(shadowRadius, shadowRadius)
        backgroundPaint.setShadowLayer(shadowRadius, 0f, 0f, Color.GRAY)
        canvas?.drawRoundRect(bounds, borderRadius, borderRadius, backgroundPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter.listener?.dropDownFinished(null)
        adapter.listener = null
    }

    private fun calcSize() : Size {
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        measure(widthMeasureSpec, heightMeasureSpec)
        return Size(measuredWidth, measuredHeight)
    }

    class DropDownHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private val imageView = view.findViewById<ImageView>(R.id.image)!!
        private val textView = view.findViewById<TextView>(R.id.title)!!

        fun bind(icon: Any?, title: Any) {
            textView.text = title.toString()
            if (icon == null) {
                imageView.setImageDrawable(null)
                imageView.visibility = View.GONE
            } else {
                if (icon is Int) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, icon))
                } else {
                    imageView.setImageDrawable(icon as Drawable)
                }
                imageView.visibility = View.VISIBLE
            }
        }
    }

    class DropDownAdapter(private val outer: ZDropDown) : RecyclerView.Adapter<DropDownHolder>(), OnClickListener {

        var listener: DropDownListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DropDownHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.drop_down_item, parent, false)
            view.setOnClickListener(this)
            return DropDownHolder(view)
        }

        override fun getItemCount(): Int {
            return outer.titles.count()
        }

        override fun onBindViewHolder(holder: DropDownHolder, position: Int) {
            holder.bind(outer.icons?.elementAtOrNull(position), outer.titles.elementAt(position))
        }

        override fun onClick(v: View?) {
            val index = (v?.parent as ViewGroup).indexOfChild(v)
            val l = listener
            listener = null
            l?.dropDownFinished(index)
        }
    }

}