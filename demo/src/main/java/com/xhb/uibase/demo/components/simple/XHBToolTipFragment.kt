package com.xhb.uibase.demo.components.simple

import android.os.Bundle
import androidx.databinding.Bindable
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.SkinManager
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.annotation.*
import com.xhb.uibase.demo.databinding.XhbCompoundButtonFragmentBinding
import com.xhb.uibase.demo.databinding.XhbToolTipFragmentBinding
import com.xhb.uibase.widget.XHBButton
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class XHBToolTipFragment : ComponentFragment<XhbToolTipFragmentBinding?, XHBToolTipFragment.Model?, XHBToolTipFragment.Styles?>(), SkinObserver {

    class Model(fragment: XHBToolTipFragment) : ViewModel() {
        var tipButton: XHBButton = {
            val button = XHBButton(fragment.context!!)
            button.buttonType = XHBButton.ButtonType.Text
            button.icon = R.drawable.ic_plus
            button.iconAtRight = true
            button
        } ()
    }

    class Styles(fragment: XHBToolTipFragment?) : ViewStyles() {

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        var disabled = false

        @Bindable
        @Title("文字")
        @Description("改变文字，按钮会自动适应文字宽度")
        var text = "文字"

        private val fragment_ = fragment

        init {
        }

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
        private const val TAG = "XHBCompoundButtonFragment"
    }
}