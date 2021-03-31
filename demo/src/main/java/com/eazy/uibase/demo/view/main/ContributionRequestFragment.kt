package com.eazy.uibase.demo.view.main

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.Bindable
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.SkinManager
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ColorStyle
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.databinding.ContributionRequestFragmentBinding
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class ContributionRequestFragment : ComponentFragment<ContributionRequestFragmentBinding?, ContributionRequestFragment.Model?, ContributionRequestFragment.Styles?>(), SkinObserver {

    class Model : ViewModel() {
        val message = "很抱歉，该组件（控件）暂未实现。\n如果您有意愿加入并贡献一份力量，我们将非常欢迎！"
    }

    class Styles : ViewStyles() {
        @Bindable
        @Style(ColorStyle::class)
        var color = Color.RED
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
        private const val TAG = "ZTextAreaFragment"
    }
}