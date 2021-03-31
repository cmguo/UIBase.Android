package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

import com.eazy.uibase.widget.ZPanel;

public class ZPanelBindingAdapter {

    @BindingAdapter("data")
    public static void setPanelData(ZPanel panel, Object data) {
        ViewDataBinding binding = DataBindings.get(panel.getBody());
        if (binding != null)
            binding.setVariable(BR.data, data);
    }

}
