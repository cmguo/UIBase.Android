package com.xhb.uibase.demo.components.basic

import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.databinding.XhbCompoundButtonFragmentBinding
import com.xhb.uibase.demo.databinding.XhbRadioButtonItemBinding
import com.xhb.uibase.view.list.PaddingDecoration
import com.xhb.uibase.view.list.UnitTypeItemBinding
import com.xhb.uibase.widget.XHBCheckBox

class XHBCompoundButtonFragment : ComponentFragment<XhbCompoundButtonFragmentBinding?, XHBCompoundButtonFragment.Model?, XHBCompoundButtonFragment.Styles?>() {

    class StateItem(state: Any) : ViewModel() {
        @Bindable
        var state = state
            set(value) {
                if (field == value)
                    return
                field = value
                notifyPropertyChanged(BR.state)
            }
    }

    class Model(fragment: XHBCompoundButtonFragment) : ViewModel() {
        var states: List<StateItem> = when (fragment.component.id()) {
            R.id.component_xhb_check_boxes -> XHBCheckBox.CheckedState.values().asList().map {StateItem(it) }
            else -> arrayListOf(false, true).map { StateItem(it) }
        }
    }

    class Styles(private val fragment: XHBCompoundButtonFragment) : ViewStyles() {

        var itemBinding: ItemBinding = ItemBinding(this, when (fragment.component.id()) {
            R.id.component_xhb_check_boxes -> R.layout.xhb_check_box_item
            R.id.component_xhb_radio_buttons -> R.layout.xhb_radio_button_item
            R.id.component_xhb_switch_buttons -> R.layout.xhb_switch_button_item
            else -> 0})
        var itemDecoration: RecyclerView.ItemDecoration = PaddingDecoration()

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        var disabled = false

        @Bindable
        @Title("文字")
        @Description("改变文字，按钮会自动适应文字宽度")
        var text = "文字"

        // simulate radio group
        val radioCheckedChange = CompoundButton.OnCheckedChangeListener { view: View, isChecked: Boolean ->
            if (isChecked) {
                val binding: XhbRadioButtonItemBinding = DataBindingUtil.findBinding(view.parent as ViewGroup)!!
                for (item in fragment.model.states) {
                    if (item != binding.data) {
                        item.state = false
                    }
                }
            }
        }
    }

    class ItemBinding(private val styles: Styles, layoutId: Int) : UnitTypeItemBinding<StateItem>(layoutId) {
        override fun bindView(binding: ViewDataBinding, item: StateItem, position: Int) {
            super.bindView(binding, item, position)
            binding.setVariable(BR.styles, styles)
        }
    }

    companion object {
        private const val TAG = "XHBCompoundButtonFragment"
    }
}