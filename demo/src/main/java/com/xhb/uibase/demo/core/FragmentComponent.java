package com.xhb.uibase.demo.core;

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

public abstract class FragmentComponent<DataBinding extends ViewDataBinding,
        Model extends ViewModel, Style extends ViewStyle
        > extends Fragment implements Component {

    private int layoutId_;
    DataBinding binding_;
    Model model_;
    Style style_;

    protected FragmentComponent(int layoutId) {
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
        model_ = createModel();
        style_ = createStyle();
        ObjectWrapper.wrap(binding).invoke("setModel", model_);
        ObjectWrapper.wrap(binding).invoke("setStyle", style_);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public ViewStyle style() {
        return style_;
    }

    private Class<DataBinding> getDataBindingType() {
        return Generic.getParamType(getClass(), 0);
    }

    private Class<Model> getModelType() {
        return Generic.getParamType(getClass(), 1);
    }

    private Class<Style> getStyleType() {
        return Generic.getParamType(getClass(), 2);
    }

    protected DataBinding createDataBinding(@NonNull LayoutInflater inflater) {
        Class<DataBinding> clzB = getDataBindingType();
        if (clzB == ViewDataBinding.class) {
            return null;
        }
        return ClassWrapper.wrap(getDataBindingType()).invoke("inflate",
                ObjectWrapper.wrap(LayoutInflater.class, inflater));
    }

    protected Model createModel() {
        Class<Model> clzM = getModelType();
        return ClassWrapper.wrap(clzM).newInstance();
    }

    protected Style createStyle() {
        Class<Style> clzS = getStyleType();
        return ClassWrapper.wrap(clzS).newInstance();
    }
}
