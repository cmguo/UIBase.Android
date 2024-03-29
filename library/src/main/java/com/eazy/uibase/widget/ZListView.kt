package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.eazy.uibase.R
import com.eazy.uibase.view.list.*
import com.eazy.uibase.view.parentOfType
import java.lang.ref.WeakReference

@SuppressLint("CustomViewStyleable")
class ZListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.listViewStyle)
    : RecyclerView(context, attrs, defStyleAttr) {

    interface OnItemValueChangeListener {
        fun onItemValueChanged(listView: ZListView, item: IntArray, value: Any?)
    }

    var data: Iterable<Any> = ArrayList()
        set(value) {
            field = value
            syncData()
        }

    var listener: OnItemValueChangeListener? = null

    private val _adapter = ListAdapter()
    private val _decoration: WeakReference<DividerDecoration>

    companion object {
        private const val TAG = "ZListView"
    }

    init {
        this.adapter = _adapter
        _adapter.setItemBinding(ZListItemBinding(context))
        val decoration = ItemDecorations.divider(1f,
            ContextCompat.getColor(context, R.color.blue_100)).build(this)
        _decoration = WeakReference(decoration as DividerDecoration)
        this.addItemDecoration(decoration)
        this.layoutManager = LinearLayoutManager(context)
    }

    fun findViewHolderForAdapterPosition(position: IntArray) : ViewHolder? {
        val adapter = this.adapter
        val data = if (adapter is RecyclerViewAdapter) adapter.items else null
        val pos = if (data is RecyclerTree) data.getItemPosition(position) - 1 else {
            if (position.size != 1) {
                error("Assertion failed")
            }
            position[0]
        }
        return super.findViewHolderForAdapterPosition(pos)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (adapter is RecyclerViewAdapter) {
            adapter.setOnItemClickListener { listView: RecyclerView, view: View, _: Int, _: Any ->
                ZListWidgetCache.onItemClicked(listView as ZListView, view)
            }
            adapter.setOnViewBindingCreateListener { _: RecyclerView, binding: ViewBinding ->
                binding.root.findViewById<ZCheckBox>(R.id.checkBox)?.setOnCheckedStateChangeListener(object: ZCheckBox.OnCheckedStateChangeListener {
                    override fun onCheckedStateChanged(checkBox: ZCheckBox, state: ZCheckBox.CheckedState) {
                        TODO("Not yet implemented")
                    }
                })
            }
            adapter.setItemBinding(ZListItemBinding(context))
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        val decoration = _decoration.get()
        if (decoration != null) {
            decoration.updateColor(ContextCompat.getColor(context, R.color.blue_100))
            invalidate()
        }
    }

    internal fun listItemChanged(view: View, value: Any?) {
        val itemView = view.parentOfType(ZListItemView::class.java) ?: return
        val position = getChildAdapterPosition(itemView)
        if (position < 0) return
        val adapter = this.adapter
        val data = if (adapter is RecyclerViewAdapter) adapter.items else null
        val position2 = if (data is RecyclerTree) data.getTreePosition(position + 1) else intArrayOf(position)
        listener?.onItemValueChanged(this, position2, value)
    }

    /* private */

    private fun syncData() {
        _adapter.setItems(data)
    }

    private class ListAdapter : RecyclerViewAdapter() {
        private var list : List<*>? = null
        override fun setItems(items: List<*>?) {
            if (items == list)
                return;
            list = items;
            val items2 = object : RecyclerTreeList<Any>(items) {
                override fun getChildren(t: Any?): Iterable<Any>? {
                    return if (t is ZListItemView.GroupData) t.items else null
                }
            }
            super.setItems(items2)
        }
    }

}