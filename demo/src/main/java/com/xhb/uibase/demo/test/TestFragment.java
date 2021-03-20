package com.xhb.uibase.demo.test;

import android.graphics.Color;

import androidx.databinding.Bindable;

import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.core.style.ColorStyle;
import com.xhb.uibase.demo.core.style.annotation.Style;
import com.xhb.uibase.demo.databinding.TestFragmentBinding;

public class TestFragment extends ComponentFragment<TestFragmentBinding, TestFragment.Model, TestFragment.Styles> {

    public static class Model extends ViewModel {
        public String text = "Hello world!";
    }

    public enum EnumStyle {
        Value1,
        Value2,
        Value3
    }

    public static class Styles extends ViewStyles {
        @Bindable
        @Style(ColorStyle.class)
        public int color = Color.RED;
        @Bindable
        public boolean boolStyle = false;
        @Bindable
        public int intStyle = 0;
        @Bindable
        public String stringStyle = "String";
        @Bindable
        public EnumStyle enumStyle = null;
    }
}
