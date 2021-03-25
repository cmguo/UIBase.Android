package com.xhb.uibase.view.list;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

public class ItemBindings {

    public static <T> ItemBinding<T> get(Context context, Object bindingOrLayout) {
        if (bindingOrLayout instanceof ItemBinding)
            return (ItemBinding) bindingOrLayout;
        return new UnitTypeItemBinding<>(context, (Integer) bindingOrLayout);
    }

}
