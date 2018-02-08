package com.restamenu.views.pager;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;

import com.restamenu.util.AndroidUtils;

public class MaterialViewPagerHeader {

    //positions used to animate views during scroll
    public float finalTabsY;
    protected Context context;
    protected View toolbarLayout;
    protected View mPagerSlidingTabStrip;
    protected View headerBackground;

    public MaterialViewPagerHeader(View pagerSlidingTabStrip) {
        this.mPagerSlidingTabStrip = pagerSlidingTabStrip;

        mPagerSlidingTabStrip.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                finalTabsY = AndroidUtils.dpToPixel(-2, context);

                mPagerSlidingTabStrip.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        this.context = pagerSlidingTabStrip.getContext();
        this.toolbarLayout = (View) pagerSlidingTabStrip.getParent();
    }

    public static MaterialViewPagerHeader withPagerSlidingTabStrip(View pagerSlidingTabStrip) {
        return new MaterialViewPagerHeader(pagerSlidingTabStrip);
    }

    public Context getContext() {
        return context;
    }

    public MaterialViewPagerHeader withHeaderBackground(View headerBackground) {
        this.headerBackground = headerBackground;
        return this;
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public View getHeaderBackground() {
        return headerBackground;
    }

}
