package com.xhb.uibase.demo.core.style;

import com.ustc.base.util.reflect.ClassWrapper;
import com.xhb.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceStyle extends ComponentStyle {

    protected ResourceStyle(Field field, Method getter, Method setter, String[] resources) {
        super(field, getter, setter);
        apply(resources);
    }

    private static final Map<String[], List<String>> valuesMap = new HashMap<>();
    private static final Map<String[], List<String>> titlesMap = new HashMap<>();

    private void apply(String[] resources) {
        if (valuesMap.containsKey(resources)) {
            setValues(valuesMap.get(resources), titlesMap.get(resources));
            return;
        }
        List<String> titles = new ArrayList<>();
        List<String> values = new ArrayList<>();
        titles.add("<null>");
        values.add("0");
        String type = null;
        ClassWrapper<?> clazz = null;
        for (String r : resources) {
            String[] n = r.split(":");
            if (n.length == 2) {
                r = n[1];
            }
            String[] t = r.split("/");
            if (t.length == 2) {
                r = t[1];
                if (!t[0].equals(type)) {
                    try {
                        R.drawable s;
                        Class<?> clz = Class.forName("com.xhb.uibase.demo.R$" + t[0]);
                        type = t[0];
                        clazz = ClassWrapper.wrap(clz);
                    } catch (ClassNotFoundException e) {
                        type = null;
                        clazz = null;
                    }
                }
                if (clazz != null) {
                    Integer value = clazz.get(r);
                    if (value != null) {
                        titles.add(n.length == 2 ? n[0] : r);
                        values.add(String.valueOf(value));
                    }
                }
            }
        }
        valuesMap.put(resources, values);
        titlesMap.put(resources, titles);
        setValues(values, titles);
    }

}
