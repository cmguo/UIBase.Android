package com.xhb.uibase.demo.components.simple

import android.view.View
import androidx.databinding.Bindable
import com.xhb.uibase.demo.BR
import com.xhb.uibase.demo.core.Colors
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.ContentStyle
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.databinding.XhbSearchBoxFragmentBinding
import com.xhb.uibase.widget.XHBSearchBox
import com.xhb.uibase.widget.XHBTipView

class XHBSearchBoxFragment : ComponentFragment<XhbSearchBoxFragmentBinding?, XHBSearchBoxFragment.Model?, XHBSearchBoxFragment.Styles?>()
    , XHBSearchBox.SearchBoxListener {

    class Model(fragment: XHBSearchBoxFragment) : ViewModel() {

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
        private const val TAG = "XHBSearchBoxFragment"
    }

    override fun searchBoxButtonClicked(bar: XHBSearchBox, btnId: Int) {
        val tip = XHBTipView(requireContext(), null)
        val name = resources.getResourceEntryName(btnId)
        tip.message = "点击了按钮${name}"
        tip.location = XHBTipView.Location.AutoToast
        tip.popAt(bar)
    }

    override fun searchBoxFocused(bar: XHBSearchBox) {
//        val tip = XHBTipView(requireContext(), null)
//        tip.message = "开始输入"
//        tip.location = XHBTipView.Location.AutoToast
//        tip.popAt(bar)
    }

    override fun searchBoxTextChanged(bar: XHBSearchBox, text: String) {
        model.filter(text)
    }
}