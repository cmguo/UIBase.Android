package com.xhb.uibase.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewbinding.ViewBinding;

import com.xhb.uibase.BR;

public abstract class BaseItemBinding<T> implements ItemBinding<T> {

    private LayoutInflater mInflater;

    public BaseItemBinding() {
    }

    public BaseItemBinding(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder implements ViewBinding {

        private final View view;

        ViewHolder(View view) {
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

    @Override
    public void bindView(ViewBinding binding, T item, int position) {
        if (binding instanceof ViewDataBinding)
            bindView((ViewDataBinding) binding, item, position);
        else
            bindView(binding.getRoot(), item, position);
    }

    protected ViewDataBinding createDataBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(inflater, viewType, parent, false);
    }

    @CallSuper
    protected void bindView(ViewDataBinding binding, T item, int position) {
        binding.setVariable(BR.data, item);
    }

    protected void bindView(View view, T item, int position) {
    }

}
