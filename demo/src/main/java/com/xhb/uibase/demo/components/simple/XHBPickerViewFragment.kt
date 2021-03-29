package com.xhb.uibase.demo.components.simple

import android.view.View
import androidx.databinding.Bindable
import com.xhb.uibase.demo.BR
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.ContentStyle
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.databinding.XhbPickerViewFragmentBinding
import com.xhb.uibase.widget.XHBPanel

class XHBPickerViewFragment : ComponentFragment<XhbPickerViewFragmentBinding?, XHBPickerViewFragment.Model?, XHBPickerViewFragment.Styles?>() {

    class Model : ViewModel() {

        @Bindable
        var selection: Int? = null
            set(value) {
                field = value
                notifyPropertyChanged(BR.selection)
            }

        @Bindable
        var selections: List<Int> = arrayListOf<Int>()
            set(value) {
                field = value
                notifyPropertyChanged(BR.selections)
            }
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("单选")
        @Description("单项选择，只可选择一个，也可能没有选择任何项")
        var singleSelection = false

    }

    companion object {
        private const val TAG = "XHBPanelFragment"
    }

    var buttonClick = View.OnClickListener {
    }

}