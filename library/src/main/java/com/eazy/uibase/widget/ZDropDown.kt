package com.eazy.uibase.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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
import com.eazy.uibase.resources.Drawables
import com.eazy.uibase.view.list.DividerDecoration

class ZDropDown @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.dropDownStyle)
    : LinearLayoutCompat(context, attrs, defStyleAttr) {

    interface DropDownListener {
        fun dropDownFinished(dropDown: ZDropDown, selection: Int?)
    }

    var titles: Iterable<Any> = ArrayList()

    var icons: Iterable<Any>? = null

    var shadowRadius = 0f
        set(value) {
            field = value
            val padding = value.toInt()
            setPadding(padding, padding, padding, padding)
        }

    var cornerRadius = 0f

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
        listView.addItemDecoration(DividerDecoration(context, 1f, ContextCompat.getColor(context, R.color.bluegrey_100)))
        listView.layoutManager = LinearLayoutManager(context)

        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        backgroundPaint.style = Paint.Style.FILL

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZDropDown, defStyleAttr, 0)
        shadowRadius = a.getDimensionPixelSize(R.styleable.ZDropDown_shadowRadius, 0).toFloat()
        cornerRadius = a.getDimensionPixelSize(R.styleable.ZDropDown_cornerRadius, 0).toFloat()
        backgroundPaint.color = a.getColor(R.styleable.ZDropDown_backgroundColor, Color.WHITE)
        a.recycle()
    }

    fun popAt(target: View, listener: DropDownListener) {
        val size = calcSize()
        // pop
        val window = PopupWindow(this, size.width, size.height, true)
        adapter.listener = {
            if (it != null)
                window.dismiss()
            listener.dropDownFinished(this, it)
        }
        // for sdk 21, window don't dismiss when click outside. setBackgroundDrawable can work
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.showAsDropDown(target, 0, 0, Gravity.TOP or Gravity.START)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        bounds.inset(shadowRadius, shadowRadius)
        backgroundPaint.setShadowLayer(shadowRadius, 0f, 0f, Color.GRAY)
        canvas?.drawRoundRect(bounds, cornerRadius, cornerRadius, backgroundPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter.listener?.invoke(null)
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
                    imageView.setImageDrawable(Drawables.getDrawable(imageView.context, icon))
                } else {
                    imageView.setImageDrawable(icon as Drawable)
                }
                imageView.visibility = View.VISIBLE
            }
        }
    }

    class DropDownAdapter(private val outer: ZDropDown) : RecyclerView.Adapter<DropDownHolder>(), OnClickListener {

        var listener: ((Int?) -> Unit)? = null

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

        override fun onClick(view: View) {
            val index = (view.parent as ViewGroup).indexOfChild(view)
            val l = listener
            listener = null
            l?.invoke(index)
        }
    }

}