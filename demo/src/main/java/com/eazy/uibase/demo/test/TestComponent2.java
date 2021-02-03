package com.eazy.uibase.demo.test;

import android.graphics.Color;

import com.google.auto.service.AutoService;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Author;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.FragmentComponent;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyle;
import com.eazy.uibase.demo.databinding.Test2Binding;

@AutoService(Component.class)
@Author("cmguo")
public class TestComponent2 extends FragmentComponent<Test2Binding, TestComponent2.Model, TestComponent2.Style> {

    public static class Model extends ViewModel {
        public String text = "Hello world2!";
    }

    public static class Style extends ViewStyle {
        public int color = Color.BLUE;
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
