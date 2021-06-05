package com.eazy.uibase.widget.calendar.bean;


public class MonthBean extends DateBean {
    private int month;

    public MonthBean(int month, int year) {
        this.month = month;
        super.setYear(year);
    }

    public int getMonth() {
        return month;
    }
}
