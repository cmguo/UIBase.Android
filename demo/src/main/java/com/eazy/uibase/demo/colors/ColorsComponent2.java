package com.eazy.uibase.demo.colors;

import android.util.Log;

import com.google.auto.service.AutoService;
import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Author;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.FragmentComponent;
import com.eazy.uibase.demo.core.Generic;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyle;
import com.eazy.uibase.demo.databinding.ColorsBinding;

import java.util.Map;

@AutoService(Component.class)
@Author("cmguo")
public class ColorsComponent2 extends ColorsComponent {

    @Override
    public int title() {
        return R.string.component_colors2;
    }

    @Override
    public int description() {
        return R.string.component_colors2_desc;
    }

    @Override
    protected Class<ColorsBinding> getDataBindingType() {
        return ColorsBinding.class;
    }

    @Override
    protected Class<Model> getModelType() {
        return Model.class;
    }

    @Override
    protected Class<Style> getStyleType() {
        return Style.class;
    }

}
