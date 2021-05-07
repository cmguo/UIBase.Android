package com.eazy.uibase.daynight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class DayNightManager {

    private static final String TAG = "DayNightManager";
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
        init(context, false);
    }

    public void init(Context context, boolean defaultIsNight) {
        this.context = context.getApplicationContext();
        boolean isNightModel = loadNightModel(defaultIsNight);
        AppCompatDelegate.setDefaultNightMode(isNightModel ?
            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private WeakHashMap<Context, SparseArray<?>> AppCompatResources_sColorStateCaches = null;

    public void setDayNightModel(boolean isNight) {
        int mode = isNight ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
        saveNightModel(isNight);
        for (DayNightViewInflater inflater : inflaterList) {
            if (inflater.getActivity() instanceof  AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) inflater.getActivity();
                if (activity.getDelegate().getLocalNightMode() != mode) {
                    // setLocalNightMode will cause onConfigurationChanged to be invoked
                    //  some customs views will response to onConfigurationChanged
                    Configuration configuration = activity.getResources().getConfiguration();
                    activity.getDelegate().setLocalNightMode(mode);
                    if (Build.VERSION.SDK_INT < 23) {
                        Configuration configuration1 = activity.getResources().getConfiguration();
                        if (configuration == configuration1) {
                            if (AppCompatResources_sColorStateCaches == null) {
                                try {
                                    Field field = AppCompatResources.class.getDeclaredField("sColorStateCaches");
                                    field.setAccessible(true);
                                    AppCompatResources_sColorStateCaches = (WeakHashMap<Context, SparseArray<?>>) field.get(null);
                                } catch (Throwable e) {
                                    Log.w(TAG, e);
                                }
                            }
                            if (AppCompatResources_sColorStateCaches != null) {
                                AppCompatResources_sColorStateCaches.clear();
                            }
                        }
                    }
                    activity.findViewById(android.R.id.content).dispatchConfigurationChanged(configuration);
                }
            }
            inflater.updateViews();
        }
    }

    public boolean isNightMode() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    private final List<DayNightViewInflater> inflaterList = new ArrayList<>();


    void addActiveViewInflater(DayNightViewInflater inflater) {
        inflaterList.add(inflater);
    }

    void removeActiveViewInflater(DayNightViewInflater inflater) {
        inflaterList.remove(inflater);
    }

    private boolean loadNightModel(boolean defaultIsNight) {
        SharedPreferences sp = context.getSharedPreferences(KEY_CURRENT_MODEL, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_CURRENT_MODEL, defaultIsNight);
    }

    private void saveNightModel(boolean isNight) {
        SharedPreferences sp = context.getSharedPreferences(KEY_CURRENT_MODEL, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_CURRENT_MODEL, isNight).apply();
    }

}
