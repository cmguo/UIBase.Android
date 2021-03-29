package com.xhb.uibase.demo.components.simple

import androidx.databinding.Bindable
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.ContentStyle
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.core.style.annotation.TextAppearanceStyle
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.databinding.XhbAppTitleBarFragmentBinding
import com.xhb.uibase.widget.XHBAppTitleBar
import com.xhb.uibase.widget.XHBTipView

class XHBAppTitleBarFragment : ComponentFragment<XhbAppTitleBarFragmentBinding?, XHBAppTitleBarFragment.Model?, XHBAppTitleBarFragment.Styles?>()
    , XHBAppTitleBar.TitleBarListener {

    class Model : ViewModel()

    class Styles : ViewStyles() {

        @Bindable
        @Title("左侧按钮")
        @Description("左侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["button", "icon", "text"])
        var leftButton = 0

        @Bindable
        @Title("右侧按钮")
        @Description("右侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["button", "icon", "text"])
        var rightButton = 0

        @Bindable
        @Title("右侧按钮2")
        @Description("右侧第2个按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["button", "icon", "text"])
        var rightButton2 = 0

        @Bindable
        @Title("内容")
        @Description("中间或者整体内容，资源ID：布局（layout，中间内容）或者样式（style，整体内容）")
        @Style(ContentStyle::class, params = ["@layout"])
        var content = 0

        @Bindable
        @Title("标题")
        @Description("标题文字，一般在中间显示；如果没有左侧按钮内容，则在左侧大标题样式展示")
        var title = "标题"

        @Bindable
        @Title("标题样式")
        @Description("标题样式（android:textAppearance），应用于标题；默认样式会根据位置自动计算设置，所以一般不需要设置")
        @Style(TextAppearanceStyle::class)
        var textAppearance = 0
    }

    companion object {
        private const val TAG = "XHBAppTitleBarFragment"
    }

    override fun titleBarButtonClicked(bar: XHBAppTitleBar, viewId: Int) {
        val tip = XHBTipView(requireContext(), null)
        val name = resources.getResourceEntryName(viewId)
        tip.message = "点击了按钮${name}"
        tip.location = XHBTipView.Location.AutoToast
        tip.popAt(requireView())
    }
}