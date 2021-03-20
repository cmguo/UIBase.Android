package com.eazy.uibase.demo.core;

import android.content.Context;
import android.util.Log;

import com.eazy.uibase.R;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Drawables extends Resources {

    private static final String TAG = "Drawables";

    public static Map<String, Integer> drawables(Context context) {
        return getResources(context, R.drawable.class, Pattern.compile("^([a-z]{2,}_)+\\d+"));
    }

    public static int drawable(Context context, String name) {
        return getResource(context, R.drawable.class, "name");
    }

    public static final String[] icons = {"<null>", "ic_erase", "ic_plus"};
}
