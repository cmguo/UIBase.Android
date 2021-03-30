package com.xhb.uibase.demo.components.simple

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.Bindable
import com.xhb.uibase.demo.BR
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.databinding.XhbPickerViewFragmentBinding
import com.xhb.uibase.view.list.Items
import com.xhb.uibase.widget.XHBPanel
import com.xhb.uibase.widget.XHBPickerView

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
        private const val TAG = "XHBPickerViewFragment"
    }

    var buttonClick = View.OnClickListener {
        val panel = XHBPanel(requireContext())
        panel.titleBar = R.style.title_bar_text
        panel.bottomButton = R.string.cancel
        val picker = LayoutInflater.from(requireContext()).inflate(R.layout.xhb_picker_view, panel, false) as XHBPickerView
        picker.singleSelection = styles!!.singleSelection
        picker.selections = model!!.selections
        panel.addView(picker)
        panel.popUp(parentFragmentManager)
    }

}