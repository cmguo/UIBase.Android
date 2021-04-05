package com.eazy.uibase.demo.core;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.paris.Paris;
import com.eazy.uibase.demo.R;

import java.util.Map;
import java.util.regex.Pattern;

public class Styles extends Resources {

    private static final String TAG = "Styles";

    public static Map<String, Integer> xhbButtonStyles(Context context) {
        return getResources(context, R.style.class, Pattern.compile("^Z_Button_\\w+_\\w+"));
    }

    public static Map<String, Integer> buttonStyles(Context context) {
        return getResources(context, R.style.class, Pattern.compile("Button"));
    }

    public static Map<String, Integer> checkboxStyles(Context context) {
        return getResources(context, R.style.class, Pattern.compile("CheckBox"));
    }

    public static Map<String, Integer> radioStyles(Context context) {
        return getResources(context, R.style.class, Pattern.compile("Radio"));
    }

    public static Map<String, Integer> switchStyles(Context context) {
        return getResources(context, R.style.class, Pattern.compile("Switch"));
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
