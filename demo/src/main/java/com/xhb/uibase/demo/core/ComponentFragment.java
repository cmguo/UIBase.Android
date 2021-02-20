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

public abstract class ComponentFragment<DataBinding extends ViewDataBinding,
        Model extends ViewModel, Style extends ViewStyles
        > extends Fragment {

    Component component_;
    DataBinding binding_;
    Model model_;
    Style style_;

    protected void setComponent(Component component_) {
        this.component_ = component_;
    }

    public Component getComponent() {
        return component_;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Integer componentId = getArguments().getInt("componentId");
        if (componentId != null)
            component_ = Components.getComponent(componentId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DataBinding binding = createDataBinding(inflater);
        binding.setLifecycleOwner(this);
        model_ = createModel();
        style_ = createStyle();
        ObjectWrapper<DataBinding> wrapper = ObjectWrapper.wrap(binding);
        if (wrapper.hasMethod("setFragment", getClass()))
            ObjectWrapper.wrap(binding).invoke("setFragment", this);
        if (wrapper.hasMethod("setModel", model_.getClass()))
            ObjectWrapper.wrap(binding).invoke("setModel", model_);
        if (wrapper.hasMethod("setStyle", style_.getClass()))
            ObjectWrapper.wrap(binding).invoke("setStyle", style_);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public DataBinding getBinding() {
        return binding_;
    }

    public Model getModel() {
        return model_;
    }

    public ViewStyles getStyles() {
        return style_;
    }

    protected Class<DataBinding> getDataBindingType() {
        return Generic.getParamType(getClass(), ComponentFragment.class, 0);
    }

    protected Class<Model> getModelType() {
        return Generic.getParamType(getClass(), ComponentFragment.class, 1);
    }

    protected Class<Style> getStyleType() {
        return Generic.getParamType(getClass(), ComponentFragment.class, 2);
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
        ClassWrapper wrapper = ClassWrapper.wrap(clzM);
        if (wrapper.hasConstructor(getClass())) {
            return ClassWrapper.wrap(clzM).newInstance(this);
        } else {
            return ClassWrapper.wrap(clzM).newInstance();
        }
    }

    protected Style createStyle() {
        Class<Style> clzS = getStyleType();
        ClassWrapper wrapper = ClassWrapper.wrap(clzS);
        if (wrapper.hasConstructor(getClass())) {
            return ClassWrapper.wrap(clzS).newInstance(this);
        } else {
            return ClassWrapper.wrap(clzS).newInstance();
        }
    }
}
