package com.eazy.uibase.demo.components.complex

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.Bindable
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.TimeStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.TimePickerViewFragmentBinding
import com.eazy.uibase.widget.ZPanel
import com.eazy.uibase.widget.ZTimePickerView
import java.util.*

class ZTimePickerViewFragment : ComponentFragment<TimePickerViewFragmentBinding?, ZTimePickerViewFragment.Model?, ZTimePickerViewFragment.Styles?>() {

    class Model : ViewModel()

    class Styles : ViewStyles() {

        @Bindable
        @Title("循环滚动")
        @Description("设置是否循环滚动")
        var cyclic = false

        @Bindable
        @Title("农历")
        @Description("设置是否显示为农历")
        var lunar = false

        @Bindable
        @Title("标题固定")
        @Description("是否将标题固定显示在中间，默认false")
        var centerLabel = false

        @Bindable
        @Title("显示数目")
        @Description("最大可见数目，建议3，7，9")
        var itemsVisibleCount = 9

        @Bindable
        @Title("选中时间")
        @Description("当前选中时间")
        @Style(TimeStyle::class)
        var selectTime = Date()
            set(value) {
                field = value
                notifyPropertyChanged(BR.selectTime)
            }

        @Bindable
        @Title("开始时间")
        @Description("开始时间")
        @Style(TimeStyle::class)
        var startTime = Date()

        @Bindable
        @Title("结束时间")
        @Description("结束时间")
        @Style(TimeStyle::class)
        var endTime = Date()

        init {
            startTime.time -= 365L * 24 * 60 * 60 * 1000
            endTime.time += 365L * 24 * 60 * 60 * 1000
        }

    }

    companion object {
        private const val TAG = "ZPickerViewFragment"
    }

    override fun backgroudColor(): Int {
        return Color.parseColor("#EDF4FF")
    }

    var buttonClick = View.OnClickListener {
        val panel = ZPanel(requireContext())
        panel.titleBar = R.style.title_bar_text
        panel.bottomButton = R.string.cancel
        val picker = LayoutInflater.from(requireContext()).inflate(R.layout.time_picker_view, panel, false) as ZTimePickerView
        picker.centerLabel = styles.centerLabel
        picker.lunar = styles.lunar
        picker.itemsVisibleCount = styles.itemsVisibleCount
        picker.cyclic = styles.cyclic
        picker.startTime = styles.startTime
        picker.endTime = styles.endTime
        picker.selectTime = styles.selectTime
        panel.addView(picker)
        panel.popUp(parentFragmentManager)
    }

}