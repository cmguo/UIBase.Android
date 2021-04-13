package com.eazy.uibase.demo.core;

import android.content.Context;

import com.eazy.uibase.demo.R;

import java.util.Map;
import java.util.regex.Pattern;

public class Layouts extends Resources {

    private static final String TAG = "Layouts";

    public static Map<String, Integer> dialogLayouts() {
        return getResources(R.layout.class, Pattern.compile("[Dd]ialog"));
        // return getResources(context, R.layout.class, Pattern.compile("YellowLargeButtonStyle"));
    }

}
