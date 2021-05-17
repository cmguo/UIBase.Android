package com.eazy.uibase.demo.components.dataview

import android.graphics.drawable.ColorDrawable
import androidx.databinding.Bindable
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.ListViewFragmentBinding
import com.eazy.uibase.demo.resources.Colors
import com.eazy.uibase.demo.resources.Drawables
import com.eazy.uibase.demo.resources.Resources
import com.eazy.uibase.widget.ZListItemView
import com.eazy.uibase.widget.ZListView
import com.eazy.uibase.widget.ZTipView

class ZListViewFragment : ComponentFragment<ListViewFragmentBinding?, ZListViewFragment.Model?, ZListViewFragment.Styles?>() {

    class Model(fragment: ZListViewFragment) : ViewModel() {
        val colors: List<ZListItemView.Data>
        val colorGroups: List<ZListItemView.Data>

        init {
            val colors = Colors.stdDynamicColors(fragment.requireContext()).map { ResourceColorItem(Colors.simpleName(it.key), it.value) }
            this.colors = colors
            colorGroups = listOf("bluegrey", "blue", "red", "brand", "cyan", "green", "purple", "redorange").map { ResourceColorGroup(it, colors) }
        }
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("分组")
        var group = false
    }

    val listener = object : ZListView.OnItemValueChangeListener {
        override fun onItemValueChanged(listView: ZListView, item: IntArray, value: Any?) {
            val holder = listView.findViewHolderForAdapterPosition(item)
            if (holder != null) {
                val tip = ZTipView(requireContext())
                val pos = item.contentToString()
                tip.message = "项目 $pos 的值变为 $value"
                tip.location = ZTipView.Location.TopCenter
                tip.popAt(holder.itemView)
            }
        }
    }

    companion object {
        private const val TAG = "ZListFragment"
    }
}

class ResourceColorGroup(private val name: String, colors: List<ResourceColorItem>) : ZListItemView.GroupData {
    private val colors = colors.filter { it.title.startsWith(name) }
    override val items: Iterable<ZListItemView.Data>
        get() = colors
    override val title: String
        get() = name
    override val subTitle: String?
        get() = null
    override val icon: Any?
        get() = null
    override val contentType: ZListItemView.ContentType?
        get() = null
    override val content: Any?
        get() = null
    override val badge: Any?
        get() = null
}

class ResourceColorItem(private val key: String, private val value: Resources.ResourceValue) : ZListItemView.Data {
    override val title: String
        get() = key
    override val subTitle: String?
        get() = if (key.contains("600")) null else Colors.text(value.value)
    override val icon: Any
        get() = ColorDrawable(value.value)
    override val contentType: ZListItemView.ContentType?
        get() = when (key) {
            "blue_100" -> ZListItemView.ContentType.Button
            "bluegrey_100", "bluegrey_300"-> ZListItemView.ContentType.CheckBox
            "bluegrey_800", "bluegrey_900" -> ZListItemView.ContentType.RadioButton
            "brand_600" -> ZListItemView.ContentType.TextField
            "red_600" -> ZListItemView.ContentType.SwitchButton
            else -> null
        }
    override val content: Any?
        get() = when (key) {
            "blue_100" -> R.array.button_icon_text
            "bluegrey_800" -> true
            "brand_600" -> "请输入"
            else -> null
        }
    override val badge: Any?
        get() = null

}