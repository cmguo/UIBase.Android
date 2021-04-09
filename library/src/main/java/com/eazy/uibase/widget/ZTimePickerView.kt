package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.configure.PickerOptions
import com.bigkoo.pickerview.listener.CustomListener
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener
import com.bigkoo.pickerview.view.TimePickerView
import com.bigkoo.pickerview.view.WheelTime2
import com.eazy.uibase.R
import java.text.SimpleDateFormat
import java.util.*

class ZTimePickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : FrameLayout(context, attrs, R.attr.timePickerViewStyle), OnTimeSelectChangeListener {

    @FunctionalInterface
    interface OnSelectTimeChangeListener {
        fun onSelectTimeChanged(picker: ZTimePickerView, time: Date)
    }

    enum class TimeMode(val mode : Int) {
        YearMonthDay(0b110100000),
        YearMonthWithDayWithWeek(0b1110010000),
        MonthWithDayWithWeekHourMinute(0b1010010110);
    }

    var timeMode: TimeMode = TimeMode.YearMonthDay
        set(value) {
            field = value
            if (_inited && timeMode2 == 0) {
                _wheel.setMode(value.mode shr 4, value.mode and 15)
            }
        }
    
    var timeMode2 = 0
        set(value) {
            if (_inited) {
                field = value
                if (value == 0)
                    timeMode = timeMode
                else
                    _wheel.setMode(value shr 4, value and 15)
            }
        }

    var timeInterval = 1
        set(value) {
            field = value
            if (_inited) {
                _wheel.setInterval(value)
            }
        }

    var labels: Array<String?>?
        get() = _options.labels
        set(value) {
            _options.labels = value
        }

    var centerLabel
        get() = _options.isCenterLabel
        set(value) {
            _options.isCenterLabel = value
            if (_inited) {
                _wheel.isCenterLabel(value)
            }
        }

    var itemsVisibleCount
        get() = _options.itemsVisibleCount
        set(value) {
            _options.itemsVisibleCount = value
            if (_inited) {
                _wheel.setItemsVisible(value)
            }
        }

    var cyclic
        get() = _options.cyclic
        set(value) {
            _options.cyclic = value
            if (_inited) {
                _wheel.setCyclic(value)
            }
        }

    var lunar
        get() = _options.isLunarCalendar
        set(value) {
            _options.isLunarCalendar = value
            if (_inited)
                _picker.isLunarCalendar = value
        }

    var selectTime: Date
        get() = _options.date.time
        set(value) {
            val date = calendar(value)
            if (date == _options.date)
                return
            _options.date = date
            if (_inited) {
                _picker.setDate(date)
            }
        }

    var startTime: Date?
        get() = _options.startDate?.time
        set(value) {
            _options.startDate = calendar(value)
            if (_inited)
                setRange()
        }

    var endTime: Date?
        get() = _options.endDate?.time
        set(value) {
            _options.endDate = calendar(value)
            if (_inited)
                setRange()
        }

    var textAppearance = 0
        set(value) {
            if (field == value)
                return
            field = value
            updateTextAppearance()
        }

    var listener: OnSelectTimeChangeListener? = null

    private val _options = PickerOptions(PickerOptions.TYPE_PICKER_TIME)
    private var _picker: TimePickerView
    private val _wheel: WheelTime2

    private var _inited = false

    init {
        initOptions(context)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZTimePickerView, R.attr.timePickerViewStyle, 0)
        val type = a.getInt(R.styleable.ZTimePickerView_timeMode, 0)
        val interval = a.getInt(R.styleable.ZTimePickerView_timeInterval, 1)
        val titles = a.getString(R.styleable.ZTimePickerView_labels)
        cyclic = a.getBoolean(R.styleable.ZTimePickerView_cyclic, cyclic)
        centerLabel = a.getBoolean(R.styleable.ZTimePickerView_centerLabel, cyclic)
        lunar = a.getBoolean(R.styleable.ZTimePickerView_lunar, lunar)
        itemsVisibleCount = a.getInt(R.styleable.ZTimePickerView_itemsVisibleCount, itemsVisibleCount)
        textAppearance = a.getResourceId(R.styleable.ZTimePickerView_android_textAppearance, textAppearance)
        val date = a.getString(R.styleable.ZTimePickerView_selectTime)
        val startDate = a.getString(R.styleable.ZTimePickerView_startTime)
        val endDate = a.getString(R.styleable.ZTimePickerView_endTime)
        a.recycle()

        if (titles != null) {
            labels = titles.split(',').toTypedArray()
        }
        val df = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault())
        if (date != null) {
            this.selectTime = df.parse(date)!!
        }
        if (startDate != null) {
            this.startTime = df.parse(startDate)!!
        }
        if (endDate != null) {
            this.endTime = df.parse(endDate)!!
        }

        _picker = TimePickerView(_options)
        _wheel = wheelTime.get(_picker) as WheelTime2
        _inited = true
        timeInterval = interval
        timeMode = TimeMode.values()[type]
        _picker.show()
    }

    override fun onTimeSelectChanged(date: Date) {
        _options.date = calendar(date)
        listener?.onSelectTimeChanged(this, date)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateTextAppearance()
    }

    /* private */

    companion object {

        private const val TAG = "ZTimePickerView"

        private val wheelTime = TimePickerView::class.java.getDeclaredField("wheelTime")

        init {
            wheelTime.isAccessible = true
        }

        private fun calendar(date: Date?) : Calendar? {
            if (date == null)
                return null
            val cal = Calendar.getInstance()
            cal.time = date
            return cal
        }

    }

    private fun initOptions(context: Context) {
        _options.context = context
        _options.customListener = CustomListener { }
        _options.layoutRes = R.layout.time_picker_view
        _options.isDialog = false
        _options.decorView = this
        _options.timeSelectChangeListener = this
        _options.outSideColor = ContextCompat.getColor(context, R.color.bluegrey_00)
    }

    private fun setRange() {
        _wheel.setRangDate(_options.startDate, _options.endDate)
    }

    private fun updateTextAppearance() {
        if (textAppearance == 0)
            return
        val a = context.obtainStyledAttributes(textAppearance, R.styleable.TextAppearance)
        val color = a.getColorStateList(R.styleable.TextAppearance_android_textColor)
        val size = a.getDimension(R.styleable.TextAppearance_android_textSize, 0f)
        if (color != null) {
            _options.textColorOut = color.defaultColor
            _options.textColorCenter = color.getColorForState(intArrayOf(android.R.attr.state_selected), color.defaultColor)
            if (_inited) {
                _wheel.setTextColorOut(_options.textColorOut)
                _wheel.setTextColorCenter(_options.textColorCenter)
            }
        }
         if (size > 0)
            _options.textSizeContent = (size / context.resources.displayMetrics.density).toInt()
    }

}