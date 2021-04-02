package com.xhb.uibase.demo.components.complex

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.Bindable
import com.xhb.uibase.demo.BR
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.TimeStyle
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.databinding.XhbTimePickerViewFragmentBinding
import com.xhb.uibase.widget.XHBPanel
import com.xhb.uibase.widget.XHBTimePickerView
import java.util.*

class XHBTimePickerViewFragment : ComponentFragment<XhbTimePickerViewFragmentBinding?, XHBTimePickerViewFragment.Model?, XHBTimePickerViewFragment.Styles?>() {

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
        private const val TAG = "XHBPickerViewFragment"
    }

    override fun backgroudColor(): Int {
        return Color.parseColor("#EDF4FF")
    }

    var buttonClick = View.OnClickListener {
        val panel = XHBPanel(requireContext())
        panel.titleBar = R.style.title_bar_text
        panel.bottomButton = R.string.cancel
        val picker = LayoutInflater.from(requireContext()).inflate(R.layout.xhb_time_picker_view, panel, false) as XHBTimePickerView
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