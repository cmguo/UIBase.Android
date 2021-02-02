package com.xhb.uibase.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.xhb.uibase.app.ui.main.ComponentsFragment;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.test.TestComponent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.xhb.uibase.app.R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.components_container, new ComponentsFragment())
                .commitNow();
        switchComponent(new TestComponent());
    }

    public void switchComponent(FragmentComponent component) {
        getSupportFragmentManager().beginTransaction()
                .replace(com.xhb.uibase.app.R.id.component_container, component)
                .commitNow();
    }

}