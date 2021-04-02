package com.eazy.uibase.demo.components.simple

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.Bindable
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.PickerViewFragmentBinding
import com.eazy.uibase.view.list.Items
import com.eazy.uibase.widget.ZPanel
import com.eazy.uibase.widget.ZPickerView

class ZPickerViewFragment : ComponentFragment<PickerViewFragmentBinding?, ZPickerViewFragment.Model?, ZPickerViewFragment.Styles?>() {

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
        private const val TAG = "ZPickerViewFragment"
    }

    var buttonClick = View.OnClickListener {
        val panel = ZPanel(requireContext())
        panel.titleBar = R.style.title_bar_text
        panel.bottomButton = R.string.cancel
        val picker = LayoutInflater.from(requireContext()).inflate(R.layout.picker_view, panel, false) as ZPickerView
        picker.singleSelection = styles.singleSelection
        picker.selections = model.selections
        panel.addView(picker)
        panel.popUp(parentFragmentManager)
    }

}