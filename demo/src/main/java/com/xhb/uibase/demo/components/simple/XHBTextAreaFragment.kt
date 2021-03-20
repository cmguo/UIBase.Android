package com.xhb.uibase.demo.components.simple

import android.os.Bundle
import androidx.databinding.Bindable
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.SkinManager
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.IconStyle
import com.xhb.uibase.demo.core.style.annotation.*
import com.xhb.uibase.demo.databinding.XhbCompoundButtonFragmentBinding
import com.xhb.uibase.demo.databinding.XhbTextAreaFragmentBinding
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class XHBTextAreaFragment : ComponentFragment<XhbTextAreaFragmentBinding?, XHBTextAreaFragment.Model?, XHBTextAreaFragment.Styles?>(), SkinObserver {

    class Model(fragment: XHBTextAreaFragment?) : ViewModel() {
    }

    class Styles(fragment: XHBTextAreaFragment?) : ViewStyles() {

        @Bindable
        @Title("最大字数") @Description("设为0不限制；如果有限制，将展现字数指示")
        var maximumCharCount = 100

        @Bindable
        @Title("最小高度") @Description("没有文字时的高度")
        var minimunHeight = 100f

        @Bindable
        @Title("最大高度") @Description("高度随文字变化，需要指定最大高度；包含字数指示（如果有的话）")
        var maximunHeight = 300f

        @Bindable
        @Title("占位文字") @Description("没有任何输入文字时，显示的占位文字（灰色）")
        var placeholder = "请输入"

        @Bindable
        @Title("显示边框") @Description("设为0不限制；如果有限制，将展现字数指示")
        var showBorder = false

        @Bindable
        @Title("左图标") @Description("设置左边的图标，资源ID类型，控件内部会自动重新布局")
        @Style(IconStyle::class)
        var leftIcon = 0

        @Bindable
        @Title("右图标") @Description("设置右边的图标，资源ID类型，控件内部会自动重新布局")
        @Style(IconStyle::class)
        var rightIcon = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SkinManager.addObserver(this)
    }

    override fun onDestroy() {
        SkinManager.removeObserver(this)
        super.onDestroy()
    }

    override fun updateSkin(observable: SkinObservable, o: Any) {
    }

    companion object {
        private const val TAG = "XHBTextAreaFragment"
    }
}