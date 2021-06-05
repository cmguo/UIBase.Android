package com.eazy.uibase.widget.calendar.bean;


import com.eazy.uibase.widget.calendar.DateHelper;

import java.time.Year;
import java.util.Calendar;

public class DayBean extends DateBean {
    public static final int STATE_NORMAL = 0;   // 正常状态
    public static final int STATE_STARTED = 1;  // 开始日期
    public static final int STATE_END = 2;      // 结束日期
    public static final int STATE_SELECTED = 3;   // 选中日期
    public static final int STATE_FUTURE = 4;   // 未来日期
    public static final int STATE_PERIOD_SELECTED = 5;  // 选中区间
    public static final int STATE_TODAY = 6;  // 当前日期

    private int month;
    private int day;
    private int state = 0;

    private boolean isSpace = false;

    private boolean isColumnStart = false; // 有效列首，1号或者每周末
    private boolean isColumnEnd = false;  // 月末或者周六

    public DayBean() {
    }

    public DayBean(int year, int month, int day, int state) {
        super.setYear(year);
        this.month = month;
        this.day = day;
        this.state = state;
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setData(int year, int month, int day) {
        super.setYear(year);
        this.month = month;
        this.day = day;
    }

    public boolean isSpace() {
        return isSpace;
    }

    public void setSpace(boolean space) {
        isSpace = space;
    }

    public boolean isColumnStart() {
        return isColumnStart;
    }

    public void setColumnStart(boolean columnStart) {
        isColumnStart = columnStart;
    }

    public boolean isColumnEnd() {
        return isColumnEnd;
    }

    public void setColumnEnd(boolean columnEnd) {
        isColumnEnd = columnEnd;
    }

    public boolean isSameDay(DayBean dayBean) {
        return this.year == dayBean.year && this.month == dayBean.month && this.day == dayBean.day;
    }

    public boolean isToady() {
        Calendar calendar = Calendar.getInstance();
        return this.year == calendar.get(Calendar.YEAR) && this.month == calendar.get(Calendar.MONTH) &&
            this.day == calendar.get(Calendar.DAY_OF_MONTH);
    }

    public boolean withByDayRange(long start, long end) {
        long dayTime = DateHelper.setCalendar(this).getTimeInMillis();
        return dayTime >= start && dayTime <= end;
    }

    public long getDayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }
}

