package com.eazy.uibase.demo.components.simple

import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.PickerViewFragmentBinding
import com.eazy.uibase.widget.ZPanel

class ZPickerViewFragment : ComponentFragment<PickerViewFragmentBinding?, ZPickerViewFragment.Model?, ZPickerViewFragment.Styles?>()
    , ZPanel.PanelListener {

    class Model : ViewModel() {

        // first two is disabled
        var states = arrayListOf<Any?>(-android.R.attr.state_enabled, -android.R.attr.state_enabled)

        @Bindable
        var selection: Int? = 0
            set(value) {
                field = value
                notifyPropertyChanged(BR.selection)
            }

        @Bindable
        var selections: List<Int> = arrayListOf(0)
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

    private var lp : ViewGroup.LayoutParams? = null

    var buttonClick = View.OnClickListener {
        val panel = ZPanel(requireContext())
        panel.titleBar = R.style.title_bar_icon
        binding.frame.removeView(binding.picker)
        lp = binding.picker.layoutParams
        panel.addView(binding.picker)
        panel.listener = this
        panel.popUp(parentFragmentManager)
    }

    override fun panelDismissed(panel: ZPanel) {
        panel.removeView(binding.picker)
        binding.frame.addView(binding.picker, lp)
    }
}