package com.xhb.uibase.app.ui.main;

import androidx.lifecycle.ViewModel;

import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.core.Component;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public class ComponentsViewModel extends ViewModel {

    private static final String TAG = "ComponentsViewModel";

    // TODO: Implement the ViewModel
    private Map<Integer, Map<Integer, FragmentComponent>> controllers_ = collectComponents();

    private static Map<Integer, Map<Integer, FragmentComponent>> collectComponents() {
        Map<Integer, Map<Integer, FragmentComponent>> components = new TreeMap<>();
        for (Component controller : ServiceLoader.load(Component.class)) {
            int g = controller.group();
            int n = controller.name();
            Map<Integer, FragmentComponent> group = components.get(g);
            if (group == null) {
                group = new TreeMap<>();
                components.put(g, group);
            }
            if (controller instanceof FragmentComponent)
                group.put(n, (FragmentComponent) controller);
        }
        return components;
    }
}