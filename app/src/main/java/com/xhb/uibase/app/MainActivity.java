package com.xhb.uibase.app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;

import com.xhb.uibase.app.ui.main.ComponentsFragment;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.test.TestComponent;

public class MainActivity extends AppCompatActivity {

    private static final Rect RECT = new Rect();
    private static final MotionEvent.PointerCoords COORDS = new MotionEvent.PointerCoords();

    private ComponentsFragment componetsFragment = new ComponentsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.xhb.uibase.app.R.layout.activity_main);
        switchComponent(new TestComponent());
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
                .replace(R.id.components_container, componetsFragment)
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .commitNow();
    }

    private void hideComponents() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.components_container, null)
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .commitNow();
    }

    public void switchComponent(FragmentComponent component) {
        getSupportFragmentManager().beginTransaction()
                .replace(com.xhb.uibase.app.R.id.component_container, component)
                .commitNow();
    }

}