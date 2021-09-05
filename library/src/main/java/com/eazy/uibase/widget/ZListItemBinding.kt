package com.eazy.uibase.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eazy.uibase.view.list.BaseItemBinding

/*
   Item binding for ListView's item
 */
open class ZListItemBinding(context: Context) : BaseItemBinding() {

    private val _widgetCache = ZListWidgetCache()

    protected var headerStyle = ZListItemView.Appearance(context, true)

    protected var itemStyle = ZListItemView.Appearance(context)

    override fun getItemViewType(item: Any?): Int {
        return 0
    }

    protected open fun getItemViewAppearance(item: Any) : ZListItemView.Appearance {
        return if (item is ZListItemView.GroupData) headerStyle else itemStyle
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewBinding {
        return ViewHolder(ZListItemView(parent.context))
    }

    override fun bindView(view: View, item: Any, position: Int) {
        val itemView = view as ZListItemView
        itemView.appearance = getItemViewAppearance(item)
        val data = if (item is ZListItemView.Data) item else ItemData(this, item)
        itemView.setData(data, _widgetCache)
    }

    protected fun title(item: Any) : String {
        return item.toString()
    }

    protected open fun subTitle(item: Any) : String? {
        return null
    }

    protected open fun icon(item: Any) : Any? {
        return null
    }

    protected open fun contentType(item: Any): ZListItemView.ContentType? {
        return null
    }

    protected open fun content(item: Any): Any? {
        return null
    }

    protected open fun badge(item: Any): Any? {
        return null
    }

    private class ItemData(private val binding: ZListItemBinding, private val item: Any) : ZListItemView.Data {
        override val title: String
            get() = binding.title(item)
        override val subTitle: String?
            get() = binding.subTitle(item)
        override val icon: Any?
            get() = binding.icon(item)
        override val contentType: ZListItemView.ContentType?
            get() = binding.contentType(item)
        override val content: Any?
            get() = binding.content(item)
        override val badge: Any?
            get() = binding.badge(item)

    }
}