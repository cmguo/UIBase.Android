package com.eazy.uibase.demo.components.simple

import android.view.View
import androidx.databinding.Bindable
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ContentStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.PickerViewFragmentBinding
import com.eazy.uibase.widget.ZPanel

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
        private const val TAG = "ZPanelFragment"
    }

    var buttonClick = View.OnClickListener {
    }

}