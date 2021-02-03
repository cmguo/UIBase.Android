package com.xhb.uibase.demo.test;

import android.graphics.Color;

import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.core.Author;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyle;
import com.xhb.uibase.demo.databinding.TestBinding;

@AutoService(Component.class)
@Author("cmguo")
public class TestComponent extends FragmentComponent<TestBinding, TestComponent.Model, TestComponent.Style> {

    public static class Model extends ViewModel {
        public String text = "Hello world!";
    }

    public static class Style extends ViewStyle {
        public int color = Color.RED;
    }

    @Override
    public int group() {
        return R.string.group_test;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_radio;
    }

    @Override
    public int title() {
        return R.string.component_test;
    }

    @Override
    public int description() {
        return R.string.component_test_desc;
    }
}
