package com.eazy.uibase.demo.components.optional

import android.view.View
import android.view.ViewGroup
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

class ZTimePickerViewFragment : ComponentFragment<TimePickerViewFragmentBinding?, ZTimePickerViewFragment.Model?, ZTimePickerViewFragment.Styles?>()
    , ZPanel.PanelListener {

    class Model : ViewModel()

    class Styles : ViewStyles() {

        @Bindable
        @Title("循环滚动")
        @Description("设置是否循环滚动")
        var cyclic = false

        @Bindable
        @Title("时间模式")
        @Description("常用时间模式枚举，特殊模式设置 timeMode2")
        var timeMode = ZTimePickerView.TimeMode.YearMonthDay

        @Bindable
        @Title("日历模式2")
        @Description("通用日历模式，由 6 + 4 个 bit 位描述，从高到低依次是：合并模式、年、月、周次、日、周，上下午，时、分、秒")
        var timeMode2 = 0

        @Bindable
        @Title("合并")
        @Description("通用日历模式第 10 个 bit 位，表示是否使用合并模式")
        var timeModeBit9 = false

        @Bindable
        @Title("年")
        @Description("通用日历模式第 9 个 bit 位，表示是否显示年")
        var timeModeBit8 = false

        @Bindable
        @Title("月")
        @Description("通用日历模式第 8 个 bit 位，表示是否显示月")
        var timeModeBit7 = false

        @Bindable
        @Title("周次")
        @Description("通用日历模式第 7 个 bit 位，表示是否显示周次，如果显示月，则为当月的周次，否则为当年的周次")
        var timeModeBit6 = false

        @Bindable
        @Title("日")
        @Description("通用日历模式第 6 个 bit 位，表示是否显示日")
        var timeModeBit5 = false

        @Bindable
        @Title("周")
        @Description("通用日历模式第 5 个 bit 位，表示是否显示周，周一到周日")
        var timeModeBit4 = false

        @Bindable
        @Title("上下午")
        @Description("通用日历模式第 4 个 bit 位，表示是否显示上午、下午")
        var timeModeBit3 = false

        @Bindable
        @Title("时")
        @Description("通用日历模式第 3 个 bit 位，表示是否显示时，如果显示上下午，则为 12 小时制")
        var timeModeBit2 = false

        @Bindable
        @Title("分")
        @Description("通用日历模式第 2 个 bit 位，表示是否显示分")
        var timeModeBit1 = false

        @Bindable
        @Title("秒")
        @Description("通用日历模式第 1 个 bit 位，表示是否显示秒")
        var timeModeBit0 = false

        @Bindable
        @Title("时间间隔")
        @Description("显示时间时，减少精度，如 interval = 5，则只显示 0、5、10 ...")
        var timeInterval = 1

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

        override fun notifyPropertyChanged(fieldId: Int) {
            val index = arrayListOf(
                BR.timeModeBit0, BR.timeModeBit1, BR.timeModeBit2, BR.timeModeBit3,
                BR.timeModeBit4, BR.timeModeBit5, BR.timeModeBit6, BR.timeModeBit7,
                BR.timeModeBit8, BR.timeModeBit9).indexOf(fieldId)
            if (index >= 0) {
                timeMode2 = timeMode2 xor (1 shl index)
                notifyPropertyChanged(BR.timeMode2)
            }
            super.notifyPropertyChanged(fieldId)
        }
    }

    companion object {
        private const val TAG = "ZPickerViewFragment"
    }

    override fun backgroundColor(): Int {
        return R.color.blue_100
    }

    private var lp : ViewGroup.LayoutParams? = null

    var buttonClick = View.OnClickListener {
        val panel = ZPanel(requireContext())
        panel.titleBar = R.style.title_bar_text
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