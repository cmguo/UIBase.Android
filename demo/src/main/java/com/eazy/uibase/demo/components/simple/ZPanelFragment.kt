package com.eazy.uibase.demo.components.simple

import android.view.View
import androidx.databinding.Bindable
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ContentStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.PanelFragmentBinding
import com.eazy.uibase.widget.ZPanel
import com.eazy.uibase.widget.ZTipView

class ZPanelFragment : ComponentFragment<PanelFragmentBinding?, ZPanelFragment.Model?, ZPanelFragment.Styles?>()
    , ZPanel.PanelListener {

    class Model : ViewModel()

    class Styles : ViewStyles() {

        @Bindable
        @Title("顶部栏")
        @Description("顶部标题栏栏，可选，参见标题栏的 content 样式")
        @Style(ContentStyle::class, params = ["title"])
        var titleBar = 0

        @Bindable
        @Title("底部按钮")
        @Description("底部操作按钮，可选，参见按纽的 content 样式")
        @Style(ContentStyle::class, params = ["button", "icon", "text"])
        var bottomButton = 0

        @Bindable
        @Title("内容")
        @Description("中间或者整体内容，资源ID：布局（layout，中间内容）或者样式（style，整体内容）")
        @Style(ContentStyle::class, params = ["@layout"])
        var content = 0
    }

    companion object {
        private const val TAG = "ZPanelFragment"
    }

    var buttonClick = View.OnClickListener {
        val panel = ZPanel(requireContext())
        panel.titleBar = styles!!.titleBar
        panel.bottomButton = styles!!.bottomButton
        panel.content = styles!!.content
        panel.listener = this
        panel.popUp()
    }

    override fun panelButtonClicked(panel: ZPanel, viewId: Int) {
        val tip = ZTipView(requireContext(), null)
        val name = resources.getResourceEntryName(viewId)
        tip.message = "点击了按钮${name}"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(requireView())
    }

    override fun panelDismissed(panel: ZPanel) {
        val tip = ZTipView(requireContext(), null)
        tip.message = "面板消失"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(requireView())
    }
}