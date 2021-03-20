package com.eazy.uibase.demo.components.simple

import android.os.Bundle
import androidx.databinding.Bindable
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.SkinManager
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.CompoundButtonFragmentBinding
import com.eazy.uibase.demo.databinding.ToolTipFragmentBinding
import com.eazy.uibase.widget.ZButton
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class ZToolTipFragment : ComponentFragment<ToolTipFragmentBinding?, ZToolTipFragment.Model?, ZToolTipFragment.Styles?>(), SkinObserver {

    class Model(fragment: ZToolTipFragment) : ViewModel() {
        var tipButton: ZButton = {
            val button = ZButton(fragment.context!!)
            button.buttonType = ZButton.ButtonType.Text
            button.icon = R.drawable.ic_plus
            button.iconAtRight = true
            button
        } ()
    }

    class Styles(fragment: ZToolTipFragment?) : ViewStyles() {

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
        private const val TAG = "ZCompoundButtonFragment"
    }
}