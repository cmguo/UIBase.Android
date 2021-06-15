package com.eazy.uibase.demo;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eazy.uibase.widget.calendar.CalendarView;


public class SimpleCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_calendar);
        ((CalendarView) findViewById(R.id.calendarView)).setSelectedCallback(time -> {
            Toast.makeText(this, "time" + time, Toast.LENGTH_LONG).show();
        });
        ((CalendarView) findViewById(R.id.calendarView)).setSelectedTime(System.currentTimeMillis());
    }
}
