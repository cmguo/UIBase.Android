package com.eazy.uibase.demo.core;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public abstract class FragmentComponent<DataBinding extends ViewDataBinding,
        Model extends ViewModel, Style extends ViewStyles
        > extends Fragment implements Component {

    public static Map<Integer, List<FragmentComponent>> collectComponents() {
        Map<Integer, List<FragmentComponent>> components = new TreeMap<>();
        for (Component controller : ServiceLoader.load(Component.class)) {
            int g = controller.group();
            int n = controller.title();
            List<FragmentComponent> group = components.get(g);
            if (group == null) {
                group = new ArrayList<>();
                components.put(g, group);
            }
            if (controller instanceof FragmentComponent)
                group.add((FragmentComponent) controller);
        }
        return components;
    }

    DataBinding binding_;
    Model model_;
    Style style_;

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
        ObjectWrapper<DataBinding> wrapper = ObjectWrapper.wrap(binding);
        if (wrapper.hasMethod("setComponent", getClass()))
            ObjectWrapper.wrap(binding).invoke("setComponent", this);
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

    @Override
    public ViewStyles styles() {
        return style_;
    }

    protected Class<DataBinding> getDataBindingType() {
        return Generic.getParamType(getClass(), FragmentComponent.class, 0);
    }

    protected Class<Model> getModelType() {
        return Generic.getParamType(getClass(), FragmentComponent.class, 1);
    }

    protected Class<Style> getStyleType() {
        return Generic.getParamType(getClass(), FragmentComponent.class, 2);
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
