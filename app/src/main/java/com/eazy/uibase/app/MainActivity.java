package com.eazy.uibase.app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;

import com.eazy.uibase.app.ui.main.ComponentsFragment;
import com.eazy.uibase.demo.core.FragmentComponent;
import com.eazy.uibase.demo.test.TestComponent;

public class MainActivity extends AppCompatActivity {

    private static final Rect RECT = new Rect();
    private static final MotionEvent.PointerCoords COORDS = new MotionEvent.PointerCoords();

    private ComponentsFragment componetsFragment = new ComponentsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.eazy.uibase.app.R.layout.activity_main);
        switchComponent(new TestComponent());
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.components_container, componetsFragment)
            .commitNow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showComponents();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        ev.getPointerCoords(0, COORDS);
        componetsFragment.getView().getDrawingRect(RECT);
        if (!RECT.contains((int) COORDS.x, (int) COORDS.y))
            hideComponents();
        return result;
    }

    private void showComponents() {
        getSupportFragmentManager().beginTransaction()
                .show(componetsFragment)
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .commitNow();
    }

    private void hideComponents() {
        getSupportFragmentManager().beginTransaction()
                .hide(componetsFragment)
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .commitNow();
    }

    public void switchComponent(FragmentComponent component) {
        getSupportFragmentManager().beginTransaction()
                .replace(com.eazy.uibase.app.R.id.component_container, component)
                .commitNow();
    }

}