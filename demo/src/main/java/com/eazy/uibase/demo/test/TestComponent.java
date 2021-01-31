package com.eazy.uibase.demo.test;

import android.graphics.Color;

import com.google.auto.service.AutoService;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.FragmentComponent;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyle;
import com.eazy.uibase.demo.databinding.TestBinding;

@AutoService(Component.class)
public class TestComponent extends FragmentComponent<TestBinding, TestComponent.Model, TestComponent.Style> {

    public static class Model extends ViewModel {
        public String text = "Hello world!";
    }

    public static class Style extends ViewStyle {
        public int color = Color.RED;
    }

    public TestComponent() {
        super(R.layout.test);
    }

    @Override
    public int group() {
        return R.string.group_test;
    }

    @Override
    public int name() {
        return R.string.component_test;
    }
}
