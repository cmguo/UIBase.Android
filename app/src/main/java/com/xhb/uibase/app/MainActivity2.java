package com.xhb.uibase.app;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.ComponentFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private static final String TAG = "MainActivity2";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Map<Integer, List<Component>> components = Component.collectComponents();
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
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.component_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void buildNavMenu(Menu menu, Map<Integer, List<Component>> components) {
        for (Map.Entry<Integer, List<Component>> g : components.entrySet()) {
            SubMenu sm = menu.addSubMenu(g.getKey());
            for (Component c : g.getValue()) {
                Log.d(TAG, "buildNavMenu id=" + c.id() + ", title=" + c.title());
                sm.add(0, c.id(), 0, c.title()).setIcon(c.icon());
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
}