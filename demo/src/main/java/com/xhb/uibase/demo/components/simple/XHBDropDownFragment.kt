package com.xhb.uibase.demo.components.simple

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.GridLayoutManager
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.DimemDpStyle
import com.xhb.uibase.demo.core.style.IconStyle
import com.xhb.uibase.demo.core.style.annotation.*
import com.xhb.uibase.demo.databinding.XhbDropDownFragmentBinding
import com.xhb.uibase.demo.databinding.XhbDropDownBinding
import com.xhb.uibase.view.list.UnitTypeItemBinding
import com.xhb.uibase.widget.XHBDropDown
import com.xhb.uibase.widget.XHBTipView

class XHBDropDownFragment : ComponentFragment<XhbDropDownFragmentBinding?, XHBDropDownFragment.Model?, XHBDropDownFragment.Styles?>(), XHBDropDown.DropDownListener {

    class Model : ViewModel() {

        var buttons = arrayOfNulls<Any>(24).toList()

        val titles = arrayListOf("菜单项目1", "菜单项目2", "菜单项目3", "菜单项目4", "菜单项目5")
        val icons = IconStyle.icons.toList()
    }

    class Styles(private val fragment: XHBDropDownFragment) : ViewStyles() {

        val layoutManager = GridLayoutManager(fragment.context, 4)

        val itemBinding: ItemBinding = ItemBinding(this)

        @Bindable
        @Title("宽度")
        @Description("整体宽度，设置负数，则自动加上窗口宽度，设置为 0，自动计算宽度；通过 layout_width 或者 minimumWidth 设置")
        @Style(DimemDpStyle::class)
        var width = 200

        fun buttonClick(view: View) {
            val binding = XhbDropDownBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.model = fragment.model
            binding.executePendingBindings()
            if (width > 0) {
                binding.dropDown.minimumWidth = width
            }
            binding.dropDown.popAt(view, fragment)
        }
    }

    class ItemBinding(private val styles: Styles) : UnitTypeItemBinding<Any>(R.layout.xhb_drop_down_button_item) {
        override fun bindView(binding: ViewDataBinding?, item: Any?, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.styles, styles)
        }
    }

    override fun dropDownFinished(dropDown: XHBDropDown, selection: Int?) {
        val tip = XHBTipView(requireContext(), null)
        tip.message = "选择了项目${selection}"
        tip.location = XHBTipView.Location.AutoToast
        tip.popAt(requireView())
    }

    companion object {
        private const val TAG = "XHBDropDownFragment"
    }
}