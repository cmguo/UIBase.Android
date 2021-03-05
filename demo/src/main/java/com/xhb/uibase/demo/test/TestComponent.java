package com.xhb.uibase.demo.test;

import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.annotation.Author;

@AutoService(Component.class)
@Author("cmguo")
public class TestComponent implements Component
{
    @Override
    public int id() {
        return R.id.component_test;
    }

    @Override
    public int group() {
        return R.string.group_test;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_radio;
    }

    @Override
    public int title() {
        return R.string.component_test;
    }

    @Override
    public int description() {
        return R.string.component_test_desc;
    }

    @Override
    public Class<? extends ComponentFragment> fragmentClass() {
        return TestFragment.class;
    }
}

