package com.eazy.uibase.view.list;

import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

public interface ItemBinding<T> {

    int getItemViewType(T item);

    ViewBinding createBinding(ViewGroup parent, int viewType);

    void bindView(ViewBinding binding, T item, int position);
}
