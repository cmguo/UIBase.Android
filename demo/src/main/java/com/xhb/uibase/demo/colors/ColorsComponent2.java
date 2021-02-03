package com.xhb.uibase.demo.colors;

import android.util.Log;

import com.google.auto.service.AutoService;
import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.Author;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.core.Generic;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyle;
import com.xhb.uibase.demo.databinding.ColorsBinding;

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
