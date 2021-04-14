package com.eazy.uibase.demo.components.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.*
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.TipViewButtonItemBinding
import com.eazy.uibase.demo.databinding.TipViewFragmentBinding
import com.eazy.uibase.demo.databinding.SnackBarBinding
import com.eazy.uibase.demo.databinding.ToastBinding
import com.eazy.uibase.demo.databinding.ToolTipBinding
import com.eazy.uibase.view.list.UnitTypeItemBinding
import com.eazy.uibase.widget.ZTipView

class ZTipViewFragment : ComponentFragment<TipViewFragmentBinding?, ZTipViewFragment.Model?, ZTipViewFragment.Styles?>(), ZTipView.TipViewListener {

    class Model(fragment: ZTipViewFragment) : ViewModel() {

        var buttons = arrayOfNulls<Any>(when (fragment.component.id()) {
            R.id.component_z_tool_tips -> 24
            else -> 2 }).toList()
    }

    open class Styles(val fragment: ZTipViewFragment) : ViewStyles() {

        val layoutManager = GridLayoutManager(fragment.context, 4)

        val itemBinding: ItemBinding = ItemBinding(this)

        @Bindable
        @Title("最大宽度")
        @Description("整体最大宽度，设置负数，则自动加上窗口宽度")
        @Style(DimemDpStyle::class)
        var maxWidth = 300

        @Bindable
        @Title("单行文字")
        @Description("限制单行文字，默认是多行的")
        var singleLine = false

        @Bindable
        @Title("消息内容")
        @Description("自适应消息内容的宽度和高度")
        var message = "你点击了按钮"

        open fun buttonClick(view: View) {}
    }

    class ToolStyles(fragment: ZTipViewFragment) : Styles(fragment) {

        @Bindable
        @Title("建议位置")
        @Description("建议弹出位置，如果左右或者上下超过窗口区域，则会分别自动调整为其他适当位置")
        var location = ZTipView.Location.TopRight

        @Bindable
        @Title("右侧按钮")
        @Description("右侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var rightButton = R.drawable.icon_close

        @Bindable
        @Title("视觉样式")
        @Description("视觉样式集，StyleRes 类型；包括背景色，圆角尺寸，文字样式")
        @Style(AppearanceStyle::class, params = ["TipViewToolTip"])
        var tipAppearance = 0

        override fun buttonClick(view: View) {
            val binding = ToolTipBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            binding.tipView.popAt(view, fragment)
        }
    }

    open class TipToastStyles(fragment: ZTipViewFragment) : Styles(fragment) {

        @Bindable
        @Title("提示图标")
        @Description("附加在文字左侧的图标，资源ID类型，不能点击")
        @Style(IconStyle::class)
        var icon = R.drawable.icon_notice

        @Bindable
        @Title("右侧按钮")
        @Description("右侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var rightButton = 0

        init {
            maxWidth = -100
        }

    }

    open class ToastStyles(fragment: ZTipViewFragment) : TipToastStyles(fragment) {

        @Bindable
        @Title("视觉样式")
        @Description("视觉样式集，StyleRes 类型；包括背景色，圆角尺寸，文字样式")
        @Style(AppearanceStyle::class, params = ["TipViewToast"])
        var tipAppearance = 0

        init {
            rightButton = R.style.button_content_prim_style
        }

        override fun buttonClick(view: View) {
            val binding = ToastBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            val index = (view.parent.parent as ViewGroup).indexOfChild(view.parent as View)
            binding.tipView.location = ZTipView.Location.AutoToast
            if (index == 0)
                binding.tipView.popAt(view, fragment)
            else
                binding.tipView.popUp(Toast.LENGTH_SHORT)
        }
    }

    class SnackStyles(fragment: ZTipViewFragment) : TipToastStyles(fragment) {

        @Bindable
        @Title("左侧按钮")
        @Description("左侧按钮的内容，参见按钮的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var leftButton = R.drawable.icon_close

        @Bindable
        @Title("视觉样式")
        @Description("视觉样式集，StyleRes 类型；包括背景色，圆角尺寸，文字样式")
        @Style(AppearanceStyle::class, params = ["TipViewSnack"])
        var tipAppearance = 0

        init {
            rightButton = R.style.button_content_text_style
        }

        private val snacks = arrayListOf<ZTipView>()

        override fun buttonClick(view: View) {
            val binding = SnackBarBinding.inflate(LayoutInflater.from(fragment.context))
            binding.styles = this
            binding.executePendingBindings()
            val index = (view.parent.parent as ViewGroup).indexOfChild(view.parent as View)
            binding.tipView.location = ZTipView.Location.ManualLayout
            binding.tipView.dismissDelay = 0
            snacks.add(binding.tipView)
            val target = if (index == 0) (view.rootView as ViewGroup).getChildAt(0) else fragment.binding.root
            binding.tipView.popAt(target, fragment)
        }

        fun clearSnacks() {
            for (s in snacks) {
                s.dismiss()
            }
            snacks.clear()
        }

    }

    class ItemBinding(private val styles: Styles) : UnitTypeItemBinding<Any>(R.layout.tip_view_button_item) {
        override fun bindView(binding: ViewDataBinding, item: Any?, position: Int) {
            super.bindView(binding, item, position)
            binding.setVariable(BR.styles, styles)
            when (styles.fragment.component.id()) {
                R.id.component_z_snack_bars -> {
                    (binding as TipViewButtonItemBinding).button.text =
                        if (position == 0) "根视图" else "展示区域"
                }
                R.id.component_z_toasts -> {
                    (binding as TipViewButtonItemBinding).button.text =
                        if (position == 0) "普通View" else "系统Toast"
                }
            }
        }
    }

    override fun tipViewButtonClicked(view: ZTipView, btnId: Int) {
        val tip = ZTipView(requireContext(), null)
        val name = resources.getResourceEntryName(btnId)
        tip.location = ZTipView.Location.TopCenter
        tip.message = "点击了按钮${name}"
        tip.popAt(view)
    }

    override fun createStyle(): Styles {
        return when (component.id()) {
            R.id.component_z_tool_tips -> ToolStyles(this)
            R.id.component_z_toasts -> ToastStyles(this)
            R.id.component_z_snack_bars -> SnackStyles(this)
            else -> Styles(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (styles as? SnackStyles)?.clearSnacks()
    }

    companion object {
        private const val TAG = "ZToolTipFragment"
    }
}