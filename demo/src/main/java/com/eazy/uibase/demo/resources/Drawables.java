package com.eazy.uibase.demo.resources;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.eazy.uibase.demo.R;

import java.util.Map;
import java.util.regex.Pattern;

public class Drawables extends Resources {

    private static final String TAG = "Drawables";

    public static Map<String, ResourceValue> icons(Context context) {
        return drawables(context, Pattern.compile("^icon(_[a-z]{2,})+"), false);
    }

    public static Map<String, ResourceValue> images(Context context) {
        return drawables(context, Pattern.compile("^img(_[a-z0-9]{2,})+"), false);
    }

    public static Map<String, ResourceValue> dynamicImages(Context context) {
        return drawables(context, Pattern.compile("^([^i]|i[^c])"), true);
    }

    public static Map<String, ResourceValue> drawables(Context context, Pattern pattern, boolean dayNightOnly) {
        return getResources(context.getResources(), R.drawable.class, pattern, dayNightOnly);
    }

    public static int drawable(Context context, String name) {
        return getResource(R.drawable.class, "name");
    }

    @BindingAdapter("drawable")
    public static void setDrawable(TextView view, int id) {
        Drawable drawable = AppCompatResources.getDrawable(view.getContext(), id);
        view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

}
