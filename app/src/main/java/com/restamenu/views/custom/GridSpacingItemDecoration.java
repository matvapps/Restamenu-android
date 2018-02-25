package com.restamenu.views.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Alexandr.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacingHoriz;
    private int spacingVert;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacingHoriz, int spacingVert, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacingVert = spacingVert;
        this.spacingHoriz = spacingHoriz;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacingHoriz - column * spacingHoriz / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacingHoriz / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacingVert;
            }
            outRect.bottom = spacingVert; // item bottom
        } else {
            outRect.left = column * spacingHoriz / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacingHoriz - (column + 1) * spacingHoriz / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacingVert; // item top
            }
        }
    }
}