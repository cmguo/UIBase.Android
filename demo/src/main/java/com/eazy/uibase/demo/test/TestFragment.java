package com.eazy.uibase.demo.test;

import android.graphics.Color;

import androidx.databinding.Bindable;

import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.TestFragmentBinding;

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
