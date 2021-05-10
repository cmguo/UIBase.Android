package com.eazy.uibase.app;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.eazy.uibase.daynight.DayNightManager;
import com.eazy.uibase.demo.resources.Resources;
import com.eazy.uibase.demo.view.GridDrawable;
import com.eazy.uibase.demo.view.main.InformationFragment;
import com.eazy.uibase.demo.view.main.StylesFragment;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.Components;
import com.eazy.uibase.resources.Drawables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements NavController.OnDestinationChangedListener, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "MainActivity2";

    private AppBarConfiguration mAppBarConfiguration;

    private Fragment informationFragment;
    private Fragment stylesFragment;
    private GridDrawable gridDrawable = new GridDrawable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> getSupportFragmentManager().beginTransaction()
                .show(stylesFragment)
                .commitNow());
        Map<Integer, List<Component>> components = Components.collectComponents();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        buildNavMenu(navigationView.getMenu(), components);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(getNavTopIds(components))
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.component_fragment);
        buildNavGraph(navController, components);
        //navController.setGraph(navController.getNavigatorProvider().getNavigator());
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(this);
        Fragment componentFragment = getSupportFragmentManager().findFragmentById(R.id.component_fragment);
        componentFragment.getChildFragmentManager().addOnBackStackChangedListener(this);
        informationFragment = getSupportFragmentManager().findFragmentById(R.id.information_fragment);
        stylesFragment = getSupportFragmentManager().findFragmentById(R.id.styles_fragment);
        findViewById(R.id.component_fragment).setBackground(gridDrawable);
    }

    private static final Rect RECT = new Rect();
    private static final int[] LOCATION = new int[2];
    private static final MotionEvent.PointerCoords COORDS = new MotionEvent.PointerCoords();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            ev.getPointerCoords(0, COORDS);
            stylesFragment.getView().getDrawingRect(RECT);
            stylesFragment.getView().getLocationInWindow(LOCATION);
            if (!RECT.contains((int) COORDS.x - LOCATION[0], (int) COORDS.y - LOCATION[1])) {
                getSupportFragmentManager().beginTransaction()
                        .hide(stylesFragment)
                        .commitNow();
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        menu.findItem(R.id.action_skin_dark).setChecked(DayNightManager.getInstance().isNightMode());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_back_grid) {
            item.setChecked(!item.isChecked());
            gridDrawable.setGridOn(item.isChecked());
            return true;
        } else if (item.getItemId() == R.id.action_skin_dark) {
            item.setChecked(!item.isChecked());
            DayNightManager.getInstance().setDayNightModel(item.isChecked());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.component_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void buildNavMenu(Menu menu, Map<Integer, List<Component>> components) {
        List<Resources.ResourceValue> icons = new ArrayList(com.eazy.uibase.demo.resources.Drawables.icons(this).values());
        icons.addAll(com.eazy.uibase.demo.resources.Drawables.images(this).values());
        for (Map.Entry<Integer, List<Component>> g : components.entrySet()) {
            SubMenu sm = menu.addSubMenu(0, g.getKey(), 0, g.getKey());
            for (Component c : g.getValue()) {
                Log.d(TAG, "buildNavMenu id=" + c.id() + ", title=" + c.title() + ", icon=" + c.icon());
                int icon = c.icon();
                if (icon == 0 && !icons.isEmpty()) {
                    icon = icons.remove(0).getResId();
                }
                Drawable drawable = null;
                if (icon != 0) {
                    drawable = Drawables.getDrawable(this, icon);
                }
                sm.add(0, c.id(), 0, c.title()).setIcon(drawable);
            }
        }
    }

    private int[] getNavTopIds(Map<Integer, List<Component>> components) {
        int n = 1;
        for (List<Component> g : components.values()) {
            n += g.size();
        }
        int[] ids = new int[n];
        n = 0; ids[n++] = R.id.nav_home;
        for (List<Component> g : components.values()) {
            for (Component c : g) {
                ids[n++] = c.id();
            }
        }
        return ids;
    }

    private void buildNavGraph(NavController controller, Map<Integer, List<Component>> components) {
        NavGraph graph = controller.getGraph();
        FragmentNavigator navigator = controller.getNavigatorProvider().getNavigator("fragment");
        for (List<Component> g : components.values()) {
            for (Component c : g) {
                FragmentNavigator.Destination destination = navigator.createDestination();
                destination.setId(c.id());
                destination.setLabel(getText(c.title()));
                destination.addArgument("componentId", new NavArgument.Builder().setDefaultValue(c.id()).build());
                destination.setClassName(c.fragmentClass().getName());
                graph.addDestination(destination);
            }
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        for (int h = 0, size = menu.size(); h < size; h++) {
            MenuItem item = menu.getItem(h);
            if (item.hasSubMenu()) {
                for (int i = 0, size2 = item.getSubMenu().size(); i < size2; i++) {
                    MenuItem item2 = item.getSubMenu().getItem(i);
                    item2.setChecked(item2.getItemId() == destination.getId());
                }
           }
        }
    }

    @Override
    public void onBackStackChanged() {
        Fragment componentFragment = getSupportFragmentManager().findFragmentById(R.id.component_fragment);
        List<Fragment> fragments = ((NavHostFragment) componentFragment).getChildFragmentManager().getFragments();
        if (!fragments.isEmpty())
            componentFragment = fragments.get(fragments.size() - 1);
        if (componentFragment instanceof ComponentFragment) {
            ((InformationFragment) informationFragment).bindComponent((ComponentFragment) componentFragment);
            ((StylesFragment) stylesFragment).bindComponent((ComponentFragment) componentFragment);
        } else {
            ((InformationFragment) informationFragment).bindComponent(null);
            ((StylesFragment) stylesFragment).bindComponent(null);
        }
    }

}