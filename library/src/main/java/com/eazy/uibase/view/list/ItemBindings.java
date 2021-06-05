package com.eazy.uibase.view.list;

import android.content.Context;

public class ItemBindings {

    public static <T> ItemBinding get(Context context, Object bindingOrLayout) {
        if (bindingOrLayout == null)
            return null;
        if (bindingOrLayout instanceof ItemBinding)
            return (ItemBinding) bindingOrLayout;
        return new UnitTypeItemBinding(context, (Integer) bindingOrLayout);
    }

}
