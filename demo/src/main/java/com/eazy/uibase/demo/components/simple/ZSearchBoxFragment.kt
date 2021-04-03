package com.eazy.uibase.demo.components.simple

import androidx.databinding.Bindable
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.core.Colors
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ContentStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.SearchBoxFragmentBinding
import com.eazy.uibase.widget.ZSearchBox
import com.eazy.uibase.widget.ZTipView

class ZSearchBoxFragment : ComponentFragment<SearchBoxFragmentBinding?, ZSearchBoxFragment.Model?, ZSearchBoxFragment.Styles?>()
    , ZSearchBox.SearchBoxListener {

    class Model(fragment: ZSearchBoxFragment) : ViewModel() {

        private val colors = Colors.stdColors(fragment.requireContext()).keys

        fun filter(text: String) {
            result = colors.filter { it.contains(text) }
        }

        @Bindable
        var result: List<String> = colors.toList()
            set(value) {
                field = value
                notifyPropertyChanged(BR.result)
            }
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("右侧按钮")
        @Description("右侧操作按钮，可选，参见按纽的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var rightButton = 0

    }

    companion object {
        private const val TAG = "ZSearchBoxFragment"
    }

    override fun searchBoxButtonClicked(bar: ZSearchBox, btnId: Int) {
        val tip = ZTipView(requireContext(), null)
        val name = resources.getResourceEntryName(btnId)
        tip.message = "点击了按钮${name}"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(bar)
    }

    override fun searchBoxFocused(bar: ZSearchBox) {
//        val tip = ZTipView(requireContext(), null)
//        tip.message = "开始输入"
//        tip.location = ZTipView.Location.AutoToast
//        tip.popAt(bar)
    }

    override fun searchBoxTextChanged(bar: ZSearchBox, text: String) {
        model.filter(text)
    }
}