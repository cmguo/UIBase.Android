package com.eazy.uibase.demo.resources;

import com.google.auto.service.AutoService;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("cmguo")
public class DynamicImagesComponent implements Component
{
    @Override
    public int id() {
        return R.id.component_dynamic_images;
    }

    @Override
    public int group() {
        return R.string.group_resource_styles;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_star;
    }

    @Override
    public int title() {
        return R.string.component_dynamic_images;
    }

    @Override
    public int description() {
        return R.string.component_dynamic_images_desc;
    }

    @Override
    public Class<? extends ComponentFragment> fragmentClass() {
        return DrawablesFragment.class;
    }
}

