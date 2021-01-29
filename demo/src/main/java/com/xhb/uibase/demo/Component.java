package com.xhb.uibase.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.ustc.base.util.reflect.ClassWrapper;
import com.ustc.base.util.reflect.ObjectWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Component<DataBinding extends ViewDataBinding> extends Fragment implements DemoController {

    private int layoutId_;

    protected Component(int layoutId) {
        layoutId_ = layoutId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DataBinding binding = createDataBinding(inflater);
        binding.setLifecycleOwner(this);
        ObjectWrapper.wrap(binding).invoke("setVm", this);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public DemoSettings settings() {
        return null;
    }

    private Class<DataBinding> getDataBindingType() {
        return Generic.getParamType(getClass(), 0);
    }

    protected DataBinding createDataBinding(@NonNull LayoutInflater inflater) {
        Class<DataBinding> clzB = getDataBindingType();
        if (clzB == ViewDataBinding.class) {
            return null;
        }
        return ClassWrapper.wrap(getDataBindingType()).invoke("inflate",
                ObjectWrapper.wrap(LayoutInflater.class, inflater));
    }

}
