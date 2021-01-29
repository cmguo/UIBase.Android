package com.eazy.uibase.app.ui.main;

import androidx.lifecycle.ViewModel;

import com.eazy.uibase.demo.Component;
import com.eazy.uibase.demo.DemoController;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public class ComponentsViewModel extends ViewModel {

    private static final String TAG = "ComponentsViewModel";

    // TODO: Implement the ViewModel
    private Map<Integer, Map<Integer, Component>> controllers_ = collectComponents();

    private static Map<Integer, Map<Integer, Component>> collectComponents() {
        Map<Integer, Map<Integer, Component>> components = new TreeMap<>();
        for (DemoController controller : ServiceLoader.load(DemoController.class)) {
            int g = controller.group();
            int n = controller.name();
            Map<Integer, Component> group = components.get(g);
            if (group == null) {
                group = new TreeMap<>();
                components.put(g, group);
            }
            if (controller instanceof Component)
                group.put(n, (Component) controller);
        }
        return components;
    }
}