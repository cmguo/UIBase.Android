package com.eazy.uibase.demo.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;

import com.ustc.base.util.reflect.ClassWrapper;
import com.ustc.base.util.reflect.ObjectWrapper;

import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

public abstract class ComponentFragment<DataBinding extends ViewDataBinding,
        Model extends ViewModel, Styles extends ViewStyles
        > extends Fragment implements SkinObserver {

    Component component_;
    DataBinding binding_;
    Model model_;
    Styles styles_;

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
        SkinManager.addObserver(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding_ = createDataBinding(inflater);
        binding_.setLifecycleOwner(this);
        model_ = createModel();
        styles_ = createStyle();
        binding_.setVariable(BR.fragment, this);
        binding_.setVariable(BR.model, model_);
        binding_.setVariable(BR.styles, styles_);
        return binding_.getRoot();
    }

    @Override
    public void onDestroy() {
        SkinManager.removeObserver(this);
        super.onDestroy();
    }

    public DataBinding getBinding() {
        return binding_;
    }

    public Model getModel() {
        return model_;
    }

    public Styles getStyles() {
        return styles_;
    }

    protected Class<DataBinding> getDataBindingType() {
        return Generic.getParamType(getClass(), ComponentFragment.class, 0);
    }

    protected Class<Model> getModelType() {
        return Generic.getParamType(getClass(), ComponentFragment.class, 1);
    }

    protected Class<Styles> getStyleType() {
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
        ClassWrapper<?> wrapper = ClassWrapper.wrap(clzM);
        if (wrapper.hasConstructor(getClass())) {
            return ClassWrapper.wrap(clzM).newInstance(this);
        } else {
            return ClassWrapper.wrap(clzM).newInstance();
        }
    }

    protected Styles createStyle() {
        Class<Styles> clzS = getStyleType();
        ClassWrapper<?> wrapper = ClassWrapper.wrap(clzS);
        if (wrapper.hasConstructor(getClass())) {
            return ClassWrapper.wrap(clzS).newInstance(this);
        } else {
            return ClassWrapper.wrap(clzS).newInstance();
        }
    }

    @Override
    public void updateSkin(SkinObservable observable, Object o) {
    }
}
