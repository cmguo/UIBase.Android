package com.eazy.uibase.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewbinding.ViewBinding;

import com.eazy.uibase.BR;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseItemBinding implements ItemBinding {

    private LayoutInflater mInflater;
    private int mBrData;
    private int mBrViewModel;
    private Map<Object, Object> itemViewModels = new HashMap<>();

    public BaseItemBinding() {
        this(null, BR.data, BR.viewModel);
    }

    public BaseItemBinding(int brData) {
        this(null, brData, BR.viewModel);
    }

    public BaseItemBinding(int brData, int brViewModel) {
        this(null, brData, brViewModel);
    }

    public BaseItemBinding(Context context) {
        this(context, BR.data, BR.viewModel);
    }

    public BaseItemBinding(Context context, int brData) {
        this(context, brData, BR.viewModel);
    }

    public BaseItemBinding(Context context, int brData, int brViewModel) {
        if (context != null)
            mInflater = LayoutInflater.from(context);
        mBrData = brData;
        mBrViewModel = brViewModel;
    }

    public static class ViewHolder implements ViewBinding {

        private final View view;

        public ViewHolder(View view) {
            this.view = view;
        }

        @NonNull
        @Override
        public View getRoot() {
            return view;
        }
    }

    @Override
    public ViewBinding createBinding(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null)
            mInflater = LayoutInflater.from(parent.getContext());
        try {
            ViewDataBinding binding = createDataBinding(mInflater, parent, viewType);
            if (binding != null)
                return binding;
        } catch (Throwable ignored) {
        }
        View view = mInflater.inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    public Object createViewModel(Object item) {
        return null;
    }

    @Override
    public void bindView(ViewBinding binding, Object item, int position) {
        if (binding instanceof ViewDataBinding)
            bindView((ViewDataBinding) binding, item, position);
        else
            bindView(binding.getRoot(), item, position);
    }

    protected ViewDataBinding createDataBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(inflater, viewType, parent, false);
    }

    @CallSuper
    protected void bindView(ViewDataBinding binding, Object item, int position) {
        if (mBrData != 0)
            binding.setVariable(mBrData, item);
        if (mBrViewModel != 0) {
            Object vm = itemViewModels.get(item);
            if (vm == null) {
                vm = createViewModel(item);
                if (vm == null)
                    vm = this; // no view model
                itemViewModels.put(item, vm);
            }
            if (vm != this)
                binding.setVariable(mBrViewModel, vm);
        }
    }

    protected void bindView(View view, Object item, int position) {
    }

}
