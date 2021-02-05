package com.xhb.uibase.demo.test;

import android.graphics.Color;

import androidx.databinding.Bindable;

import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.core.annotation.Author;
import com.xhb.uibase.demo.databinding.TestBinding;

@AutoService(Component.class)
@Author("cmguo")
public class TestComponent extends FragmentComponent<TestBinding, TestComponent.Model, TestComponent.Style> {

    public static class Model extends ViewModel {
        public String text = "Hello world!";
    }

    public enum EnumStyle {
        Value1,
        Value2,
        Value3
    }

    public static class Style extends ViewStyles {
        @Bindable
        public int color = Color.RED;
        @Bindable
        public boolean boolStype = false;
        @Bindable
        public int intStyle = 0;
        @Bindable
        public String stringStyle = "String";
        @Bindable
        public EnumStyle enumStyle = null;
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
