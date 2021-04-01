package com.xhb.uibase.demo.core.style;

import com.ustc.base.util.reflect.ClassWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResourceStyle extends ComponentStyle {

    protected ResourceStyle(Field field, Method getter, Method setter, String[] resources) {
        super(field, getter, setter);
        apply(resources);
    }

    private static Map<String, String[]> paramTemplates = new TreeMap<>();

    private static final Map<String[], List<String>> typesMap = new HashMap<>();
    private static final Map<String[], List<String>> valuesMap = new HashMap<>();
    private static final Map<String[], List<String>> titlesMap = new HashMap<>();

    static {
        paramTemplates.put("<button>", new String[] {"button", "icon", "text"});
        paramTemplates.put("<title>", new String[] {"title", "text"});
    }

    private void apply(String[] resources) {
        if (!valuesMap.containsKey(resources)) {
            List<String> titles = new ArrayList<>();
            List<String> types = new ArrayList<>();
            List<String> values = new ArrayList<>();
            parse(resources, titles, types, values);
            typesMap.put(resources, types);
            valuesMap.put(resources, values);
            titlesMap.put(resources, titles);
        }
        String[] params = styleParams();
        if (params != null && params.length > 0) {
            List<String> titles = new ArrayList<>();
            List<String> values = new ArrayList<>();
            filter(params, valuesMap.get(resources), titlesMap.get(resources), typesMap.get(resources), values, titles);
            setValues(values, titles);
        } else {
            setValues(valuesMap.get(resources), titlesMap.get(resources));
        }
    }

    private void filter(String[] params, List<String> valuesIn, List<String> titlesIn, List<String> typesIn,
                       List<String> valuesOut, List<String> titlesOut) {
        List<String> titles = new ArrayList<>();
        List<String> types = new ArrayList<>();
        for (String p : params) {
            if (paramTemplates.containsKey(p)) {
                for (String p1 : paramTemplates.get(p)) {
                    if (p1.startsWith("@")) {
                        types.add(p1.substring(1));
                    } else {
                        titles.add(p1);
                    }
                }
            } else if (p.startsWith("@")) {
                types.add(p.substring(1));
            } else {
                titles.add(p);
            }
        }
        titlesOut.add("<null>");
        valuesOut.add("0");
        for (int i = 1; i < titlesIn.size(); ++i) {
            String v = valuesIn.get(i);
            String t = typesIn.get(i);
            String n = titlesIn.get(i);
            if (!types.isEmpty() && !types.contains(t))
                continue;
            if (!titles.isEmpty()) {
                boolean found = false;
                for (String tp : titles) {
                    if (n.startsWith(tp)) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    continue;
            }
            titlesOut.add(n);
            valuesOut.add(v);
        }
    }

    private void parse(String[] resources, List<String> titles, List<String> types, List<String> values) {
        titles.add("<null>");
        values.add("0");
        types.add("null");
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
                        Class<?> clz = Class.forName("com.xhb.uibase.demo.R$" + t[0]);
                        type = t[0];
                        clazz = ClassWrapper.wrap(clz);
                    } catch (ClassNotFoundException e) {
                        type = null;
                        clazz = null;
                    }
                }
            }
            if (clazz != null) {
                Integer value = clazz.get(r);
                if (value != null) {
                    titles.add(n.length == 2 ? n[0] : r);
                    types.add(type);
                    values.add(String.valueOf(value));
                }
            }
        }
    }

}
