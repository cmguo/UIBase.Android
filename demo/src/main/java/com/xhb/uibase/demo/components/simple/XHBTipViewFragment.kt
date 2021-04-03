package com.xhb.uibase.demo.components.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.GridLayoutManager
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.ContentStyle
import com.xhb.uibase.demo.core.style.DimemDpStyle
import com.xhb.uibase.demo.core.style.IconStyle
import com.xhb.uibase.demo.core.style.annotation.*
import com.xhb.uibase.demo.databinding.XhbTipViewButtonItemBinding
import com.xhb.uibase.demo.databinding.XhbTipViewFragmentBinding
import com.xhb.uibase.demo.databinding.XhbToastTipViewBinding
import com.xhb.uibase.demo.databinding.XhbToolTipViewBinding
import com.xhb.uibase.view.list.UnitTypeItemBinding
import com.xhb.uibase.widget.XHBTipView

class XHBTipViewFragment : ComponentFragment<XhbTipViewFragmentBinding?, XHBTipViewFragment.Model?, XHBTipViewFragment.Styles?>(), XHBTipView.TipViewListener {

    class Model(fragment: XHBTipViewFragment) : ViewModel() {

        var buttons = arrayOfNulls<Any>(when (fragment.component.id()) {
            R.id.component_xhb_tool_tips -> 24
            else -> 2 }).toList()
    }

    open class Styles(val fragment: XHBTipViewFragment) : ViewStyles() {

        val layoutManager = GridLayoutManager(fragment.context, 4)

        val itemBinding: ItemBinding = ItemBinding(this)

        @Bindable
        @Title("最大宽度")
        @Description("整体最大宽度，设置负数，则自动加上窗口宽度")
        @Style(DimemDpStyle::class)
        var maxWidth = 300

        @Bindable
        @Title("单行文字")
        @Description("限制单行文字，默认是多行的")
        var singleLine = false

        @Bindable
        @Title("消息内容")
        @Description("自适应消息内容的宽度和高度")
        var message = "你点击了按钮"

        open fun buttonClick(view: View) {}
    }

    class ToolStyles(fragment: XHBTipViewFragment) : Styles(fragment) {

        @Bindable
        @Title("建议位置")
        @Description("建议弹出位置，如果左右或者上下超过窗口区域，则会分别自动调整为其他适当位置")
        var location = XHBTipView.Location.TopRight

        @Bindable
        @Title("右侧按钮")
        @Description("右侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var rightButton = 0

        override fun buttonClick(view: View) {
            val binding = XhbToolTipViewBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            binding.tipView.popAt(view, fragment)
        }
    }

    class ToastStyles(fragment: XHBTipViewFragment) : Styles(fragment) {

        @Bindable
        @Title("左侧按钮")
        @Description("左侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var leftButton = 0

        @Bindable
        @Title("提示图标")
        @Description("附加在文字左侧的图标，资源ID类型，不能点击")
        @Style(IconStyle::class)
        var icon = 0

        @Bindable
        @Title("右侧按钮")
        @Description("右侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var rightButton = 0

        init {
            maxWidth = -100
        }

        override fun buttonClick(view: View) {
            showTip(view, fragment.component.id() == R.id.component_xhb_toasts)
        }

        private fun showTip(view: View, toast: Boolean) {
            val binding = XhbToastTipViewBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            val index = (view.parent.parent as ViewGroup).indexOfChild(view.parent as View)
            if (toast) {
                binding.tipView.location = XHBTipView.Location.AutoToast
                if (index == 0)
                    binding.tipView.popAt(view, fragment)
                else
                    binding.tipView.popUp(Toast.LENGTH_SHORT)
            } else {
                binding.tipView.location = XHBTipView.Location.ManualLayout
                val target = if (index == 0) (view.rootView as ViewGroup).getChildAt(0) else fragment.binding.root
                binding.tipView.popAt(target, fragment)
            }
        }
    }

    class ItemBinding(private val styles: Styles) : UnitTypeItemBinding<Any>(R.layout.xhb_tip_view_button_item) {
        override fun bindView(binding: ViewDataBinding, item: Any?, position: Int) {
            super.bindView(binding, item, position)
            binding.setVariable(BR.styles, styles)
            when (styles.fragment.component.id()) {
                R.id.component_xhb_snack_bars -> {
                    (binding as XhbTipViewButtonItemBinding).button.text =
                        if (position == 0) "根视图" else "展示区域"
                }
                R.id.component_xhb_toasts -> {
                    (binding as XhbTipViewButtonItemBinding).button.text =
                        if (position == 0) "普通View" else "系统Toast"
                }
            }
        }
    }

    override fun tipViewButtonClicked(view: XHBTipView, btnId: Int) {
        val tip = XHBTipView(requireContext(), null)
        val name = resources.getResourceEntryName(btnId)
        tip.message = "点击了按钮${name}"
        tip.popAt(view)
    }

    override fun createStyle(): Styles {
        return if (component.id() == R.id.component_xhb_tool_tips) ToolStyles(this) else ToastStyles(this)
    }

    companion object {
        private const val TAG = "XHBToolTipFragment"
    }
}