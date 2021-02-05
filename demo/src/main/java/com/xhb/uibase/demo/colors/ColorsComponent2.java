package com.xhb.uibase.demo.colors;

import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.annotation.Author;
import com.xhb.uibase.demo.core.Component;

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

}
