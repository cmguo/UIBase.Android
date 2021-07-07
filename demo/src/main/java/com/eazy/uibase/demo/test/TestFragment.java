package com.eazy.uibase.demo.test;

import android.graphics.Color;
import android.os.Build;

import androidx.databinding.Bindable;

import com.eazy.uibase.BuildConfig;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.core.style.ColorStyle;
import com.eazy.uibase.demo.core.style.annotation.Style;
import com.eazy.uibase.demo.databinding.TestFragmentBinding;

public class TestFragment extends ComponentFragment<TestFragmentBinding, TestFragment.Model, TestFragment.Styles> {

    public static class Model extends ViewModel {

        public String text = "Hello world!";

        public String osVersion = "SDK:" + Build.VERSION.SDK_INT;
        public String buildVersion = "UIBase Build:" + BuildConfig.BUILD_VERSION
            + "\nMagicIndicator Build: " + net.lucode.hackware.magicindicator.BuildConfig.BUILD_VERSION
            + "\nWheelView Build: " + com.contrarywind.view.BuildConfig.BUILD_VERSION
            + "\nPickerView Build: " + com.bigkoo.pickerview.BuildConfig.BUILD_VERSION
            ;
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
