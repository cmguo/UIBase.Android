package com.xhb.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

import com.xhb.uibase.widget.XHBAppTitleBar;

public class XHBAppTitleBarBindingAdapter {

    @BindingAdapter("data")
    public static void setPanelData(XHBAppTitleBar bar, Object data) {
        ViewDataBinding binding = DataBindings.get(bar.getExtensionBody());
        if (binding != null)
            binding.setVariable(BR.data, data);
    }

}
