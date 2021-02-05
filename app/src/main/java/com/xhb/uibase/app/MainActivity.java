package com.xhb.uibase.app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;

import com.xhb.uibase.app.ui.main.ComponentsFragment;
import com.xhb.uibase.app.ui.main.StylesFragment;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.test.TestComponent;

public class MainActivity extends AppCompatActivity {

    private static final Rect RECT = new Rect();
    private static final int[] LOCATION = new int[2];
    private static final MotionEvent.PointerCoords COORDS = new MotionEvent.PointerCoords();

    private ComponentsFragment componetsFragment = new ComponentsFragment();
    private StylesFragment stylesFragment = new StylesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.xhb.uibase.app.R.layout.activity_main);
        switchComponent(new TestComponent());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.components_container, componetsFragment)
                .commitNow();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.styles_container, stylesFragment)
                .hide(stylesFragment)
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
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            ev.getPointerCoords(0, COORDS);
            componetsFragment.getView().getDrawingRect(RECT);
            componetsFragment.getView().getLocationInWindow(LOCATION);
            if (!RECT.contains((int) COORDS.x - LOCATION[0], (int) COORDS.y - LOCATION[1]))
                hideComponents();
            stylesFragment.getView().getDrawingRect(RECT);
            stylesFragment.getView().getLocationInWindow(LOCATION);
            if (!RECT.contains((int) COORDS.x - LOCATION[0], (int) COORDS.y - LOCATION[1]))
                hideStyles();
        }
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

    private void showStyles() {
        getSupportFragmentManager().beginTransaction()
                .show(stylesFragment)
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .commitNow();
    }

    private void hideStyles() {
        getSupportFragmentManager().beginTransaction()
                .hide(stylesFragment)
                .setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
                .commitNow();
    }

    public void switchComponent(FragmentComponent component) {
        getSupportFragmentManager().beginTransaction()
                .replace(com.xhb.uibase.app.R.id.component_container, component)
                .commitNow();
        stylesFragment.bindComponent(component);
        showStyles();
    }

}