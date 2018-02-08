package com.restamenu.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;


public class AndroidUtils {

    public static float dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit.
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float pixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    /*
     * Create a color from [$color].RGB and then add an alpha with 255*[$percent]
     */
    public static int colorWithAlpha(int color, float percent) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        int alpha = Math.round(percent * 255);

        return Color.argb(alpha, r, g, b);
    }

    public static float minMax(float min, float value, float max) {
        value = Math.min(value, max);
        value = Math.max(min, value);
        return value;
    }

    /**
     * modify the scale of multiples views
     *
     * @param scale the new scale
     * @param views
     */
    public static void setScale(float scale, View... views) {
        for (View view : views) {
            if (view != null) {
                ViewCompat.setScaleX(view, scale);
                ViewCompat.setScaleY(view, scale);
            }
        }
    }

    /**
     * modify the elevation of multiples views
     *
     * @param elevation the new elevation
     * @param views
     */
    public static void setElevation(float elevation, View... views) {
        for (View view : views) {
            if (view != null) {
                ViewCompat.setElevation(view, elevation);
            }
        }
    }

    /**
     * modify the backgroundcolor of multiples views
     *
     * @param color the new backgroundcolor
     * @param views
     */
    public static void setBackgroundColor(int color, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setBackgroundColor(color);
            }
        }
    }

    public static boolean canScroll(View view) {
        if (view instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) view;
            View child = scrollView.getChildAt(0);
            if (child != null) {
                int childHeight = child.getHeight();
                return scrollView.getHeight() < childHeight + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
            }
            return false;
        } else if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            int yOffset = recyclerView.computeVerticalScrollOffset();
            return yOffset != 0;
        }
        return true;
    }

    public static void scrollTo(Object scroll, float yOffset) {
        if (scroll instanceof RecyclerView) {
            //RecyclerView.scrollTo : UnsupportedOperationException
            //Moved to the RecyclerView.LayoutManager.scrollToPositionWithOffset
            //Have to be instanceOf RecyclerView.LayoutManager to work (so work with RecyclerView.GridLayoutManager)
            final RecyclerView.LayoutManager layoutManager = ((RecyclerView) scroll).getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                linearLayoutManager.scrollToPositionWithOffset(0, (int) -yOffset);
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                staggeredGridLayoutManager.scrollToPositionWithOffset(0, (int) -yOffset);
            }
        } else if (scroll instanceof NestedScrollView) {
            ((NestedScrollView) scroll).scrollTo(0, (int) yOffset);
        }
    }

    public static View getTheVisibileView(List<View> viewList) {
        Rect scrollBounds = new Rect();

        int listSize = viewList.size();
        for (int i = 0; i < listSize; ++i) {
            View view = viewList.get(i);
            if (view != null) {
                view.getHitRect(scrollBounds);
                if (view.getLocalVisibleRect(scrollBounds)) {
                    return view;
                }
            }
        }
        return null;
    }

    public  static void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }



}
