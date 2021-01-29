package com.eazy.uibase.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.eazy.uibase.app.ui.main.ComponentsFragment;
import com.eazy.uibase.demo.Component;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.eazy.uibase.app.R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.components_container, new ComponentsFragment())
                .commitNow();
    }

    private void switchController(Component controller) {
        getSupportFragmentManager().beginTransaction()
                .replace(com.eazy.uibase.app.R.id.control_container, controller)
                .commitNow();
    }

}