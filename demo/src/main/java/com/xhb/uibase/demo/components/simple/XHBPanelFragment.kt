package com.xhb.uibase.demo.components.simple

import android.graphics.Color
import android.view.View
import androidx.databinding.Bindable
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.ContentStyle
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.core.style.annotation.Values
import com.xhb.uibase.demo.databinding.XhbPanelFragmentBinding
import com.xhb.uibase.widget.XHBPanel
import com.xhb.uibase.widget.XHBTipView

class XHBPanelFragment : ComponentFragment<XhbPanelFragmentBinding?, XHBPanelFragment.Model?, XHBPanelFragment.Styles?>()
    , XHBPanel.PanelListener {

    class Model : ViewModel()

    class Styles : ViewStyles() {

        @Bindable
        @Title("顶部栏")
        @Description("顶部标题栏栏，可选，参见标题栏的 content 样式")
        @Style(ContentStyle::class, params = ["<title>"])
        var titleBar = 0

        @Bindable
        @Title("底部按钮")
        @Description("底部操作按钮，可选，参见按纽的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var bottomButton = 0

        @Bindable
        @Title("内容")
        @Description("中间或者整体内容，资源ID：布局（layout，中间内容）或者样式（style，整体内容）")
        @Style(ContentStyle::class, params = ["@layout"])
        var content = 0

        @Bindable
        @Title("数据")
        @Description("通过 BindingAdapter 实现的虚拟属性，间接设置给扩展内容，仅用于基于 DataBinding 的布局")
        @Values(value = ["1", "2", "xxx"])
        var data: Any? = null
    }

    companion object {
        private const val TAG = "XHBPanelFragment"
    }

    override fun backgroundColor(): Int {
        return R.color.blue_100
    }

    var buttonClick = View.OnClickListener {
        val panel = XHBPanel(requireContext())
        panel.titleBar = styles.titleBar
        panel.bottomButton = styles.bottomButton
        panel.content = styles.content
        panel.listener = this
        panel.popUp(parentFragmentManager)
    }

    override fun panelButtonClicked(panel: XHBPanel, btnId: Int) {
        val tip = XHBTipView(requireContext(), null)
        val name = resources.getResourceEntryName(btnId)
        tip.message = "点击了按钮${name}"
        tip.location = XHBTipView.Location.AutoToast
        tip.popAt(panel)
    }

    override fun panelDismissed(panel: XHBPanel) {
        val tip = XHBTipView(requireContext(), null)
        tip.message = "面板消失"
        tip.location = XHBTipView.Location.AutoToast
        tip.popAt(requireView())
    }
}