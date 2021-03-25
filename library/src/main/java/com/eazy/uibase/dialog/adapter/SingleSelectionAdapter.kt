package com.eazy.uibase.dialog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.R
import com.eazy.uibase.dialog.bean.ItemBean
import kotlinx.android.synthetic.main.commonlib_single_item.view.*

class SingleSelectionAdapter(private var dataList: ArrayList<ItemBean>,
                             private var onSingleItemClickListener: OnSingleItemClickListener)
    : RecyclerView.Adapter<SingleSelectionAdapterHolder>() {

    fun updateData(dataList: ArrayList<ItemBean>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleSelectionAdapterHolder {
        return SingleSelectionAdapterHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.commonlib_single_item, parent, false))

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: SingleSelectionAdapterHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.setOnClickListener {
            onSingleItemClickListener.onItemClick(position)
        }
    }

    fun getSelectedItemPosition(): Int {
        for (index in 0 until dataList.size) {
            if (dataList[index].isChecked) {
                return index
            }
        }
        return 0
    }
}

class SingleSelectionAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(bean: ItemBean) {
        itemView.tv_content.text = bean.name
        itemView.iv_select.visibility = if (bean.isChecked) {
            itemView.isActivated = true
            View.VISIBLE
        } else {
            itemView.isActivated = false
            View.INVISIBLE
        }
    }

}

interface OnSingleItemClickListener {
    fun onItemClick(position: Int)
}