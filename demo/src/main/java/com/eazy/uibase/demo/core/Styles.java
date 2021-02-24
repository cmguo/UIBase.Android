package com.eazy.uibase.demo.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.airbnb.paris.Paris;
import com.airbnb.paris.styles.ResourceStyle;
import com.airbnb.paris.styles.Style;
import com.airbnb.paris.typed_array_wrappers.TypedArrayWrapper;
import com.eazy.uibase.demo.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import skin.support.content.res.SkinCompatResources;

public class Styles {

    private static final String TAG = "Styles";

    public static Map<String, Integer> xhbButtonStyles(Context context) {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.putAll(getStyles(context, R.style.class, Pattern.compile("^Z.Button.*.Large.*")));
        map.putAll(getStyles(context, R.style.class, Pattern.compile("^Z.Button.*.Medium.*")));
        map.putAll(getStyles(context, R.style.class, Pattern.compile("^Z.Button.*.Small.*")));
        return map;
        // return getStyles(context, R.style.class, Pattern.compile("YellowLargeButtonStyle"));
    }

    public static Map<String, Integer> buttonStyles(Context context) {
        return getStyles(context, R.style.class, Pattern.compile("Button"));
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
            Paris.style(view).apply(new SkinResourceStyle(style));
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
