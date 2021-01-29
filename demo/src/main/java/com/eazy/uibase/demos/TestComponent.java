package com.eazy.uibase.demos;


import com.google.auto.service.AutoService;
import com.eazy.uibase.demo.DemoController;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.Component;
import com.eazy.uibase.demo.databinding.DemoTestBinding;

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
