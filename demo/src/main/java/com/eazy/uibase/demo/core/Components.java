package com.eazy.uibase.demo.core;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public class Components {

    static Map<Integer, List<Component>> componentGroups_ = null;
    static Map<Integer, Component> components_ = null;

    public static Map<Integer, List<Component>> collectComponents() {
        if (componentGroups_ != null)
            return componentGroups_;
        componentGroups_ = new TreeMap<>();
        components_ = new TreeMap<>();
        for (Component component : ServiceLoader.load(Component.class)) {
            int g = component.group();
            components_.put(component.id(), component);
            List<Component> group = componentGroups_.get(g);
            if (group == null) {
                group = new ArrayList<>();
                componentGroups_.put(g, group);
            }
            group.add((Component) component);
        }
        return componentGroups_;
    }

    public static Component getComponent(int id) {
        if (components_ == null)
            collectComponents();
        return components_.get(id);
    }
}
