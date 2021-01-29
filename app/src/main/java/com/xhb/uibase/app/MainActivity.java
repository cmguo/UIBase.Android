package com.xhb.uibase.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.xhb.uibase.app.ui.main.ComponentsFragment;
import com.xhb.uibase.demo.Component;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.xhb.uibase.app.R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.components_container, new ComponentsFragment())
                .commitNow();
    }

    private void switchController(Component controller) {
        getSupportFragmentManager().beginTransaction()
                .replace(com.xhb.uibase.app.R.id.control_container, controller)
                .commitNow();
    }

}