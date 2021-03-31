package com.eazy.uibase.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.bigkoo.pickerview.configure.PickerOptions
import com.bigkoo.pickerview.listener.CustomListener
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener
import com.bigkoo.pickerview.view.TimePickerView
import com.bigkoo.pickerview.view.WheelTime
import com.eazy.uibase.R
import java.text.SimpleDateFormat
import java.util.*

class ZTimePickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : FrameLayout(context, attrs, R.attr.timePickerViewStyle), OnTimeSelectChangeListener {

    @FunctionalInterface
    interface OnSelectTimeChangeListener {
        fun onSelectTimeChanged(picker: ZTimePickerView, time: Date)
    }

    enum class TimeType {
        YearMonthDay,

    }

    var labels: Array<String>?
        get() = arrayOf(_options.label_year, _options.label_month, _options.label_day,
            _options.label_hours,  _options.label_minutes,  _options.label_seconds)
        set(value) {
            if (value != null && value.size == 6) {
                _options.label_year = value[0]
                _options.label_month = value[1]
                _options.label_day = value[2]
                _options.label_hours = value[3]
                _options.label_minutes = value[4]
                _options.label_seconds = value[5]
            }
        }

    var centerLabel
        get() = _options.isCenterLabel
        set(value) {
            _options.isCenterLabel = value
            if (_inited) {
                _wheel.isCenterLabel(value)
                refresh()
            }
        }

    var itemsVisibleCount
        get() = _options.itemsVisibleCount
        set(value) {
            _options.itemsVisibleCount = value
            if (_inited) {
                _wheel.setItemsVisible(value)
                refresh()
            }
        }

    var cyclic
        get() = _options.cyclic
        set(value) {
            _options.cyclic = value
            if (_inited) {
                _wheel.setCyclic(value)
                refresh()
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
            if (_inited) {
                _picker.setDate(date) // will modify _options.date
            } else {
                _options.date = date
            }
        }

    var startTime: Date?
        get() = _options.startDate?.time
        set(value) {
            _options.startDate = calendar(value)
        }

    var endTime: Date?
        get() = _options.endDate?.time
        set(value) {
            _options.endDate = calendar(value)
            if (_inited)
                setRange()
        }

    var listener: OnSelectTimeChangeListener? = null

    private val _options = PickerOptions(PickerOptions.TYPE_PICKER_TIME)
    private var _picker: TimePickerView
    private val _wheel: WheelTime

    private var _inited = false

    init {
        initOptions(context)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZTimePickerView, R.attr.timePickerViewStyle, 0)
        val titles = a.getString(R.styleable.ZTimePickerView_labels)
        cyclic = a.getBoolean(R.styleable.ZTimePickerView_cyclic, cyclic)
        centerLabel = a.getBoolean(R.styleable.ZTimePickerView_centerLabel, cyclic)
        lunar = a.getBoolean(R.styleable.ZTimePickerView_lunar, lunar)
        itemsVisibleCount = a.getInt(R.styleable.ZTimePickerView_itemsVisibleCount, itemsVisibleCount)
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
        _wheel = wheelTime.get(_picker) as WheelTime
        _inited = true
        _picker.show()
    }

    override fun onTimeSelectChanged(date: Date) {
        _options.date = calendar(date)
        listener?.onSelectTimeChanged(this, date)
    }

    /* private */

    companion object {

        private const val TAG = "ZTimePickerView"

        private val wheelTime = TimePickerView::class.java.getDeclaredField("wheelTime")
        private val setRangDate = TimePickerView::class.java.getDeclaredMethod("setRangDate")

        init {
            wheelTime.isAccessible = true
            setRangDate.isAccessible = true
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
    }

    private fun setRange() {
        setRangDate(_picker)
        refresh()
    }

    private fun refresh() {
        _picker.setDate(_options.date)
    }

}