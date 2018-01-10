package com.restamenu.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.views.dialog.LoadingDialog;
import com.restamenu.views.dialog.LoadingView;

/**
 * @author Roodie
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected LoadingView loadingView;
    protected FrameLayout container;
    protected Toolbar toolbar;
    protected TextView toolbarTitle;

    protected CoordinatorLayout coordinatorLayout;
    protected AppBarLayout appBarLayout;

    @LayoutRes
    protected int getBaseViewLayoutId() {
        return R.layout.activity_base;
    }

    @LayoutRes
    protected int getToolbarLayoutId() {
        return R.layout.include_toolbar_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getBaseViewLayoutId());

        container = findViewById(R.id.container);

        ViewStub toolbarStub = findViewById(R.id.toolbar_stub);
        toolbarStub.setLayoutResource(getToolbarLayoutId());
        toolbarStub.inflate();

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);


        appBarLayout = findViewById(R.id.appbar);
        coordinatorLayout = findViewById(R.id.main_content);

        setContentFrame();

        boolean isLargeLayout = getResources().getBoolean(R.bool.isLargeLayout);
        if(isLargeLayout) {
            // Tablet Mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            // Handset Mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        initViews();
        configureToolbar();

        loadingView = LoadingDialog.view(getSupportFragmentManager());
    }

    protected void setContentFrame() {
        getLayoutInflater().inflate(getContentViewLayoutId(), container);
    }

    /**
     * Implement this to instantiate your container views.
     */
    protected abstract void initViews();

    /**
     * Override this if you want to change up the container content.
     */
    @LayoutRes
    protected abstract int getContentViewLayoutId();

    protected abstract boolean showToolbarBackStack();


    public int getToolbarIcon() {
        return R.drawable.ic_burger_menu;
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isLargeLayout);
    }

    protected void configureToolbar() {
        if (toolbar != null) {
            toolbar.setNavigationIcon(getToolbarIcon());
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
            if (showToolbarBackStack()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
            }
        }
    }
}
