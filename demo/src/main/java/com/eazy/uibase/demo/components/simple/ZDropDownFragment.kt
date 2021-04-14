package com.eazy.uibase.demo.components.simple

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.DimemDpStyle
import com.eazy.uibase.demo.core.style.IconStyle
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.DropDownFragmentBinding
import com.eazy.uibase.demo.databinding.DropDownBinding
import com.eazy.uibase.view.list.UnitTypeItemBinding
import com.eazy.uibase.widget.ZDropDown
import com.eazy.uibase.widget.ZTipView

class ZDropDownFragment : ComponentFragment<DropDownFragmentBinding?, ZDropDownFragment.Model?, ZDropDownFragment.Styles?>(), ZDropDown.DropDownListener {

    class Model : ViewModel() {

        var buttons = arrayOfNulls<Any>(24).toList()

        val titles = arrayListOf("菜单项目1", "菜单项目2", "菜单项目3", "菜单项目4", "菜单项目5")
        val icons = IconStyle.icons.toList()
    }

    class Styles(private val fragment: ZDropDownFragment) : ViewStyles() {

        val layoutManager = GridLayoutManager(fragment.context, 4)

        val itemBinding: ItemBinding = ItemBinding(this)

        @Bindable
        @Title("宽度")
        @Description("整体宽度，设置负数，则自动加上窗口宽度，设置为 0，自动计算宽度；通过 layout_width 或者 minimumWidth 设置")
        @Style(DimemDpStyle::class)
        var width = 200

        fun buttonClick(view: View) {
            val binding = DropDownBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.model = fragment.model
            binding.executePendingBindings()
            if (width > 0) {
                binding.dropDown.minimumWidth = width
            }
            binding.dropDown.popAt(view, fragment)
        }
    }

    class ItemBinding(private val styles: Styles) : UnitTypeItemBinding<Any>(R.layout.drop_down_button_item) {
        override fun bindView(binding: ViewDataBinding, item: Any?, position: Int) {
            super.bindView(binding, item, position)
            binding.setVariable(BR.styles, styles)
        }
    }

    override fun dropDownFinished(dropDown: ZDropDown, selection: Int?) {
        val tip = ZTipView(requireContext(), null)
        tip.message = "选择了项目${selection}"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(requireView())
    }

    companion object {
        private const val TAG = "ZDropDownFragment"
    }
}