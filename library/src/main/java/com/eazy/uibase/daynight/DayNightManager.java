package com.eazy.uibase.daynight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

public class DayNightManager {

    private static final String KEY_CURRENT_MODEL = "them_day_night";

    @SuppressLint("StaticFieldLeak")
    private static final DayNightManager sInstance = new DayNightManager();

    public static DayNightManager getInstance() {
        return sInstance;
    }

    private Context context = null;

    /**
     * ths method should be called in Application onCreate method
     */
    public void init(Context context) {
        this.context = context.getApplicationContext();
        boolean isNightModel = loadNightModel();
        AppCompatDelegate.setDefaultNightMode(isNightModel ?
            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void setDayNightModel(AppCompatActivity activity, boolean isNight) {
        int mode = isNight ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
        saveNightModel(isNight);
        if (activity.getDelegate().getLocalNightMode() != mode) {
            // setLocalNightMode will cause onConfigurationChanged to be invoked
            //  some customs views will response to onConfigurationChanged
            activity.getDelegate().setLocalNightMode(mode);
            for (DayNightViewInflater inflater : inflaterList)
                inflater.updateViews();
        }
    }

    private final List<DayNightViewInflater> inflaterList = new ArrayList<>();


    void addActiveViewInflater(DayNightViewInflater inflater) {
        inflaterList.add(inflater);
    }

    void removeActiveViewInflater(DayNightViewInflater inflater) {
        inflaterList.remove(inflater);
    }

    private boolean loadNightModel() {
        SharedPreferences sp = context.getSharedPreferences(KEY_CURRENT_MODEL, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_CURRENT_MODEL, false);
    }

    private void saveNightModel(boolean isNight) {
        SharedPreferences sp = context.getSharedPreferences(KEY_CURRENT_MODEL, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_CURRENT_MODEL, isNight).apply();
    }

}
