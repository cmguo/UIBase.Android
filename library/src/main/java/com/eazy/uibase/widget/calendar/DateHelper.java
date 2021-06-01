package com.eazy.uibase.widget.calendar;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.widget.calendar.bean.DateBean;
import com.eazy.uibase.widget.calendar.bean.DayBean;
import com.eazy.uibase.widget.calendar.bean.MonthBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DateHelper {
    private int todayDayIndex = 0;


    /**
     * 获取当前月的index，方便快速滑动到当前日期
     */
    public int getTodayIndex() {
        return todayDayIndex;
    }

    /**
     * 更新当前日期index，方便快速滑动到当前日期
     */
    public void updateToadyDayIndex(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance();
        if (currentCalendar.getTimeInMillis() > calendar.getTimeInMillis()) {
            todayDayIndex += 1;
        }
    }

    /**
     * 获取每个月的日历，包括这个月的具体是几月
     *
     */
    public List<DateBean> getDateBeans(Calendar calendar) {
        List<DateBean> beans = new ArrayList<>();
        beans.add(getMonthBean(calendar));
        updateToadyDayIndex(calendar);
        beans.addAll(getDaysOfMonth(calendar));
        return beans;
    }


    /**
     * 获取每个月具体的日历
     *
     * @param calendar
     * @return
     */
    public List<DayBean> getDaysOfMonth(Calendar calendar) {
        List<DayBean> dayBeans = new ArrayList<>();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < firstDayOfWeek - 1; i++) {
            // 每月前面的空格部分
            updateToadyDayIndex(calendar);
            DayBean bean = new DayBean();
            bean.setYear(year);
            bean.setSpace(true);
            dayBeans.add(bean);
        }
        DayBean bean = new DayBean(year, month, currentDay, DayBean.STATE_NORMAL);
        // 当前日期，1号
        dayBeans.add(bean);
        isFuture(bean);
        isToday(bean);
        isWeek(bean);
        updateToadyDayIndex(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int maxDays = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = currentDay; i < maxDays; i++) {
            // 所有日期
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            DayBean beanL = new DayBean(year, month, i + 1, DayBean.STATE_NORMAL);
            dayBeans.add(beanL);
            isFuture(beanL);
            isToday(beanL);
            isWeek(beanL);
            updateToadyDayIndex(calendar);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return dayBeans;
    }

    public DayBean getDayBeanByCalendar(Calendar calendar) {
        DayBean beanL = new DayBean(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), DayBean.STATE_NORMAL);
        isFuture(beanL);
        isToday(beanL);
        isWeek(beanL);
        return beanL;
    }

    /**
     * 获取哪一年哪一月
     *
     * @param calendar
     * @return
     */
    public MonthBean getMonthBean(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return new MonthBean(year + "年" + month + "月", year);
    }

    /**
     * 判断是否是今天
     *
     * @param bean
     */
    private void isToday(DayBean bean) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, bean.getYear());
        calendar.set(Calendar.MONTH, bean.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, bean.getDay());
        if (calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)) {
            bean.setState(DayBean.STATE_TODAY);
        }
    }

    /**
     * 判断是否是将来的某个时刻
     */
    private void isFuture(DayBean bean) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, bean.getYear());
        calendar.set(Calendar.MONTH, bean.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, bean.getDay());
        if (calendar.get(Calendar.YEAR) > todayCalendar.get(Calendar.YEAR)) {
            bean.setState(DayBean.STATE_FUTURE);
        } else if (calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)
                && calendar.get(Calendar.DAY_OF_YEAR) > todayCalendar.get(Calendar.DAY_OF_YEAR)) {
            bean.setState(DayBean.STATE_FUTURE);
        }
    }

    /**
     * 判断是否周末
     */
    private void isWeek(DayBean bean) {
        Calendar calendar = setCalendar(bean);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            bean.setColumnStart(true);
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            bean.setColumnEnd(true);
        }

    }

    /**
     * 将所有条目中的状态改为普通状态
     *
     * @param items
     */
    public void setAllNormal(List<DateBean> items) {
        for (DateBean dateBean : items) {
            if (dateBean instanceof DayBean) {
                if (((DayBean) dateBean).getState() != DayBean.STATE_FUTURE) {
                    ((DayBean) dateBean).setState(DayBean.STATE_NORMAL);
                    isToday((DayBean) dateBean);
                    isFuture((DayBean) dateBean);
                }
            }
        }
    }

    /**
     * 设置给定时间的日历calendar
     *
     * @param bean
     * @return
     */
    public Calendar setCalendar(DayBean bean) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, bean.getYear());
        calendar.set(Calendar.MONTH, bean.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, bean.getDay());
        return calendar;
    }


    /**
     * 点击结束时间的处理
     *
     * @param items
     * @param start
     * @param end
     */
    public void onEndClick(List<DateBean> items, DayBean start, DayBean end) {
        for (DateBean dateBean : items) {
            if (dateBean instanceof DayBean) {
                if (dateBean.getYear() != 0 && !((DayBean) dateBean).isSpace()) {
                    Calendar startCalendar = setCalendar(start);
                    Calendar endCaldendar = setCalendar(end);
                    Calendar currentCalendar = setCalendar((DayBean) dateBean);
                    if (currentCalendar.getTimeInMillis() == startCalendar.getTimeInMillis()) {
                        ((DayBean) dateBean).setState(DayBean.STATE_STARTED);
                    } else if (currentCalendar.getTimeInMillis() == endCaldendar.getTimeInMillis()) {
                        ((DayBean) dateBean).setState(DayBean.STATE_END);
                    } else if (currentCalendar.getTimeInMillis() > startCalendar.getTimeInMillis()
                            && currentCalendar.getTimeInMillis() < endCaldendar.getTimeInMillis()) {
                        ((DayBean) dateBean).setState(DayBean.STATE_PERIOD_SELECTED);
                    }
                }
            }
        }
    }


    public void isSelected(DayBean bean, DayBean selectBean) {
        if (bean.getYear() == selectBean.getYear() && bean.getMonth() == selectBean.getMonth()
                && bean.getDay() == selectBean.getDay()) {
            bean.setState(DayBean.STATE_SELECTED);
        }
    }

    public boolean isLastItemVisible(RecyclerView recyclerView, int size) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return layoutManager instanceof GridLayoutManager &&
            ((GridLayoutManager) layoutManager).findLastVisibleItemPosition() > size - 2;
    }

    public boolean isFirstItemVisible(RecyclerView recyclerView){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return layoutManager instanceof GridLayoutManager &&
            ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() <= 0;
    }

    public int loadMoreItems(List<DateBean> items, int monthIndex,boolean next) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, monthIndex);
        List<DateBean> moreList = getDateBeans(calendar);
        if(next)
          items.addAll(moreList);
        else
            items.addAll(0,moreList);
        return moreList.size();
    }

}
