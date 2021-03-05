package com.xhb.uibase.demo.checkboxes;

import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("cmguo")
public class RatioButtonsComponent extends CheckBoxesComponent {

    @Override
    public int id() {
        return R.id.component_radios;
    }

    @Override
    public int title() {
        return R.string.component_radios;
    }

    @Override
    public int description() {
        return R.string.component_radios_desc;
    }

}

