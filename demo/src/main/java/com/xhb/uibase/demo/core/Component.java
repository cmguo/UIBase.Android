package com.xhb.uibase.demo.core;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public interface Component {
    @IdRes
    int id();
    @StringRes
    int group();
    @DrawableRes
    int icon();
    @StringRes
    int title();
    @StringRes
    int description();

    Class<? extends ComponentFragment> fragmentClass();

    static Map<Integer, List<Component>> collectComponents() {
        Map<Integer, List<Component>> components = new TreeMap<>();
        for (Component component : ServiceLoader.load(Component.class)) {
            int g = component.group();
            List<Component> group = components.get(g);
            if (group == null) {
                group = new ArrayList<>();
                components.put(g, group);
            }
            group.add((Component) component);
        }
        return components;
    }

    default ComponentFragment createFragment() throws InstantiationException, IllegalAccessException {
        return fragmentClass().newInstance();
    }
}
