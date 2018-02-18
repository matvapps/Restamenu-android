package com.restamenu.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.restamenu.R;

/**
 * @author Roodie
 */

public class RestaurantUtils {

    public static String getServiceTitle(Context context, int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.service_restaurant);
            case 2:
                return context.getString(R.string.service_takeaway);
            case 3:
                return context.getString(R.string.service_delivery);
            default:
                return context.getString(R.string.service_restaurant);
        }
    }


    public static Drawable getServiceImage(Context context, int position) {
        switch (position) {
            case 0:
                return ContextCompat.getDrawable(context, R.drawable.ic_eat_noactive);
            case 1:
                return ContextCompat.getDrawable(context, R.drawable.ic_drink_noactive);
            case 2:
                return ContextCompat.getDrawable(context, R.drawable.ic_delivery_noactive);
            default:
                return ContextCompat.getDrawable(context, R.drawable.ic_eat_noactive);
        }
    }
}
