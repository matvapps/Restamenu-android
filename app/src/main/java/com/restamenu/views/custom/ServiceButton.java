package com.restamenu.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.util.Logger;
import com.restamenu.util.TextUtils;

/**
 * @author Roodie
 */

public class ServiceButton extends RelativeLayout {

    private final TextView title;
    private final ImageView image;
    private final TextView isAvailable;
    //private final View container;

    private boolean isServiceAvailable;

    public ServiceButton(Context context) {
        this(context, null);
    }

    public ServiceButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ServiceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.item_service_button, this, true);

        //container = findViewById(R.id.container);
        title = findViewById(R.id.title);
        image = findViewById(R.id.image);
        isAvailable = findViewById(R.id.is_available);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ServiceButton);
        final String titleText = a.getString(R.styleable.ServiceButton_service_title);

        final Drawable drawable = a.getDrawable(R.styleable.ServiceButton_service_icon);
        if (drawable != null) {
            image.setImageDrawable(drawable);
        }

        if (!TextUtils.isEmpty(titleText)) {
            title.setText(titleText);
        }

        setDefaults();

        a.recycle();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setImage(Drawable image) {
        this.image.setImageDrawable(image);
    }

    private void setDefaults() {
        setSelected(false);
        isServiceAvailable = true;
        image.setScaleX(1);
        image.setScaleY(1);
        title.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        isAvailable.setVisibility(View.GONE);
        isAvailable.setText("");
    }

    public void setServiceSelected(boolean selected) {
        if (selected) {
            setSelected(true);
            title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            image.setScaleX(1.35f);
            image.setScaleY(1.35f);
        } else {
            setDefaults();
        }
    }

    public void setAvailable(boolean available) {
        Logger.log("Is available: " + available);
        if (!available) {
            isServiceAvailable = false;
            //container.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            title.setTextColor(ContextCompat.getColor(getContext(), R.color.semi_grey));
            isAvailable.setTextColor(ContextCompat.getColor(getContext(), R.color.semi_grey));
            isAvailable.setVisibility(View.VISIBLE);
            isAvailable.setText(getContext().getString(R.string.service_not_available));
        } else {
            isServiceAvailable = true;
            setSelected(false);
        }

    }


    /**
     * If flag not available - disable all touch events.
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Logger.log("Event: return: " + super.onTouchEvent(event));
        return isServiceAvailable ? true : false;
    }

}
