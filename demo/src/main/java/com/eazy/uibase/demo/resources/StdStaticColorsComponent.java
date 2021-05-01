package com.eazy.uibase.demo.resources;

import com.google.auto.service.AutoService;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.annotation.Author;
import com.eazy.uibase.demo.core.Component;

@AutoService(Component.class)
@Author("cmguo")
public class StdStaticColorsComponent extends StdDynamicColorsComponent {

    @Override
    public int id() {
        return R.id.component_std_static_colors;
    }

    @Override
    public int title() {
        return R.string.component_std_static_colors;
    }

    @Override
    public int description() {
        return R.string.component_std_static_colors_desc;
    }

}
