package com.restamenu.views.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Alexandr.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int spaceTop;
    private int spaceBtm;
    private int spaceLeft;
    private int spaceRight;

    public SpacesItemDecoration(int spaceTop, int spaceBtm, int spaceLeft, int spaceRight) {
        this.spaceTop = spaceTop;
        this.spaceBtm = spaceBtm;
        this.spaceLeft = spaceLeft;
        this.spaceRight = spaceRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = spaceLeft;
        outRect.right = spaceRight;
        outRect.bottom = spaceBtm;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = spaceTop;
        } else {
            outRect.top = 0;
        }
    }
}
