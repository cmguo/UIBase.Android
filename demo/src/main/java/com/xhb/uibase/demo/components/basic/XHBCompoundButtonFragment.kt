package com.xhb.uibase.demo.components.basic

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.xhb.uibase.binding.RecyclerViewAdapter
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.SkinManager
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.annotation.*
import com.xhb.uibase.demo.databinding.XhbCompoundButtonFragmentBinding
import com.xhb.uibase.demo.view.recycler.PaddingDecoration
import com.xhb.uibase.widget.XHBCheckBox
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class XHBCompoundButtonFragment : ComponentFragment<XhbCompoundButtonFragmentBinding?, XHBCompoundButtonFragment.Model?, XHBCompoundButtonFragment.Styles?>(), SkinObserver {

    class Model(fragment: XHBCompoundButtonFragment?) : ViewModel() {
        var states: List<Any?>
        init {
            states = when (fragment!!.component.id()) {
                R.id.component_xhb_check_boxes -> XHBCheckBox.CheckedState.values().asList()
                else -> arrayListOf(false, true)
            }
        }
    }

    class Styles(fragment: XHBCompoundButtonFragment?) : ViewStyles() {
        var itemLayout: ItemLayout
        var itemDecoration: RecyclerView.ItemDecoration = PaddingDecoration()

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        var disabled = false

        @Bindable
        @Title("文字")
        @Description("改变文字，按钮会自动适应文字宽度")
        var text = "文字"

        fun testCompoundButtonClick(view: View) {

        }

        private val fragment_ = fragment

        init {
            itemLayout = ItemLayout(this)
        }

        val itemLayoutId: Int
            get() = when (fragment_!!.component.id()) {
                R.id.component_xhb_check_boxes -> R.layout.xhb_check_box_item
                R.id.component_xhb_radio_buttons -> R.layout.xhb_radio_button_item
                R.id.component_xhb_switch_buttons -> R.layout.xhb_switch_button_item
                else -> 0
            }
    }

    class ItemLayout(private val styles: Styles) : RecyclerViewAdapter.UnitTypeItemLayout<Any>(styles.itemLayoutId) {
        override fun bindView(binding: ViewDataBinding?, item: Any, position: Int) {
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
        private const val TAG = "XHBCompoundButtonFragment"
    }
}