package com.eazy.uibase.widget.calendar;


import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.widget.calendar.bean.DateBean;
import com.eazy.uibase.widget.calendar.bean.DayBean;
import com.eazy.uibase.widget.calendar.bean.MonthBean;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;


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
        return new MonthBean(month, year);
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
    public static Calendar setCalendar(DayBean bean) {
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

    public boolean isFirstItemVisible(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return layoutManager instanceof GridLayoutManager &&
            ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() <= 0;
    }

    public int loadMoreItems(List<DateBean> items, int monthIndex, boolean next) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, monthIndex);
        List<DateBean> moreList = getDateBeans(calendar);
        if (next)
            items.addAll(moreList);
        else
            items.addAll(0, moreList);
        return moreList.size();
    }


    // 格式：年－月－日 小时：分钟：秒
    public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";

    // 格式：年－月－日 小时：分钟
    public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

    // 格式：年月日 小时分钟秒
    public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";

    // 格式：年－月－日
    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

    // 格式：月－日
    public static final String SHORT_DATE_FORMAT = "MM-dd";

    // 格式：小时：分钟：秒
    public static final String LONG_TIME_FORMAT_SS = "HH:mm:ss";

    //格式：小时 ：分钟
    public static final String LONG_TIME_FORMAT = "HH:mm";


    //格式：年-月
    public static final String MONTG_DATE_FORMAT = "yyyy-MM";

    // 年的加减
    public static final int SUB_YEAR = Calendar.YEAR;

    // 月加减
    public static final int SUB_MONTH = Calendar.MONTH;

    // 天的加减
    public static final int SUB_DAY = Calendar.DATE;

    // 小时的加减
    public static final int SUB_HOUR = Calendar.HOUR;

    // 分钟的加减
    public static final int SUB_MINUTE = Calendar.MINUTE;

    // 秒的加减
    public static final int SUB_SECOND = Calendar.SECOND;

    static final String dayNames[] = {"", "星期日", "星期一", "星期二", "星期三", "星期四",
        "星期五", "星期六"};

    @SuppressWarnings("unused")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss");


    /**
     * 把符合日期格式的字符串转换为日期类型
     *
     * @param dateStr
     * @return
     */
    public static java.util.Date stringToDate(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat newFormat = new SimpleDateFormat(format);
        try {
            newFormat.setLenient(false);
            d = newFormat.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            d = null;
        }
        return d;
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     */
    public static java.util.Date stringToDate(String dateStr, String format,
                                              ParsePosition pos) {
        Date d = null;
        SimpleDateFormat newFormat = new SimpleDateFormat(format);
        try {
            newFormat.setLenient(false);
            d = newFormat.parse(dateStr, pos);
        } catch (Exception e) {
            d = null;
        }
        return d;
    }

    /**
     * 把日期转换为字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(java.util.Date date, String format) {
        String result = "";
        SimpleDateFormat newFormat = new SimpleDateFormat(format);
        try {
            result = newFormat.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param format
     * @return
     */
    public static String getCurrDate(String format) {
        return dateToString(new Date(), format);
    }

    /**
     * @param dateStr
     * @param amount
     * @return
     */
    public static String dateSub(int dateKind, String dateStr, int amount) {
        Date date = stringToDate(dateStr, FORMAT_ONE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(dateKind, amount);
        return dateToString(calendar.getTime(), FORMAT_ONE);
    }

    /**
     * 两个日期相减
     *
     * @param firstTime
     * @param secTime
     * @return 相减得到的秒数
     */
    public static long timeSub(String firstTime, String secTime) {
        long first = stringToDate(firstTime, FORMAT_ONE).getTime();
        long second = stringToDate(secTime, FORMAT_ONE).getTime();
        return (second - first) / 1000;
    }

    /**
     * 获得某月的天数
     *
     * @param year  int
     * @param month int
     * @return int
     */
    public static int getDaysOfMonth(String year, String month) {
        int days = 0;
        if (month.equals("1") || month.equals("3") || month.equals("5")
            || month.equals("7") || month.equals("8") || month.equals("10")
            || month.equals("12")) {
            days = 31;
        } else if (month.equals("4") || month.equals("6") || month.equals("9")
            || month.equals("11")) {
            days = 30;
        } else {
            if ((Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0)
                || Integer.parseInt(year) % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        }

        return days;
    }

    /**
     * 获取某年某月的天数
     *
     * @param year  int
     * @param month int 月份[1-12]
     * @return int
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前日期
     *
     * @return int
     */
    public static int getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获得当前月份
     *
     * @return int
     */
    public static int getToMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前年份
     *
     * @return int
     */
    public static int getToYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 返回日期的天
     *
     * @param date Date
     * @return int
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 返回日期的年
     *
     * @param date Date
     * @return int
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 返回日期的月份，1-12
     *
     * @param date Date
     * @return int
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
     *
     * @param date1 Date
     * @param date2 Date
     * @return long
     */
    public static long dayDiff(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / 86400000;
    }

    /**
     * 比较两个日期的年差
     *
     * @param before
     * @param after
     * @return
     */
    public static int yearDiff(String before, String after) {
        Date beforeDay = stringToDate(before, LONG_DATE_FORMAT);
        Date afterDay = stringToDate(after, LONG_DATE_FORMAT);
        return getYear(afterDay) - getYear(beforeDay);
    }

    /**
     * 比较指定日期与当前日期的差
     *
     * @param after
     * @return
     */
    public static int yearDiffCurr(String after) {
        Date beforeDay = new Date();
        Date afterDay = stringToDate(after, LONG_DATE_FORMAT);
        return getYear(beforeDay) - getYear(afterDay);
    }

    /**
     * 比较指定日期与当前日期的差
     *
     * @param before
     * @return
     */
    public static long dayDiffCurr(String before) {
        Date currDate = stringToDate(currDay(), LONG_DATE_FORMAT);
        Date beforeDate = stringToDate(before, LONG_DATE_FORMAT);
        return (currDate.getTime() - beforeDate.getTime()) / 86400000;

    }

    /**
     * 获取每月的第一周
     *
     * @param year
     * @param month
     * @return
     */
    public static int getFirstWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
        c.set(year, month - 1, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取每月的最后一周
     *
     * @param year
     * @param month
     * @return
     */
    public static int getLastWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
        c.set(year, month - 1, getDaysOfMonth(year, month));
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得当前日期字符串，格式"yyyy_MM_dd_HH_mm_ss"
     *
     * @return
     */
    public static String getCurrent() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        StringBuffer sb = new StringBuffer();
        sb.append(year).append("_").append(addzero(month, 2))
            .append("_").append(addzero(day, 2)).append("_")
            .append(addzero(hour, 2)).append("_").append(
            addzero(minute, 2)).append("_").append(
            addzero(second, 2));
        return sb.toString();
    }

    /**
     * 获得当前日期字符串，格式"yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getNow() {
        Calendar today = Calendar.getInstance();
        return dateToString(today.getTime(), FORMAT_ONE);
    }

    /**
     * 根据生日获取星座
     *
     * @param birth YYYY-mm-dd
     * @return
     */
    public static String getAstro(String birth) {
        if (!isDate(birth)) {
            birth = "2000" + birth;
        }
        if (!isDate(birth)) {
            return "";
        }
        int month = Integer.parseInt(birth.substring(birth.indexOf("-") + 1,
            birth.lastIndexOf("-")));
        int day = Integer.parseInt(birth.substring(birth.lastIndexOf("-") + 1));
        String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
        int[] arr = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};
        int start = month * 2 - (day < arr[month - 1] ? 2 : 0);
        return s.substring(start, start + 2) + "座";
    }

    /**
     * 判断日期是否有效,包括闰年的情况
     *
     * @param date YYYY-mm-dd
     * @return
     */
    public static boolean isDate(String date) {
        StringBuffer reg = new StringBuffer(
            "^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
        reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
        reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
        reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
        reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
        reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
        reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
        reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
        Pattern p = Pattern.compile(reg.toString());
        return p.matcher(date).matches();
    }

    /**
     * 取得指定日期过 months 月后的日期 (当 months 为负数表示指定月之前);
     *
     * @param date 日期 为null时表示当天
     */
    public static Date nextMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 取得指定日期过 day 天后的日期 (当 day 为负数表示指日期之前);
     *
     * @param date 日期 为null时表示当天
     */
    public static Date nextDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }

    /**
     * 取得距离今天 day 日的日期
     *
     * @param day
     * @param format
     * @return
     */
    public static String nextDay(int day, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, day);
        return dateToString(cal.getTime(), format);
    }

    /**
     * 取得指定日期过 day 周后的日期 (当 day 为负数表示指定月之前)
     *
     * @param date 日期 为null时表示当天
     */
    public static Date nextWeek(Date date, int week) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.WEEK_OF_MONTH, week);
        return cal.getTime();
    }

    /**
     * 获取当前的日期(yyyy-MM-dd)
     */
    public static String currDay() {
        return dateToString(new Date(), LONG_DATE_FORMAT);
    }

    /**
     * 获取昨天的日期
     *
     * @return
     */
    public static String beforeDay() {
        return beforeDay(LONG_DATE_FORMAT);
    }

    /**
     * 根据时间类型获取昨天的日期
     *
     * @param format
     * @return
     */
    public static String beforeDay(String format) {
        return dateToString(nextDay(new Date(), -1), format);
    }

    /**
     * 获取明天的日期
     */
    public static String afterDay() {
        return dateToString(nextDay(new Date(), 1),
            LONG_DATE_FORMAT);
    }

    /**
     * 取得当前时间距离1900/1/1的天数
     *
     * @return
     */
    public static int getDayNum() {
        int daynum = 0;
        GregorianCalendar gd = new GregorianCalendar();
        Date dt = gd.getTime();
        GregorianCalendar gd1 = new GregorianCalendar(1900, 1, 1);
        Date dt1 = gd1.getTime();
        daynum = (int) ((dt.getTime() - dt1.getTime()) / (24 * 60 * 60 * 1000));
        return daynum;
    }

    /**
     * getDayNum的逆方法(用于处理Excel取出的日期格式数据等)
     *
     * @param day
     * @return
     */
    public static Date getDateByNum(int day) {
        GregorianCalendar gd = new GregorianCalendar(1900, 1, 1);
        Date date = gd.getTime();
        date = nextDay(date, day);
        return date;
    }

    /**
     * 针对yyyy-MM-dd HH:mm:ss格式,显示yyyymmdd
     */
    public static String getYmdDateCN(String datestr) {
        if (datestr == null)
            return "";
        if (datestr.length() < 10)
            return "";
        StringBuffer buf = new StringBuffer();
        buf.append(datestr.substring(0, 4)).append(datestr.substring(5, 7))
            .append(datestr.substring(8, 10));
        return buf.toString();
    }

    /**
     * 获取本月第一天
     *
     * @param format
     * @return
     */
    public static String getFirstDayOfMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        return dateToString(cal.getTime(), format);
    }

    /**
     * 获取本月最后一天
     *
     * @param format
     * @return
     */
    public static String getLastDayOfMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return dateToString(cal.getTime(), format);
    }

    /**
     * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
     *
     * @param sourceDate
     * @param formatLength
     * @return 重组后的数据
     */
    public static String addzero(int sourceDate, int formatLength) {
        /*
         * 0 指前面补充零
         * formatLength 字符总长度为 formatLength
         * d 代表为正数。
         */
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }


    /**
     * 获取当前是星期几
     *
     * @return 星期几
     */
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        return dayNames[cal.get(Calendar.DAY_OF_WEEK)];
    }

    /**
     * 根据十二小时制或者二十四小时制得到时间
     *
     * @return 时间
     */
    public static String getCurrentTimeBy12_24(Context context) {
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            return getCurrDate(LONG_TIME_FORMAT);
        } else {
            StringBuilder sb = new StringBuilder();
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            if (hour >= 12) {
                sb.append(hour - 12).append(":").append(minute).append(" PM");
            } else {
                sb.append(hour).append(":").append(minute).append(" AM");
            }
            return sb.toString();
        }
    }

    /**
     * 时间戳转时间
     *
     * @param time 时间戳
     * @return 时间
     */
    public static String TimeAndDate(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = sdf.format(new Date(time));
        return d;
    }

    /**
     * 转换任意时间格式
     *
     * @param date   日期+时间
     * @param format 时间格式--例：yyyy-MM-dd
     * @return
     */
    public static String getDate(String date, String format) {
        String re_time = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        Date d;
        try {
            d = sdf1.parse(date);
            re_time = sdf2.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * 周历中，通过diff定位周
     *
     * @param diff
     * @return
     */
    public static Calendar getCalendarWeekByDiff(long baseTime,int diff) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(baseTime);
        c.set(Calendar.DAY_OF_WEEK, 1);
        c.add(Calendar.HOUR, diff * 7 * 24);
        return c;
    }

}
