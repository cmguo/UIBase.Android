package com.eazy.uibase.demo.components.basic

import android.os.Bundle
import android.util.Log
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
import com.eazy.uibase.demo.core.SkinManager
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.CompoundButtonFragmentBinding
import com.eazy.uibase.demo.databinding.RadioButtonItemBinding
import com.eazy.uibase.view.list.PaddingDecoration
import com.eazy.uibase.view.list.RecyclerViewAdapter
import com.eazy.uibase.view.list.UnitTypeItemBinding
import com.eazy.uibase.widget.ZCheckBox
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class ZCompoundButtonFragment : ComponentFragment<CompoundButtonFragmentBinding?, ZCompoundButtonFragment.Model?, ZCompoundButtonFragment.Styles?>(), SkinObserver {

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

    class Model(fragment: ZCompoundButtonFragment?) : ViewModel() {
        var states: List<StateItem>
        init {
            states = when (fragment!!.component.id()) {
                R.id.component_z_check_boxes -> ZCheckBox.CheckedState.values().asList().map {StateItem(it) }
                else -> arrayListOf(false, true).map { StateItem(it) }
            }
        }
    }

    class Styles(fragment: ZCompoundButtonFragment?) : ViewStyles() {
        var itemBinding: ItemLayout
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
                val binding: RadioButtonItemBinding = DataBindingUtil.findBinding(view.parent as ViewGroup)!!
                for (item in fragment_!!.model!!.states) {
                    if (item != binding.data) {
                        item.state = false
                    }
                }
            }
        }

        private val fragment_ = fragment

        init {
            itemBinding = ItemLayout(this)
        }

        val itemBindingId: Int
            get() = when (fragment_!!.component.id()) {
                R.id.component_z_check_boxes -> R.layout.check_box_item
                R.id.component_z_radio_buttons -> R.layout.radio_button_item
                R.id.component_z_switch_buttons -> R.layout.switch_button_item
                else -> 0
            }
    }

    class ItemLayout(private val styles: Styles) : UnitTypeItemBinding<StateItem>(styles.itemBindingId) {
        override fun bindView(binding: ViewDataBinding?, item: StateItem, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.styles, styles)
        }
    }

    // this should be in view model, but fragment may simplify things
    val itemClicked = RecyclerViewAdapter.OnItemClickListener<Any> { position, `object` ->
        Log.d(TAG, "itemClicked$`object`")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SkinManager.addObserver(this)
    }

    override fun onDestroy() {
        SkinManager.removeObserver(this)
        super.onDestroy()
    }

    override fun updateSkin(observable: SkinObservable, o: Any) {
        binding!!.compoundButtonList.adapter!!.notifyItemRangeChanged(0, model!!.states.size)
    }

    companion object {
        private const val TAG = "ZCompoundButtonFragment"
    }
}