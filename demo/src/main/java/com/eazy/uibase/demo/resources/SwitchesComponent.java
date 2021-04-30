package com.eazy.uibase.demo.resources;

import com.google.auto.service.AutoService;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("cmguo")
public class SwitchesComponent extends CheckBoxesComponent {

    @Override
    public int id() {
        return R.id.component_switches;
    }

    @Override
    public int title() {
        return R.string.component_switches;
    }

    @Override
    public int description() {
        return R.string.component_switches_desc;
    }

}
