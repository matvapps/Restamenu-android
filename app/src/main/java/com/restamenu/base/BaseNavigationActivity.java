package com.restamenu.base;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

/**
 * @author Roodie
 */

public abstract class BaseNavigationActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected int getBaseViewLayoutId() {
        return R.layout.activity_base_navigation;
    }

    @Override
    public int getToolbarIcon() {
        return R.drawable.ic_menu;
    }

    @Override
    protected void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

    }
}
