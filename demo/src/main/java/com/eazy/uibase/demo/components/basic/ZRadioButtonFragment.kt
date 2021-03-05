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
import com.eazy.uibase.demo.databinding.ButtonItem2Binding
import com.eazy.uibase.demo.databinding.RadioButtonFragmentBinding
import com.eazy.uibase.demo.databinding.RadioButtonItemBinding
import com.eazy.uibase.demo.view.recycler.PaddingDecoration
import com.eazy.uibase.widget.ZRadioButton
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class ZRadioButtonFragment : ComponentFragment<RadioButtonFragmentBinding?, ZRadioButtonFragment.Model?, ZRadioButtonFragment.Style?>(), SkinObserver {
    class Model(fragment: ZRadioButtonFragment?) : ViewModel() {
        var states = arrayListOf(false, true)
    }

    class Style : ViewStyles() {
        var itemLayout = RadioButtonItemLayout(this)
        var itemDecoration: RecyclerView.ItemDecoration = PaddingDecoration()

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        var disabled = false

        @Bindable
        @Title("文字")
        @Description("改变文字，按钮会自动适应文字宽度")
        var text = "单选框"

        fun testRadioButtonClick(view: View) {

        }
    }

    class RadioButtonItemLayout(private val style: Style) : RecyclerViewAdapter.UnitTypeItemLayout<Boolean>(R.layout.radio_button_item) {
        override fun bindView(binding: ViewDataBinding?, item: Boolean, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.style, style)
            val radio = (binding as RadioButtonItemBinding)!!.radioButton
            radio.isChecked = item
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
        binding!!.radioButtonList.adapter!!.notifyItemRangeChanged(0, model!!.states.size)
    }

    companion object {
        private const val TAG = "ZRadioButtonFragment"
    }
}