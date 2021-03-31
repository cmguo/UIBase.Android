package com.eazy.uibase.binding;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class DataBindings {

    public static ViewDataBinding get(View view) {
        if (view == null)
            return null;
        ViewDataBinding binding = DataBindingUtil.getBinding(view);
        if (binding != null)
            return binding;
        try {
            return DataBindingUtil.bind(view);
        } catch (Throwable e) {
            return null;
        }
    }
}
