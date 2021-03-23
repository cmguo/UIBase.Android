package com.eazy.uibase.app;

import android.app.Application;

import com.ui.shapeutils.DevShapeUtils;
import com.eazy.uibase.demo.core.SkinManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.initSkin(this);
        DevShapeUtils.init(this);
    }
}
