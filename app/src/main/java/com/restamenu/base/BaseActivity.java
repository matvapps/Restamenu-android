package com.restamenu.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.restamenu.R;

/**
 * @author Roodie
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected FrameLayout container;
    protected Toolbar toolbar;
    //protected TextView toolbarTitle;

    protected CoordinatorLayout coordinatorLayout;
    protected AppBarLayout appBarLayout;

    @LayoutRes
    protected int getBaseViewLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getBaseViewLayoutId());

        container = findViewById(R.id.container);

        toolbar = findViewById(R.id.toolbar);
        //toolbarTitle = findViewById(R.id.toolbar_title);

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
    }

    protected void setContentFrame() {
        getLayoutInflater().inflate(getContentViewLayoutId(), container);
    }

    /**
     * Implement this to instantiate your container views.
     */
    protected abstract void initViews();

    /**
     * Override this if you want to set up the container content.
     */
    @LayoutRes
    protected abstract int getContentViewLayoutId();


    /**
     * Override this to add additional buttons/textViews to toolbar or set up toolbar.
     */
    protected void configureToolbar() {

    }

    public int getToolbarIcon() {
        return R.drawable.ic_back;
    }
}
