package com.eazy.uibase.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.R
import com.eazy.uibase.view.list.DeviderDecoration
import kotlin.collections.ArrayList

class ZPickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : LinearLayoutCompat(context, attrs, R.attr.pickerViewStyle) {

    @FunctionalInterface
    interface OnSelectionChangeListener {
        fun onSelectionChanged(picker: ZPickerView)
    }

    var titles: Iterable<Any> = ArrayList()

    var icons: Iterable<Any>? = null

    var singleSelection = false

    var selections = ArrayList<Int>()

    var selection: Int?
        get() = selections.singleOrNull()
        set(value) {
            selections = if (value == null) arrayListOf() else arrayListOf(value)
        }

    var listener: OnSelectionChangeListener? = null

    private val listView: RecyclerView
    private val adapter = PickerAdapter(this)

    companion object {
        private const val TAG = "ZPicker"
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.picker_view, this)
        listView = findViewById(R.id.listView)
        listView.adapter = adapter
        listView.addItemDecoration(DeviderDecoration(context))
        listView.layoutManager = LinearLayoutManager(context)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZPickerView, R.attr.pickerViewStyle, 0)
        singleSelection = a.getBoolean(R.styleable.ZPickerView_singleSelection, singleSelection)
        val titlesId = a.getResourceId(R.styleable.ZPickerView_titles, 0)
        val iconsId = a.getResourceId(R.styleable.ZPickerView_icons, 0)
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

    /* private */

    private fun select(index: Int, selected: Boolean) {
        if (singleSelection) {
            selections = arrayListOf(index)
        } else if (selected) {
            selections.add(selections.indexOfLast { it < index } + 1, index)
        }
        listener?.onSelectionChanged(this)
    }

    private class PickerHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private val imageView = view.findViewById<ImageView>(R.id.image)!!
        private val textView = view.findViewById<TextView>(R.id.title)!!
        private val checkBox = view.findViewById<ZCheckBox>(R.id.checkbox)!!

        fun bind(icon: Any?, title: Any, sc: Boolean) {
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
            checkBox.visibility = if (sc) View.VISIBLE else View.INVISIBLE
        }
    }

    private class PickerAdapter(private val outer: ZPickerView) : RecyclerView.Adapter<PickerHolder>(), OnClickListener, ZCheckBox.OnCheckedStateChangeListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.picker_view_item, parent, false)
            view.setOnClickListener(this)
            view.findViewById<ZCheckBox>(R.id.checkbox)!!.setOnCheckedStateChangeListener(this)
            return PickerHolder(view)
        }

        override fun getItemCount(): Int {
            return outer.titles.count()
        }

        override fun onBindViewHolder(holder: PickerHolder, position: Int) {
            holder.bind(outer.icons?.elementAtOrNull(position), outer.titles.elementAt(position), outer.singleSelection)
        }

        override fun onClick(view: View) {
            if (outer.singleSelection) {
                val index = (view.parent as ViewGroup).indexOfChild(view)
                outer.select(index, true)
            } else {
                view.findViewById<ZCheckBox>(R.id.checkbox)!!.toggle()
            }
        }

        override fun onCheckedStateChanged(checkBox: ZCheckBox, state: ZCheckBox.CheckedState) {
            val view = checkBox.parent as View
            val index = (view.parent as ViewGroup).indexOfChild(view)
            outer.select(index, state == ZCheckBox.CheckedState.FullChecked)
        }
    }

}