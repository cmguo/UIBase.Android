package com.xhb.uibase.demo.buttons;

import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("cmguo")
public class ButtonsComponent2 extends ButtonsComponent {

    @Override
    public int id() {
        return R.id.component_buttons2;
    }

    @Override
    public int title() {
        return R.string.component_buttons2;
    }

    @Override
    public int description() {
        return R.string.component_buttons2_desc;
    }

}

