package com.eazy.uibase.widget.calendar.bean;


public class MonthBean extends DateBean {
    private String month;

    public MonthBean() {
    }

    public MonthBean(String month, int year) {
        this.month = month;
        super.setYear(year);
    }

    public String getMonth() {
        return month;
    }
}
