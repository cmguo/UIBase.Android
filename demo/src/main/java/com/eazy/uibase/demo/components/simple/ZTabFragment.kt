package com.eazy.uibase.demo.components.simple

import android.content.Context
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.TabFragmentBinding
import com.eazy.uibase.view.list.UnitTypeItemBinding
import com.eazy.uibase.widget.ZDropDown
import com.eazy.uibase.widget.ZTipView
import com.eazy.uibase.widget.tabs.ZLineIndicator

class ZTabFragment : ComponentFragment<TabFragmentBinding?, ZTabFragment.Model?, ZTabFragment.Styles?>(), ZDropDown.DropDownListener {

    class Model : ViewModel() {

        val titles = arrayOf("项目1", "项目2", "项目3", "项目4", "项目5").toList()

        val titles2 = arrayListOf("项目1", "项目2", "项目3", "项目4", "项目5",
            "项目6", "项目7", "项目8", "项目9", "项目10", "项目11", "项目12", "项目13", "项目14", "项目15")
    }

    class Styles(private val fragment: ZTabFragment) : ViewStyles() {

        val lineIndicator = R.layout.line_indicator

        @Bindable
        var widthMode = ZLineIndicator.WidthMode.Exactly

        @Bindable
        var lineWidth = 40f

        @Bindable
        var lineHeight = 8f


        val itemBinding = ItemBinding(fragment.requireContext(), this)

        val pagerTemplates = mutableMapOf<Class<*>, Class<out Fragment>>()

        init {
            pagerTemplates[String::class.java] = Fragment::class.java
        }

    }

    class ItemBinding(context: Context, private val styles: Styles) : UnitTypeItemBinding<String?>(context, R.layout.tab_title) {
        override fun bindView(binding: ViewDataBinding?, item: String?, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.styles, styles)
        }
    }

    override fun dropDownFinished(selection: Int?) {
        val tip = ZTipView(requireContext(), null)
        tip.message = "选择了项目${selection}"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(requireView())
    }

    companion object {
        private const val TAG = "ZTabFragment"
    }
}