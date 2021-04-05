package com.xhb.uibase.demo.view.main

import android.graphics.Color
import androidx.databinding.Bindable
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.ColorStyle
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.databinding.ContributionRequestFragmentBinding

class ContributionRequestFragment : ComponentFragment<ContributionRequestFragmentBinding?, ContributionRequestFragment.Model?, ContributionRequestFragment.Styles?>() {

    class Model : ViewModel() {
        val message = "很抱歉，该组件（控件）暂未实现。\n如果您有意愿加入并贡献一份力量，我们将非常欢迎！"
    }

    class Styles : ViewStyles() {
        @Bindable
        @Style(ColorStyle::class)
        var color = Color.RED
    }

    companion object {
        private const val TAG = "XHBTextAreaFragment"
    }
}