package com.xhb.uibase.demo.styles;

import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("cmguo")
public class RatioButtonsComponent extends CheckBoxesComponent {

    @Override
    public int id() {
        return R.id.component_radio_buttons;
    }

    @Override
    public int title() {
        return R.string.component_radio_buttons;
    }

    @Override
    public int description() {
        return R.string.component_radio_buttons_desc;
    }

}

