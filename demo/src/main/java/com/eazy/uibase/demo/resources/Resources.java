package com.eazy.uibase.demo.resources;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.AnyRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.eazy.uibase.R;
import com.eazy.uibase.daynight.styleable.AttrValue;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Resources {

    private static final String TAG = "Resources";

    public static Map<String, Integer> getResources(Class<?> clazz, Pattern pattern) {
        Map<String, Integer> ress = new TreeMap<>();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                if (pattern == null || pattern.matcher(f.getName()).find()) {
                    Log.d(TAG, f.getName());
                    int id = (Integer) f.get(clazz);
                    ress.put(f.getName(), id);
                }
            }
        } catch (Throwable e) {
            Log.w(TAG, "", e);
        }
        return ress;
    }

    public static int getResource(Class<?> clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            return (Integer) f.get(clazz);
        } catch (Throwable e) {
            Log.w(TAG, "", e);
            return  0;
        }
    }

    public static class ResourceValue extends BaseObservable {

        private TypedValue value = new TypedValue();

        @Bindable
        public int getValue() {
            return value.data;
        }

        @Bindable
        public CharSequence getStringValue() {
            return value.string;
        }

        @Bindable
        @AnyRes
        public int getResId() {
            return value.resourceId;
        }

    }

    public static Map<String, ResourceValue> getResources(android.content.res.Resources resources,
                                                       Class<?> clazz, Pattern pattern) {
        return getResources(resources, clazz, pattern, false);
    }

    public static Map<String, ResourceValue> getResources(android.content.res.Resources resources,
                                                       Class<?> clazz, Pattern pattern, boolean dayNightOnly) {
        Map<String, ResourceValue> ress = new TreeMap<>();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                if (pattern == null || pattern.matcher(f.getName()).find()) {
                    Log.d(TAG, f.getName());
                    int id = (Integer) f.get(clazz);
                    String name = resources.getResourceName(id);
                    ResourceValue v = new ResourceValue();
                    resources.getValue(id, v.value, true);
                    if (dayNightOnly && !AttrValue.isThemed(v.value))
                        continue;
                    ress.put(name, v);
                }
            }
        } catch (Throwable e) {
            Log.w(TAG, "", e);
        }
        return ress;
    }

    public static ResourceValue getResource(android.content.res.Resources resources, String name) {
        try {
            ResourceValue v = new ResourceValue();
            resources.getValue(name, v.value, true);
            return v;
        } catch (Throwable e) {
            Log.w(TAG, "", e);
            return null;
        }
    }

    public static void updateResources(android.content.res.Resources resources, Map<String, ResourceValue> ress) {
        try {
            for (Map.Entry<String, ResourceValue> e : ress.entrySet()) {
                resources.getValue(e.getValue().getResId(), e.getValue().value, true);
                e.getValue().notifyChange();
            }
        } catch (Throwable e) {
            Log.w(TAG, "", e);
        }
    }

    public static String simpleName(String name) {
        return name.substring(name.indexOf('/') + 1);
    }

}