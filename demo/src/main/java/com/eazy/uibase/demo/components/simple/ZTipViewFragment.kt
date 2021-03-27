package com.eazy.uibase.demo.components.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.GridLayoutManager
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.DimemDpStyle
import com.eazy.uibase.demo.core.style.IconStyle
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.TipViewFragmentBinding
import com.eazy.uibase.demo.databinding.ToastTipViewBinding
import com.eazy.uibase.demo.databinding.ToolTipViewBinding
import com.eazy.uibase.view.list.UnitTypeItemBinding
import com.eazy.uibase.widget.ZButton
import com.eazy.uibase.widget.ZTipView

class ZTipViewFragment : ComponentFragment<TipViewFragmentBinding?, ZTipViewFragment.Model?, ZTipViewFragment.Styles?>(), ZTipView.TipViewListener {

    class Model(fragment: ZTipViewFragment) : ViewModel() {

        var buttons = arrayOfNulls<Any>(when (fragment.component.id()) {
            R.id.component_z_tool_tips -> 24
            R.id.component_z_snack_bars -> 2
            else -> 1 }).toList()

        var tipButton: ZButton = {
            val button = ZButton(fragment.context!!)
            button.buttonType = ZButton.ButtonType.Text
            button.buttonSize = ZButton.ButtonSize.Small
            button.height = 60
            button.icon = R.drawable.icon_plus
            button.iconAtRight = true
            button.text = "去查看"
            button
        } ()
    }

    open class Styles(protected val fragment: ZTipViewFragment) : ViewStyles() {

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

    class ToolStyles(fragment: ZTipViewFragment) : Styles(fragment) {

        @Bindable
        @Title("建议位置")
        @Description("建议弹出位置，如果左右或者上下超过窗口区域，则会分别自动调整为其他适当位置")
        var location = ZTipView.Location.TopRight

        @Bindable
        @Title("右侧图标")
        @Description("右侧显示的图标，资源ID类型，一般用于关闭，也可以自定义其他行为")
        @Style(IconStyle::class)
        var rightIcon = 0

        override fun buttonClick(view: View) {
            val binding = ToolTipViewBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            binding.tipView.popAt(view, fragment)
        }
    }

    class ToastStyles(fragment: ZTipViewFragment) : Styles(fragment) {

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
                (view.parent as ZTipView).dismiss()
                showTip(fragment.requireView(), true)
            }
        }

        override fun buttonClick(view: View) {
            showTip(view, fragment.component.id() == R.id.component_z_toasts)
        }

        private fun showTip(view: View, toast: Boolean) {
            val binding = ToastTipViewBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            binding.tipView.button = if ("text" == button) fragment.model!!.tipButton else null
            if (toast) {
                binding.tipView.location = ZTipView.Location.AutoToast
                binding.tipView.popAt(view, fragment)
            } else {
                binding.tipView.location = ZTipView.Location.ManualLayout
                val index = (view.parent.parent as ViewGroup).indexOfChild(view.parent as View)
                val target = if (index == 0) (view.rootView as ViewGroup).getChildAt(0) else fragment.binding!!.root
                binding.tipView.popAt(target, fragment)
            }
        }
    }

    class ItemBinding(private val styles: Styles) : UnitTypeItemBinding<Any>(R.layout.tip_view_button_item) {
        override fun bindView(binding: ViewDataBinding?, item: Any?, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.styles, styles)
        }
    }

    override fun tipViewIconTapped(view: ZTipView, index: Int) {
        val tip = ZTipView(requireContext(), null)
        tip.message = "点击了图标${index}"
        tip.popAt(view)
    }

    override fun createStyle(): Styles {
        return if (component.id() == R.id.component_z_tool_tips) ToolStyles(this) else ToastStyles(this)
    }

    companion object {
        private const val TAG = "ZToolTipFragment"
    }
}