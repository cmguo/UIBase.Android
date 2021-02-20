package com.xhb.uibase.app;

import android.app.Application;

import com.xhb.uibase.demo.core.SkinManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.initSkin(this);
    }
}
