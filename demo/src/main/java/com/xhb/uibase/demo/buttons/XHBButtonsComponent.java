package com.xhb.uibase.demo.buttons;

import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("Fish")
public class XHBButtonsComponent implements Component {
    @Override
    public int id() {
        return R.id.xhb_component_buttons;
    }

    @Override
    public int group() {
        return R.string.group_styles;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_plus;
    }

    @Override
    public int title() {
        return R.string.component_xhb_buttons;
    }

    @Override
    public int description() {
        return R.string.component_xhb_buttons_des;
    }

    @Override
    public Class<? extends ComponentFragment> fragmentClass() {
        return XHBButtonsFragment.class;
    }

}

