package com.eazy.uibase.demo.test;

import android.graphics.Color;

import androidx.databinding.Bindable;

import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.TestBinding;

public class TestFragment extends ComponentFragment<TestBinding, TestFragment.Model, TestFragment.Style> {

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
}