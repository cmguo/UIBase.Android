package com.eazy.uibase.demo.components.dataview

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.Bindable
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ComponentStyle
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.ListViewFragmentBinding
import com.eazy.uibase.demo.resources.Colors
import com.eazy.uibase.demo.resources.Resources
import com.eazy.uibase.view.list.BackgroundDecoration
import com.eazy.uibase.view.list.ItemDecorations
import com.eazy.uibase.view.list.UnitTypeItemBinding
import com.eazy.uibase.widget.ZListItemView
import com.eazy.uibase.widget.ZListView
import com.eazy.uibase.widget.ZTipView
import java.lang.reflect.Field
import java.lang.reflect.Method

class ZListViewFragment : ComponentFragment<ListViewFragmentBinding?, ZListViewFragment.Model?, ZListViewFragment.Styles?>() {

    class Model(fragment: ZListViewFragment) : ViewModel() {

        val empty = arrayListOf<Any>()
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
        @Title("空列表")
        var empty = false

        @Bindable
        @Title("分组")
        var group = false

        @Bindable
        @Title("装饰器")
        @Style(DecorationStyle::class)
        var itemDecoration : ItemDecorations.Builder? = null

        val emptyItemBinding = object : UnitTypeItemBinding(R.layout.list_empty_view) {
            override fun bindView(view: View, item: Any?, position: Int) {
                val image = view.findViewById<ImageView>(R.id.image)
                image.setImageResource(R.drawable.icon_weblink)
                image.visibility = View.VISIBLE
                val title = view.findViewById<TextView>(R.id.title)
                title.text = "标题"
                title.visibility = View.VISIBLE
                val subTitle = view.findViewById<TextView>(R.id.subTitle)
                subTitle.text = "详细内容详细内容详细内容详细内容详细内容详细内容"
                subTitle.visibility = View.VISIBLE
                val button = view.findViewById<TextView>(R.id.button)
                button.text = "重试"
                button.visibility = View.VISIBLE
            }
        }
    }

    class DecorationStyle(field: Field, getter: Method? = null, setter: Method? = null)
        : ComponentStyle(field, getter, setter) {
        init {
            val values = arrayListOf("divider", "background", "tree")
            val titles = arrayListOf("分割线", "圆角背景", "树形装饰器")
            setValues(values, titles)
        }
        override fun valueToString(value: Any?): String {
            for (e in decorations.entries) {
                if (e.value === value)
                    return e.key
            }
            return ""
        }
        override fun valueFromString(value: String?): Any? {
            return decorations.get(value)
        }
        companion object {
            val decorations = mapOf<String, ItemDecorations.Builder>(
                "divider" to ItemDecorations.divider(1f, Color.RED),
                "background" to ItemDecorations.background(20f, Color.GRAY, true),
                "tree" to ItemDecorations.Builder {
                    object : BackgroundDecoration(40f, Color.GRAY, true) {
                        val paint = Paint()
                        val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
                        override fun drawTreeDecoration(c: Canvas, position: IntArray?, level: Int) {
                            paint.color = colors[level]
                            val rect = RectF(outRect)
                            rect.inset(10f, 20f)
                            c.drawRoundRect(rect, 20f, 20f, paint)
                        }
                        override fun getTreeOffsets(treeRect: Rect, position: IntArray?, level: Int) {
                            treeRect[20, 40, 20] = 40
                        }
                    }
                }
            )
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val decoration = binding.listView.getItemDecorationAt(0)
        styles.itemDecoration = ItemDecorations.Builder {
            decoration
        }
    }

    companion object {
        private const val TAG = "ZListFragment"
    }
}

class ResourceColorGroup(private val name: String, colors: List<ResourceColorItem>) : ZListItemView.GroupData {
    private val colors = colors.filter { it.title.startsWith(name + "_") }
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