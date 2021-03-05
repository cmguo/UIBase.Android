package com.eazy.uibase.demo.checkboxes;

import com.google.auto.service.AutoService;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("cmguo")
public class CheckBoxesComponent implements Component{
    @Override
    public int id() {
        return R.id.component_check_boxes;
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
        return R.string.component_checkboxes;
    }

    @Override
    public int description() {
        return R.string.component_checkboxes_desc;
    }

    @Override
    public Class<? extends ComponentFragment> fragmentClass() {
        return CheckBoxesFragment.class;
    }

}

