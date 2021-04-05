package com.eazy.uibase.app;

import android.app.Application;

import com.ui.shapeutils.DevShapeUtils;
import com.eazy.uibase.daynight.DayNightManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DayNightManager.getInstance().init(this);
        DevShapeUtils.init(this);
    }
}
