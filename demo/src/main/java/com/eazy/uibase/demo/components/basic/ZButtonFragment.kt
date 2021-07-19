package com.eazy.uibase.demo.components.basic

import android.content.res.TypedArray
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ButtonStyle
import com.eazy.uibase.demo.core.style.ContentStyle
import com.eazy.uibase.demo.core.style.IconStyle
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.ButtonFragmentBinding
import com.eazy.uibase.demo.databinding.ButtonItemBinding
import com.eazy.uibase.demo.resources.Resources
import com.eazy.uibase.view.list.PaddingDecoration
import com.eazy.uibase.view.list.UnitTypeItemBinding
import com.eazy.uibase.widget.ZButton

class ZButtonFragment : ComponentFragment<ButtonFragmentBinding?,
    ZButtonFragment.Model?, ZButtonFragment.Styles?>() {

    class Model : ViewModel() {
        val types : List<Any> = ZButton.ButtonType.values().asList()
        val sizes : List<Any> = ZButton.ButtonSize.values().asList()
    }

    class Styles(private val fragment_: ZButtonFragment) : ViewStyles() {

        var itemBinding = ItemBinding(this)
        var itemDecoration: ItemDecoration = PaddingDecoration()

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        var disabled = false

        @Bindable
        @Title("加载")
        @Description("切换到加载状态，演示时也可以分别点击某个按钮，进入加载状态")
        var loading = false

        @Bindable
        @Title("尺寸模式")
        @Description("切换到尺寸演示模式")
        var sizeMode = false

        @Bindable
        @Title("颜色模式")
        @Description("有下列颜色模式：主要（Primitive）、二级（Secondary）、三级（Tertiary）、危险（Danger）、文字（Text），默认：Primitive")
        var buttonType = ZButton.ButtonType.Text

        @Bindable
        @Title("尺寸模式")
        @Description("有下列尺寸模式：大（Large）、中（Middle）、小（Small），默认：Large")
        var buttonSize = ZButton.ButtonSize.Large

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
        var widthMode = ViewGroup.LayoutParams.WRAP_CONTENT

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
        var icon = R.drawable.icon_exit

        @Bindable
        @Title("加载文字")
        @Description("改变加载状态显示的文字，按钮会自动适应文字宽度")
        var loadingText = "加载中..."

        @Bindable
        @Title("加载图标")
        @Description("按钮加载图标，资源ID类型，图标颜色随文字颜色变化")
        @Style(IconStyle::class)
        var loadingIcon = R.drawable.button_loading_primary_anim

        @Bindable
        @Title("图标位置")
        @Description("按钮图标的位置，上下左右四种，默认在左边")
        var iconPosition = ZButton.IconPosition.Left

        fun testButtonClick(view: View) {
            if (view is ZButton) {
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

        fun detail(data: Any) : String {
            if (data is ZButton.ButtonType) {
                val a: TypedArray = fragment_.requireContext()
                    .obtainStyledAttributes(data.resId, R.styleable.ZButton_Appearance)
                val textColor = a.getResourceId(R.styleable.ZButton_Appearance_android_textColor, 0)
                val backgroundColor = a.getResourceId(R.styleable.ZButton_Appearance_backgroundColor, 0)
                a.recycle()
                val textColorStr =
                    Resources.simpleName(fragment_.requireContext().resources.getResourceName(textColor))
                val backgroundColorStr =
                    Resources.simpleName(fragment_.requireContext().resources.getResourceName(backgroundColor))
                return String.format("textColor: %s\nbackgroundColor: %s",
                    textColorStr, backgroundColorStr)
            } else if (data is ZButton.ButtonSize) {
                val a: TypedArray = fragment_.requireContext()
                    .obtainStyledAttributes(data.resId, R.styleable.ZButton_Appearance)
                val height = a.getDimension(R.styleable.ZButton_Appearance_height, 0f).toInt()
                val radius = a.getDimension(R.styleable.ZButton_Appearance_cornerRadius, 0f).toInt()
                val textSize = a.getDimension(R.styleable.ZButton_Appearance_android_textSize, 0f).toInt()
                val iconSize = a.getDimension(R.styleable.ZButton_Appearance_iconSize, 0f).toInt()
                return String.format("height: %d, radius: %d\ntextSize: %d, iconSize: %d",
                    height, radius, textSize, iconSize)
            } else {
                return ""
            }
        }
    }

    class ItemBinding(private val styles: Styles) : UnitTypeItemBinding(R.layout.button_item) {
        override fun bindView(binding: ViewDataBinding, item: Any, position: Int) {
            super.bindView(binding, item, position)
            binding.setVariable(BR.styles, styles)
            binding.executePendingBindings()
            val button = (binding as ButtonItemBinding).button
            if (!styles.sizeMode) {
                button.buttonType = item as ZButton.ButtonType
                button.buttonSize = ZButton.ButtonSize.Large
            } else {
                button.buttonType = ZButton.ButtonType.Primitive
                button.buttonSize = item as ZButton.ButtonSize
            }
            val lp = button.layoutParams
            lp.width = styles.widthMode
            button.layoutParams = lp
        }
    }

    private fun updateButtons() {
        binding.buttonList.adapter!!.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ZButtonFragment2"
    }
}