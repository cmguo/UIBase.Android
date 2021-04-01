package com.xhb.uibase.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xhb.uibase.R
import com.xhb.uibase.view.list.DeviderDecoration
import kotlin.collections.ArrayList

class XHBPickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : FrameLayout(context, attrs, R.attr.pickerViewStyle) {

    @FunctionalInterface
    interface OnSelectionChangeListener {
        fun onSelectionChanged(picker: XHBPickerView)
    }

    var titles: Iterable<Any> = ArrayList()

    var icons: Iterable<Any>? = null

    var singleSelection = false
        set(value) {
            field = value
            adapter.notifyDataSetChanged()
            selectImage.visibility = if (value && selection != null) View.VISIBLE else View.INVISIBLE
        }

    var selections: List<Int> = ArrayList<Int>()
        set(value) {
            field = value
            adapter.notifyDataSetChanged()
            selectImage.visibility = if (singleSelection && selection != null) View.VISIBLE else View.INVISIBLE
        }

    var selection: Int?
        get() = selections.singleOrNull()
        set(value) {
            selections = if (value == null) arrayListOf() else arrayListOf(value)
        }

    var listener: OnSelectionChangeListener? = null

    private val listView: RecyclerView
    private val selectImage: ImageView
    private val adapter = PickerAdapter(this)

    companion object {
        private const val TAG = "XHBPickerView"
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.picker_view, this)
        listView = findViewById(R.id.listView)
        selectImage = findViewById(R.id.selectImage)

        listView.adapter = adapter
        listView.addItemDecoration(DeviderDecoration(context))
        listView.layoutManager = LinearLayoutManager(context)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            listView.setOnScrollChangeListener(View.OnScrollChangeListener() { _: View, _: Int, _: Int, _: Int, _: Int ->
                layoutSelectImage()
            })
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBPickerView, R.attr.pickerViewStyle, 0)
        singleSelection = a.getBoolean(R.styleable.XHBPickerView_singleSelection, singleSelection)
        val titlesId = a.getResourceId(R.styleable.XHBPickerView_titles, 0)
        val iconsId = a.getResourceId(R.styleable.XHBPickerView_icons, 0)
        a.recycle()

        if (titlesId > 0) {
            titles = resources.getStringArray(titlesId).toList()
        }
        if (iconsId > 0) {
            val ta = resources.obtainTypedArray(iconsId)
            icons = (0 until ta.length()).map { ta.getResourceId(it, 0) }
            ta.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        layoutSelectImage()
    }

    /* private */

    private fun select(index: Int, selected: Boolean) {
        if (singleSelection) {
            (selections as ArrayList<Int>).clear()
            (selections as ArrayList<Int>).add(index)
            selectImage.visibility = View.VISIBLE
            layoutSelectImage()
        } else if (selected) {
            (selections as ArrayList<Int>).add(selections.indexOfLast { it < index } + 1, index)
        } else {
            (selections as ArrayList<Int>).remove(index)
        }
        listener?.onSelectionChanged(this)
    }

    private fun layoutSelectImage() {
        if (!singleSelection)
            return
        val s = selection ?: return
        val holder = listView.findViewHolderForAdapterPosition(s) as? PickerHolder
        if (holder != null) {
            val cb = holder.checkBoxBounds()
            selectImage.layout(cb.left, cb.top, cb.right, cb.bottom)
        }
    }

    private class PickerHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private val imageView = view.findViewById<ImageView>(R.id.image)!!
        private val textView = view.findViewById<TextView>(R.id.title)!!
        private val checkBox = view.findViewById<XHBCheckBox>(R.id.checkbox)!!

        fun bind(icon: Any?, title: Any, state: XHBCheckBox.CheckedState) {
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
            checkBox.visibility = if (state == XHBCheckBox.CheckedState.HalfChecked) View.INVISIBLE else View.VISIBLE
            checkBox.checkedState = state
        }

        fun checkBoxBounds() : Rect {
            val bounds = Rect(0, 0, checkBox.width, checkBox.height)
            bounds.offset(checkBox.left, checkBox.top)
            bounds.offset(itemView.left, itemView.top)
            return bounds
        }
    }

    private class PickerAdapter(private val outer: XHBPickerView) : RecyclerView.Adapter<PickerHolder>(), OnClickListener, XHBCheckBox.OnCheckedStateChangeListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.picker_view_item, parent, false)
            view.setOnClickListener(this)
            view.findViewById<XHBCheckBox>(R.id.checkbox)!!.setOnCheckedStateChangeListener(this)
            return PickerHolder(view)
        }

        override fun getItemCount(): Int {
            return outer.titles.count()
        }

        override fun onBindViewHolder(holder: PickerHolder, position: Int) {
            val checkState = if (outer.singleSelection) XHBCheckBox.CheckedState.HalfChecked else (
                if (outer.selections.contains(position)) XHBCheckBox.CheckedState.FullChecked else XHBCheckBox.CheckedState.NotChecked)
            holder.bind(outer.icons?.elementAtOrNull(position), outer.titles.elementAt(position), checkState)
        }

        override fun onClick(view: View) {
            if (outer.singleSelection) {
                val index = (view.parent as RecyclerView).getChildAdapterPosition(view)
                outer.select(index, true)
            } else {
                view.findViewById<XHBCheckBox>(R.id.checkbox)!!.toggle()
            }
        }

        override fun onCheckedStateChanged(checkBox: XHBCheckBox, state: XHBCheckBox.CheckedState) {
            val view = checkBox.parent as View
            if (view.parent == null)
                return
            val index = (view.parent as RecyclerView).getChildAdapterPosition(view)
            outer.select(index, state == XHBCheckBox.CheckedState.FullChecked)
        }
    }

}