package com.xhb.uibase.demo.core;

import android.app.Application;
import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.content.res.SkinCompatResources;
import skin.support.design.app.SkinMaterialViewInflater;
import skin.support.observe.SkinObserver;

public class SkinManager {

    public static void initSkin(Application application) {
        SkinCompatManager
            .withoutActivity(application)
            .setSkinAllActivityEnable(true)
            .addInflater(new SkinAppCompatViewInflater()) // 基础控件换肤初始化
            .addInflater(new SkinMaterialViewInflater()) // material design 控件换肤初始化[可选]
            .addInflater(new SkinCardViewInflater()) // CardView v7 控件换肤初始化[可选]
            .addInflater(new SkinConstraintViewInflater())
            .loadSkin();
    }

    public static void switchMode(boolean dark) {
        if (dark)
            SkinCompatManager.getInstance().loadSkin("skin-dark.apk", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
        else
            SkinCompatManager.getInstance().restoreDefaultTheme();
    }

    public static AppCompatDelegate getDelegate(AppCompatActivity activity) {
        return SkinAppCompatDelegateImpl.get(activity, activity);
    }

    public static void addObserver(SkinObserver o) {
        SkinCompatManager.getInstance().addObserver(o);
    }

    public static void removeObserver(SkinObserver o) {
        SkinCompatManager.getInstance().deleteObserver(o);
    }

    public static int getColor(Context context, @ColorRes int resId) {
        return SkinCompatResources.getColor(context, resId);
    }
}
