package com.eazy.uibase.view.list;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Items {

    public static Iterable<?> get(Context context, Object data) {
        if (data == null)
            return null;
        if (data instanceof List) {
            return ((List<?>) data);
        } else if (data instanceof Map) {
            return ((Map<?, ?>) data).entrySet();
        } else if (data instanceof Iterable) {
            return (Iterable<?>) data;
        } else if (data.getClass().isArray()) {
            return Arrays.asList((Object[]) data);
        } else if (data instanceof Integer) {
            return Arrays.asList(context.getResources().getStringArray((Integer) data));
        } else {
            return null;
        }
    }

}
