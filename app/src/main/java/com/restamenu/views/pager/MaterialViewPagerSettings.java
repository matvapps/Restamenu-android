package com.restamenu.views.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.restamenu.R;
import com.restamenu.util.AndroidUtils;

/**
 * Created by alessio.
 */

public class MaterialViewPagerSettings implements Parcelable {

    public static final Parcelable.Creator<MaterialViewPagerSettings> CREATOR = new Parcelable.Creator<MaterialViewPagerSettings>() {
        public MaterialViewPagerSettings createFromParcel(Parcel source) {
            return new MaterialViewPagerSettings(source);
        }

        public MaterialViewPagerSettings[] newArray(int size) {
            return new MaterialViewPagerSettings[size];
        }
    };
    int headerLayoutId;
    int pagerTitleStripId;
    int viewpagerId;
    int headerAdditionalHeight;
    int headerHeight;
    int headerHeightPx;
    int color;
    float headerAlpha;
    float parallaxHeaderFactor;
    float imageHeaderDarkLayerAlpha;
    //region parcelable

    public MaterialViewPagerSettings() {
    }

    private MaterialViewPagerSettings(Parcel in) {
        this.headerLayoutId = in.readInt();
        this.pagerTitleStripId = in.readInt();
        this.viewpagerId = in.readInt();
        this.headerAdditionalHeight = in.readInt();
        this.headerHeight = in.readInt();
        this.headerHeightPx = in.readInt();
        this.color = in.readInt();
        this.headerAlpha = in.readFloat();
        this.imageHeaderDarkLayerAlpha = in.readFloat();
        this.parallaxHeaderFactor = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.headerLayoutId);
        dest.writeInt(this.pagerTitleStripId);
        dest.writeInt(this.viewpagerId);
        dest.writeInt(this.headerAdditionalHeight);
        dest.writeInt(this.headerHeight);
        dest.writeInt(this.headerHeightPx);
        dest.writeInt(this.color);
        dest.writeFloat(this.headerAlpha);
        dest.writeFloat(this.imageHeaderDarkLayerAlpha);
        dest.writeFloat(this.parallaxHeaderFactor);
    }

    /**
     * Retrieve attributes from the MaterialViewPager
     *
     * @param context
     * @param attrs
     */
    void handleAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaterialViewPager);
            {
                headerLayoutId = styledAttrs.getResourceId(R.styleable.MaterialViewPager_viewpager_header, -1);
            }
            {
                pagerTitleStripId = styledAttrs.getResourceId(R.styleable.MaterialViewPager_viewpager_pagerTitleStrip, -1);
                if (pagerTitleStripId == -1) {
                    pagerTitleStripId = R.layout.item_pagertitlestrip;
                }
            }
            {
                viewpagerId = styledAttrs.getResourceId(R.styleable.MaterialViewPager_viewpager_viewpager, -1);
            }

            {
                color = styledAttrs.getColor(R.styleable.MaterialViewPager_viewpager_color, 0);
            }
            {
                headerHeightPx = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialViewPager_viewpager_headerHeight, 200);
                headerHeight = Math.round(AndroidUtils.pixelsToDp(headerHeightPx, context)); //convert to dp
            }
            {
                headerAdditionalHeight = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialViewPager_viewpager_headerAdditionalHeight, 60);
            }
            {
                headerAlpha = styledAttrs.getFloat(R.styleable.MaterialViewPager_viewpager_headerAlpha, 0.5f);
            }
            {
                imageHeaderDarkLayerAlpha = styledAttrs.getFloat(R.styleable.MaterialViewPager_viewpager_imageHeaderDarkLayerAlpha, 0.0f);
            }
            {
                parallaxHeaderFactor = styledAttrs.getFloat(R.styleable.MaterialViewPager_viewpager_parallaxHeaderFactor, 1.5f);
                parallaxHeaderFactor = Math.max(parallaxHeaderFactor, 1); //min=1
            }

            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion
}
