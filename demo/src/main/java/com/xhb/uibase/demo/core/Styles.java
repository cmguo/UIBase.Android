package com.xhb.uibase.demo.core;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.paris.Paris;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.colors.Colors;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Styles {

    private static final String TAG = "Styles";

    public static Map<String, Integer> buttonStyles(Context context) {
        return getStyles(context, R.style.class, Pattern.compile("ButtonStyle"));
        // return getStyles(context, R.style.class, Pattern.compile("YellowLargeButtonStyle"));
    }

    public static Map<String, Integer> buttonStyles2(Context context) {
        Map<String, Integer> allButtons = getStyles(context, R.style.class, Pattern.compile("[bB]utton"));
        Map<String, Integer> buttons = buttonStyles(context);
        for (String k : buttons.keySet()) {
            allButtons.remove(k);
        }
        return allButtons;
    }

    public static Map<String, Integer> getStyles(Context context, Class<?> clazz, Pattern pattern) {
        Map<String, Integer> styles = new TreeMap<>();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                if (pattern == null || pattern.matcher(f.getName()).find()) {
                    Log.d(TAG, f.getName());
                    int id = (Integer) f.get(clazz);
                    styles.put(f.getName(), id);
                }
            }
        } catch (IllegalAccessException e) {
            Log.w(TAG, "", e);
        }
        return styles;
    }

    @androidx.databinding.BindingAdapter("style")
    public static <T> void setStyle(TextView view, int style) {
        try {
            Paris.style(view).apply(style);
        } catch (Throwable e) {
            Log.w(TAG, "setStyle " + view.getContext().getResources().getResourceEntryName(style), e);
        }
    }


    @androidx.databinding.BindingAdapter("autoWidth")
    public static <T> void setAutoWidth(TextView view, int width) {
        if (view.getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return;
        }
        view.setWidth(width);
    }
}
