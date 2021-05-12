package com.eazy.uibase.demo.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public class Components {

    static Map<Integer, List<ComponentInfo>> componentGroups_ = null;
    static Map<Integer, ComponentInfo> components_ = null;

    public static Map<Integer, List<ComponentInfo>> collectComponents(Context context) {
        if (componentGroups_ != null)
            return componentGroups_;
        componentGroups_ = new TreeMap<>();
        components_ = new TreeMap<>();
        for (Component component : ServiceLoader.load(Component.class)) {
            ComponentInfo info = new ComponentInfo(context, component);
            components_.put(component.id(), info);
            int g = component.group();
            List<ComponentInfo> group = componentGroups_.get(g);
            if (group == null) {
                group = new ArrayList<>();
                componentGroups_.put(g, group);
            }
            group.add(info);
        }
        return componentGroups_;
    }

    public static ComponentInfo getComponent(int id) {
        return components_.get(id);
    }
}
