package com.xhb.uibase.demos;


import com.google.auto.service.AutoService;
import com.xhb.uibase.demo.DemoController;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.Component;
import com.xhb.uibase.demo.databinding.DemoTestBinding;

@AutoService(DemoController.class)
public class TestComponent extends Component<DemoTestBinding> {

    public TestComponent() {
        super(R.layout.demo_test);
    }

    @Override
    public int group() {
        return R.string.group_test;
    }

    @Override
    public int name() {
        return R.string.component_test;
    }
}
