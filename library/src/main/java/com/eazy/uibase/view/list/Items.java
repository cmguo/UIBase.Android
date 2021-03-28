package com.eazy.uibase.view.list;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Items {

    public static Iterator<?> get(Context context, Object data) {
        if (data == null)
            return null;
        if (data instanceof List) {
            return ((List<?>) data).iterator();
        } else if (data instanceof Map) {
            return ((Map) data).entrySet().iterator();
        } else if (data instanceof Iterable) {
            return ((Iterable) data).iterator();
        } else if (data instanceof Integer) {
            return Arrays.asList(context.getResources().getStringArray((Integer) data)).iterator();
        } else {
            return null;
        }
    }

}
