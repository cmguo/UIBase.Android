package com.eazy.uibase.demo.components.basic

import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.CheckBoxItemBinding
import com.eazy.uibase.demo.databinding.CompoundButtonFragmentBinding
import com.eazy.uibase.demo.databinding.RadioButtonItemBinding
import com.eazy.uibase.view.list.PaddingDecoration
import com.eazy.uibase.view.list.UnitTypeItemBinding
import com.eazy.uibase.widget.ZCheckBox

class ZCompoundButtonFragment : ComponentFragment<CompoundButtonFragmentBinding?, ZCompoundButtonFragment.Model?, ZCompoundButtonFragment.Styles?>() {

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

    class Model(fragment: ZCompoundButtonFragment) : ViewModel() {

        var states: List<StateItem> = when (fragment.component.id()) {
            R.id.component_z_check_boxes -> ZCheckBox.CheckedState.values().asList().map {StateItem(it) }
            else -> arrayListOf(false, true).map { StateItem(it) }
        }

        @Bindable
        var allCheckedState: ZCheckBox.CheckedState = ZCheckBox.CheckedState.HalfChecked
            set(value) {
                if (field == value)
                    return
                field = value
                notifyPropertyChanged(BR.allCheckedState)
                if (value == ZCheckBox.CheckedState.HalfChecked)
                    return
                for (s in states) {
                    s.state = value
                }
            }

        fun updateAllCheckedState() {
            allCheckedState = when (states.filter { s -> s.state == ZCheckBox.CheckedState.FullChecked }.size) {
                0 -> ZCheckBox.CheckedState.NotChecked
                3 -> ZCheckBox.CheckedState.FullChecked
                else -> ZCheckBox.CheckedState.HalfChecked
            }
        }

    }

    class Styles(private val fragment: ZCompoundButtonFragment) : ViewStyles() {

        var itemBinding: ItemBinding = ItemBinding(this, when (fragment.component.id()) {
            R.id.component_z_check_boxes -> R.layout.check_box_item
            R.id.component_z_radio_buttons -> R.layout.radio_button_item
            R.id.component_z_switch_buttons -> R.layout.switch_button_item
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

        val checkBoxCheckedStateChanged = object : ZCheckBox.OnCheckedStateChangeListener {
            override fun onCheckedStateChanged(checkBox: ZCheckBox, state: ZCheckBox.CheckedState) {
                fragment.model.updateAllCheckedState()
            }
        }

        // simulate radio group
        val radioCheckedChanged = CompoundButton.OnCheckedChangeListener { view: View, isChecked: Boolean ->
            if (isChecked) {
                val binding: RadioButtonItemBinding = DataBindingUtil.findBinding(view.parent as ViewGroup)!!
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
        private const val TAG = "ZCompoundButtonFragment"
    }
}