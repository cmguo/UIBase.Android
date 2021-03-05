package com.eazy.uibase.demo.components.basic;

import com.google.auto.service.AutoService;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("Fish")
public class ZRadioButtonComponent implements Component {
    @Override
    public int id() {
        return R.id.component_radio_buttons;
    }

    @Override
    public int group() {
        return R.string.group_basic;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_plus;
    }

    @Override
    public int title() {
        return R.string.component_z_radiobuttons;
    }

    @Override
    public int description() {
        return R.string.component_z_radiobuttons_desc;
    }

    @Override
    public Class<? extends ComponentFragment> fragmentClass() {
        return ZRadioButtonFragment.class;
    }

}

