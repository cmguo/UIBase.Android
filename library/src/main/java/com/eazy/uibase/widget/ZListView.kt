package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.eazy.uibase.R
import com.eazy.uibase.view.list.DividerDecoration
import com.eazy.uibase.view.list.RecyclerViewAdapter
import com.eazy.uibase.view.list.RecyclerViewTreeAdapter
import com.eazy.uibase.view.parentOfType

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

    companion object {
        private const val TAG = "ZListView"
    }

    init {
        this.adapter = _adapter
        _adapter.setItemBinding(ZListItemBinding(context))
        this.addItemDecoration(DividerDecoration(LinearLayout.VERTICAL, 1f, ContextCompat.getColor(context, R.color.blue_100)))
        this.layoutManager = LinearLayoutManager(context)
    }

    fun findViewHolderForAdapterPosition(position: IntArray) : ViewHolder? {
        val adapter = this.adapter
        val pos = if (adapter is RecyclerViewTreeAdapter) adapter.getItemPosition(position) else {
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
                ZListWidgetCache.onItemClicked(listView, view)
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

    internal fun listItemChanged(view: View, value: Any?) {
        val itemView = view.parentOfType(ZListItemView::class.java) ?: return
        val position = getChildAdapterPosition(itemView)
        if (position < 0) return
        val adapter = this.adapter
        val position2 = if (adapter is RecyclerViewTreeAdapter) adapter.getTreePosition(position) else intArrayOf(position)
        listener?.onItemValueChanged(this, position2, value)
    }

    /* private */

    private fun syncData() {
        _adapter.replace(data)
    }

    class ListAdapter : RecyclerViewTreeAdapter() {

        override fun getChildren(t: Any?): Iterable<*>? {
            return if (t is ZListItemView.GroupData) t.items else null
        }

    }
}