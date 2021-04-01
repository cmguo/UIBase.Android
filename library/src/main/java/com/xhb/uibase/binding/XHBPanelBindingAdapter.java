package com.xhb.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

import com.xhb.uibase.widget.XHBPanel;

public class XHBPanelBindingAdapter {

    @BindingAdapter("data")
    public static void setPanelData(XHBPanel panel, Object data) {
        ViewDataBinding binding = DataBindings.get(panel.getBody());
        if (binding != null)
            binding.setVariable(BR.data, data);
    }

}
