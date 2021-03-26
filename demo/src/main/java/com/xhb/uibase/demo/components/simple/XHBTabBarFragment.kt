package com.xhb.uibase.demo.components.simple

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.databinding.XhbLineIndicatorBinding
import com.xhb.uibase.demo.databinding.XhbRoundIndicatorBinding
import com.xhb.uibase.demo.databinding.XhbTabBarFragmentBinding
import com.xhb.uibase.view.list.UnitTypeItemBinding
import com.xhb.uibase.widget.XHBDropDown
import com.xhb.uibase.widget.XHBTipView
import com.xhb.uibase.widget.tabbar.XHBLineIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator

class XHBTabBarFragment : ComponentFragment<XhbTabBarFragmentBinding?, XHBTabBarFragment.Model?, XHBTabBarFragment.Styles?>(), XHBDropDown.DropDownListener {

    class Model : ViewModel() {

        val titles1 = arrayOf("语文", "数学", "英语").toList()

        val titles2 = arrayListOf("项目1", "项目2", "项目3", "项目4", "项目5",
            "项目6", "项目7", "项目8", "项目9", "项目10", "项目11", "项目12", "项目13", "项目14", "项目15")

    }

    class Styles(private val fragment: XHBTabBarFragment) : ViewStyles() {

        companion object {
            // can't share
            fun createLineIndicator(fragment: XHBTabBarFragment, styles: Styles): IPagerIndicator {
                val binding = XhbLineIndicatorBinding.inflate(LayoutInflater.from(fragment.context))
                binding.styles = styles
                return binding.lineIndicator
            }
            fun createRoundIndicator(fragment: XHBTabBarFragment, styles: Styles): IPagerIndicator {
                val binding = XhbRoundIndicatorBinding.inflate(LayoutInflater.from(fragment.context))
                binding.styles = styles
                return binding.lineIndicator
            }
        }

        val lineIndicator1 = createLineIndicator(fragment, this)
        val lineIndicator2 = createLineIndicator(fragment, this)
        val lineIndicator3 = createLineIndicator(fragment, this)

        @Bindable
        var widthMode = XHBLineIndicator.WidthMode.Exactly

        @Bindable
        var lineWidth = 40f

        @Bindable
        var lineHeight = 8f

        @Bindable
        var offsetX = 8f

        @Bindable
        var offsetY = 8f

        val roundIndicator1 = createRoundIndicator(fragment, this)

        @Bindable
        var borderRadius = 16f

        @Bindable
        var paddingX = 60

        @Bindable
        var paddingY = 16

        val itemBinding1 = ItemBinding(fragment.requireContext(), this, false)

        val itemBinding2 = ItemBinding(fragment.requireContext(), this, true)

        val pagerTemplates = mutableMapOf<Class<*>, Class<out Fragment>>()

        init {
            pagerTemplates[String::class.java] = PageFragment::class.java
        }

    }

    class PageFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.tab_title, container, false)
            (view as TextView).text = arguments?.getString("data")
            return view
        }
    }

    class ItemBinding(context: Context, private val styles: Styles, private val padding: Boolean) : UnitTypeItemBinding<String?>(context, R.layout.xhb_tab_title) {
        override fun bindView(binding: ViewDataBinding?, item: String?, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.styles, styles)
            if (padding) {
                binding.root.setPadding(60, 0, 60, 0)
            }
        }
    }

    override fun dropDownFinished(selection: Int?) {
        val tip = XHBTipView(requireContext(), null)
        tip.message = "选择了项目${selection}"
        tip.location = XHBTipView.Location.AutoToast
        tip.popAt(requireView())
    }

    companion object {
        private const val TAG = "XHBTabFragment"
    }
}