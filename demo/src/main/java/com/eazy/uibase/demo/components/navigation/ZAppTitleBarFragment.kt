package com.eazy.uibase.demo.components.navigation

import androidx.databinding.Bindable
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ContentStyle
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.AppTitleBarFragmentBinding
import com.eazy.uibase.widget.ZAppTitleBar
import com.eazy.uibase.widget.ZTipView

class ZAppTitleBarFragment : ComponentFragment<AppTitleBarFragmentBinding?, ZAppTitleBarFragment.Model?, ZAppTitleBarFragment.Styles?>()
    , ZAppTitleBar.TitleBarListener {

    class Model : ViewModel()

    class Styles : ViewStyles() {

        @Bindable
        @Title("左侧按钮")
        @Description("左侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var leftButton = R.drawable.icon_left

        @Bindable
        @Title("右侧按钮")
        @Description("右侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var rightButton = R.drawable.icon_more

        @Bindable
        @Title("右侧按钮2")
        @Description("右侧第2个按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var rightButton2 = 0

        @Bindable
        @Title("内容")
        @Description("中间或者整体内容，资源ID：布局（layout，中间内容）或者样式（style，整体内容）")
        @Style(ContentStyle::class, params = ["<title>"])
        var content = 0

        @Bindable
        @Title("数据")
        @Description("通过 BindingAdapter 实现的虚拟属性，间接设置给扩展内容，仅用于基于 DataBinding 的布局")
        @Values(value = ["1", "2", "xxx"])
        var data: String = ""

        @Bindable
        @Title("图标")
        @Description("附加在文字标题左侧的图标，资源ID类型，不能点击")
        @Style(ContentStyle::class, params = ["@drawable", "@layout"])
        var icon = 0

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
        private const val TAG = "ZAppTitleBarFragment"
    }

    override fun backgroundColor(): Int {
        return R.color.blue_100
    }

    override fun titleBarButtonClicked(bar: ZAppTitleBar, btnId: Int) {
        val tip = ZTipView(requireContext(), null)
        val name = resources.getResourceEntryName(btnId)
        tip.message = "点击了按钮: ${name}"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(requireView())
    }
}