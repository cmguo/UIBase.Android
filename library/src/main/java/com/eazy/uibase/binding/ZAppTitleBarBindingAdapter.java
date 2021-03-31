package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

import com.eazy.uibase.widget.ZAppTitleBar;

public class ZAppTitleBarBindingAdapter {

    @BindingAdapter("data")
    public static void setPanelData(ZAppTitleBar bar, Object data) {
        ViewDataBinding binding = DataBindings.get(bar.getExtensionBody());
        if (binding != null)
            binding.setVariable(BR.data, data);
    }

}
