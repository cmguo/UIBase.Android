package com.xhb.uibase.demo.components.basic

import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.ButtonStyle
import com.xhb.uibase.demo.core.style.ContentStyle
import com.xhb.uibase.demo.core.style.IconStyle
import com.xhb.uibase.demo.core.style.annotation.*
import com.xhb.uibase.demo.databinding.XhbButtonFragmentBinding
import com.xhb.uibase.demo.databinding.XhbButtonItemBinding
import com.xhb.uibase.view.list.PaddingDecoration
import com.xhb.uibase.view.list.UnitTypeItemBinding
import com.xhb.uibase.widget.XHBButton

class XHBButtonFragment : ComponentFragment<XhbButtonFragmentBinding?,
    XHBButtonFragment.Model?, XHBButtonFragment.Styles?>() {

    class Model : ViewModel() {
        val types = XHBButton.ButtonType.values().asList()
    }

    class Styles(private val fragment_: XHBButtonFragment) : ViewStyles() {

        var itemBinding = ItemBinding(this)
        var itemDecoration: ItemDecoration = PaddingDecoration()

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        var disabled = false

        @Bindable
        @Title("加载")
        @Description("切换到加载状态")
        var loading = false

        @Bindable
        @Title("尺寸模式")
        @Description("有下列尺寸模式：大（Large）、中（Middle）、小（Small），默认：Large")
        var buttonSize = XHBButton.ButtonSize.Large

        @Bindable
        @Title("展示效果")
        @Description("按钮展示效果的样式集合，包括类型、尺寸定义的所有的样式，如果设置改样式，则 buttonType、buttonSize 都无效")
        @Style(ButtonStyle::class)
        var buttonAppearance = 0

        @Bindable
        @Title("宽度模式")
        @Description("有下列宽度模式：适应内容（WrapContent）、适应布局（MatchParent），默认：WrapContent")
        @ValueTitles("WrapContent", "MatchParent")
        @Values("-2", "-1")
        var widthMode = ViewGroup.LayoutParams.MATCH_PARENT

        @Bindable
        @Title("内容")
        @Description("包含文字或者图标，或者文字和图标及其他样式；资源ID类型，可以是 drawable、string、array 或者 style")
        @Style(ContentStyle::class, params = ["text", "icon", "button"])
        var conntent = 0

        @Bindable
        @Title("文字")
        @Description("改变文字，按钮会自动适应文字宽度")
        var text = "按钮"

        @Bindable
        @Title("图标")
        @Description("按钮图标，资源ID类型，图标颜色随文字颜色变化")
        @Style(IconStyle::class)
        var icon = 0

        @Bindable
        @Title("图标在右")
        @Description("设为true，则按钮图标在文字的右边，默认在左边")
        var iconAtRight = false

        fun testButtonClick(view: View) {
            if (view is XHBButton) {
                if (!view.loading) {
                    view.postDelayed({ testButtonClick(view) }, 3000)
                }
                view.loading = !view.loading
            }
        }

        override fun notifyPropertyChanged(fieldId: Int) {
            if (fieldId == BR.widthMode) {
                fragment_.updateButtons()
                return
            }
            super.notifyPropertyChanged(fieldId)
        }
    }

    class ItemBinding(private val styles: Styles) : UnitTypeItemBinding<XHBButton.ButtonType>(R.layout.xhb_button_item) {
        override fun bindView(binding: ViewDataBinding, item: XHBButton.ButtonType, position: Int) {
            super.bindView(binding, item, position)
            binding.setVariable(BR.styles, styles)
            val button = (binding as XhbButtonItemBinding).button
            val lp = button.layoutParams
            lp.width = styles.widthMode
            button.layoutParams = lp
        }
    }

    private fun updateButtons() {
        binding.buttonList.adapter!!.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "XHBButtonFragment2"
    }
}