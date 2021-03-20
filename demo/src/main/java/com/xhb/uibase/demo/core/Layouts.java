package com.xhb.uibase.demo.core;

import android.content.Context;
import android.util.Log;

import com.xhb.uibase.demo.R;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Layouts extends Resources {

    private static final String TAG = "Layouts";

    public static Map<String, Integer> dialogLayouts(Context context) {
        return getResources(context, R.layout.class, Pattern.compile("[Dd]ialog"));
        // return getResources(context, R.layout.class, Pattern.compile("YellowLargeButtonStyle"));
    }

}
