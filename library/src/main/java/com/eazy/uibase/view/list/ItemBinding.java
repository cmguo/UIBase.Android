package com.eazy.uibase.view.list;

import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

public interface ItemBinding {

    int getItemViewType(Object item);

    ViewBinding createBinding(ViewGroup parent, int viewType);

    void bindView(ViewBinding binding, Object item, int position);
}
