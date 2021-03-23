package com.xhb.uibase.demo.components.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.GridLayoutManager
import com.xhb.uibase.binding.RecyclerViewAdapter
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.IconStyle
import com.xhb.uibase.demo.core.style.annotation.*
import com.xhb.uibase.demo.databinding.XhbTipViewFragmentBinding
import com.xhb.uibase.demo.databinding.XhbToastTipViewBinding
import com.xhb.uibase.demo.databinding.XhbToolTipViewBinding
import com.xhb.uibase.widget.XHBButton
import com.xhb.uibase.widget.XHBTipView

class XHBTipViewFragment : ComponentFragment<XhbTipViewFragmentBinding?, XHBTipViewFragment.Model?, XHBTipViewFragment.Styles?>(), XHBTipView.TipViewListener {

    class Model(fragment: XHBTipViewFragment) : ViewModel() {

        var buttons = arrayOfNulls<Any>(when (fragment.component.id()) {
            R.id.component_xhb_tool_tips -> 24
            R.id.component_xhb_snack_bars -> 2
            else -> 1 }).toList()

        var tipButton: XHBButton = {
            val button = XHBButton(fragment.context!!)
            button.buttonType = XHBButton.ButtonType.Text
            button.buttonSize = XHBButton.ButtonSize.Small
            button.height = 60
            button.icon = R.drawable.ic_plus
            button.iconAtRight = true
            button.text = "去查看"
            button
        } ()
    }

    open class Styles(protected val fragment: XHBTipViewFragment) : ViewStyles() {

        val layoutManager = GridLayoutManager(fragment.context, 4)

        val itemBinding: ItemLayout

        @Bindable
        @Title("最大宽度")
        @Description("整体最大宽度，设置负数，则自动加上窗口宽度")
        var maxWidth = 600

        @Bindable
        @Title("单行文字")
        @Description("限制单行文字，默认是多行的")
        var singleLine = false

        @Bindable
        @Title("消息内容")
        @Description("自适应消息内容的宽度和高度")
        var message = "你点击了按钮"

        init {
            itemBinding = ItemLayout(this)
        }

        open fun buttonClick(view: View) {}
    }

    class ToolStyles(fragment: XHBTipViewFragment) : Styles(fragment) {

        @Bindable
        @Title("建议位置")
        @Description("建议弹出位置，如果左右或者上下超过窗口区域，则会分别自动调整为其他适当位置")
        var location = XHBTipView.Location.TopRight

        @Bindable
        @Title("右侧图标")
        @Description("右侧显示的图标，资源ID类型，一般用于关闭，也可以自定义其他行为")
        @Style(IconStyle::class)
        var rightIcon = 0

        override fun buttonClick(view: View) {
            val binding = XhbToolTipViewBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            binding.tipView.popAt(view, fragment)
        }
    }

    class ToastStyles(fragment: XHBTipViewFragment) : Styles(fragment) {

        @Bindable
        @Title("左侧图标")
        @Description("左侧显示的图标，资源ID类型，一般用于关闭，也可以自定义其他行为")
        @Style(IconStyle::class)
        var leftIcon = 0

        @Bindable
        @Title("提示图标")
        @Description("附加在文字左侧的图标，资源ID类型，不能点击")
        @Style(IconStyle::class)
        var icon = 0

        @Bindable
        @Title("右侧图标")
        @Description("右侧显示的图标，资源ID类型，一般用于关闭，也可以自定义其他行为")
        @Style(IconStyle::class)
        var rightIcon = 0

        @Bindable
        @Title("附加按钮")
        @Description("右侧附加的按钮，类型为 View，按钮点击的具体行为由使用者定义")
        @Values("<null>", "text")
        var button = "<null>"

        init {
            maxWidth = -100
            fragment.model!!.tipButton.setOnClickListener { view ->
                (view.parent as XHBTipView).dismiss()
                showTip(fragment.requireView(), true)
            }
        }

        override fun buttonClick(view: View) {
            showTip(view, fragment.component.id() == R.id.component_xhb_toasts)
        }

        private fun showTip(view: View, toast: Boolean) {
            val binding = XhbToastTipViewBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            binding.tipView.button = if ("text" == button) fragment.model!!.tipButton else null
            if (toast) {
                binding.tipView.location = XHBTipView.Location.AutoToast
                binding.tipView.popAt(view, fragment)
            } else {
                binding.tipView.location = XHBTipView.Location.ManualLayout
                val index = (view.parent.parent as ViewGroup).indexOfChild(view.parent as View)
                val target = if (index == 0) (view.rootView as ViewGroup).getChildAt(0) else fragment.binding!!.root
                binding.tipView.popAt(target, fragment)
            }
        }
    }

    class ItemLayout(private val styles: Styles) : RecyclerViewAdapter.UnitTypeItemBinding<Any>(R.layout.xhb_tip_view_button_item) {
        override fun bindView(binding: ViewDataBinding?, item: Any?, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.styles, styles)
        }
    }

    override fun tipViewIconTapped(view: XHBTipView, index: Int) {
        val tip = XHBTipView(requireContext(), null)
        tip.message = "点击了图标${index}"
        tip.popAt(view)
    }

    override fun createStyle(): Styles {
        return if (component.id() == R.id.component_xhb_tool_tips) ToolStyles(this) else ToastStyles(this)
    }

    companion object {
        private const val TAG = "XHBToolTipFragment"
    }
}