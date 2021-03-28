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

public abstract class BaseItemBinding<T> implements ItemBinding<T> {

    private LayoutInflater mInflater;

    public BaseItemBinding() {
    }

    public BaseItemBinding(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder implements ViewBinding {

        private View view;

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
        } catch (Throwable e) {
        }
        View view = mInflater.inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindView(ViewBinding binding, T item, int position) {
        if (binding instanceof ViewDataBinding)
            bindView((ViewDataBinding) binding, item, position);
    }

    protected ViewDataBinding createDataBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(mInflater, viewType, parent, false);
    }

    @CallSuper
    protected void bindView(ViewDataBinding binding, T item, int position) {
        ((ViewDataBinding) binding).setVariable(BR.data, item);
    }

    protected void bindView(View view, T item, int position) {
    }

}
