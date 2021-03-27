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
import com.xhb.uibase.demo.core.style.DimemDpStyle
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.core.style.annotation.Title
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
        @Title("宽度模式")
        @Description("指示器的宽度模式，可用于线段指示器、圆角指示器；有适应边界（MatchEdge）、适应内容（WrapContent)、外部指定（Exactly）三种模式")
        var widthMode = XHBLineIndicator.WidthMode.Exactly

        @Bindable
        @Title("线段宽度")
        @Description("线段指示器的宽度，仅在外部指定（Exactly）有意义")
        @Style(DimemDpStyle::class)
        var lineWidth = 24f

        @Bindable
        @Title("线段高度")
        @Description("线段指示器的高度")
        @Style(DimemDpStyle::class)
        var lineHeight = 4f

        @Bindable
        @Title("横向偏移")
        @Description("线段指示器的横行偏移，实际是padding，向内偏移")
        @Style(DimemDpStyle::class)
        var offsetX = 0f

        @Bindable
        @Title("纵向偏移")
        @Description("线段指示器的纵行偏移，从底部向上的偏移")
        @Style(DimemDpStyle::class)
        var offsetY = 0f

        val roundIndicator1 = createRoundIndicator(fragment, this)

        @Bindable
        @Title("圆角尺寸")
        @Description("圆角指示器的圆角半径")
        @Style(DimemDpStyle::class)
        var borderRadius = 8f

        @Bindable
        @Title("横向填充")
        @Description("圆角指示器的横向填充大小，在外部指定（Exactly），用作实际宽度")
        @Style(DimemDpStyle::class)
        var paddingX = 7

        @Bindable
        @Title("纵向填充")
        @Description("圆角指示器的纵向填充大小，在外部指定（Exactly），用作实际高度")
        @Style(DimemDpStyle::class)
        var paddingY = -1

        val itemBinding1 = ItemBinding(fragment.requireContext(), this, 0)

        val itemBinding2 = ItemBinding(fragment.requireContext(), this, 240)

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

    class ItemBinding(context: Context, private val styles: Styles, private val width: Int) : UnitTypeItemBinding<String?>(context, R.layout.xhb_tab_title) {
        override fun bindView(binding: ViewDataBinding?, item: String?, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.styles, styles)
            if (width > 0) {
                val lp = binding.root.layoutParams
                lp.width = width
                binding.root.layoutParams = lp
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