package com.eazy.uibase.demo.components.basic

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.binding.RecyclerViewAdapter
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.SkinManager
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.annotation.Description
import com.eazy.uibase.demo.core.annotation.Title
import com.eazy.uibase.demo.databinding.CheckBoxFragmentBinding
import com.eazy.uibase.demo.view.recycler.PaddingDecoration
import com.eazy.uibase.widget.ZCheckBox
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class ZCheckBoxFragment : ComponentFragment<CheckBoxFragmentBinding?, ZCheckBoxFragment.Model?, ZCheckBoxFragment.Style?>(), SkinObserver {
    class Model(fragment: ZCheckBoxFragment?) : ViewModel() {
        var states = ZCheckBox.CheckedState.values().asList()
    }

    class Style : ViewStyles() {
        var itemLayout = CheckBoxItemLayout(this)
        var itemDecoration: RecyclerView.ItemDecoration = PaddingDecoration()

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        var disabled = false

        @Bindable
        @Title("文字")
        @Description("改变文字，按钮会自动适应文字宽度")
        var text = "复选框"

        fun testCheckBoxClick(view: View) {

        }
    }

    class CheckBoxItemLayout(private val style: Style) : RecyclerViewAdapter.UnitTypeItemLayout<ZCheckBox.CheckedState>(R.layout.check_box_item) {
        override fun bindView(binding: ViewDataBinding?, item: ZCheckBox.CheckedState, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.style, style)
        }
    }

    // this should be in view model, but fragment may simplify things
    val itemClicked = RecyclerViewAdapter.OnItemClickListener<Any> { position, `object` ->
        Log.d(TAG, "itemClicked$`object`")
    }

    fun onClick(view: View?) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SkinManager.addObserver(this)
    }

    override fun onDestroy() {
        SkinManager.removeObserver(this)
        super.onDestroy()
    }

    override fun updateSkin(observable: SkinObservable, o: Any) {
        binding!!.checkBoxList.adapter!!.notifyItemRangeChanged(0, model!!.states.size)
    }

    companion object {
        private const val TAG = "ZCheckBoxFragment"
    }
}