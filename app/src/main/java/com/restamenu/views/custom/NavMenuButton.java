package com.restamenu.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.util.TextUtils;

/**
 * Created by Alexandr
 */


/**
 * NOT USED, should be deleted
 */
public class NavMenuButton extends LinearLayout {

    private TextView title;
    //private View container;
    private OnNavMenuItemClick navMenuItemClick;
    private String titleText;


    public NavMenuButton(Context context) {
        this(context, null);
    }

    public NavMenuButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.nav_list_item, this, true);

        //container = findViewById(R.id.nav_list_text_container);
        title = findViewById(R.id.nav_list_text);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavMenuButton);
        titleText = a.getString(R.styleable.NavMenuButton_menu_title);

        if (!TextUtils.isEmpty(titleText)) {
            title.setText(titleText);
        }

        setDefaults();

        a.recycle();

    }

    public OnNavMenuItemClick getNavMenuItemClick() {
        return navMenuItemClick;
    }

    public void setNavMenuItemClick(OnNavMenuItemClick navMenuItemClick) {
        this.navMenuItemClick = navMenuItemClick;

        this.setOnClickListener(view -> navMenuItemClick.onNavMenuItemClick(titleText));

    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    private void setDefaults() {
        setSelected(false);
        //container.setBackgroundColor(Color.TRANSPARENT);
        title.setTextColor(ContextCompat.getColor(getContext(), R.color.warm_grey));
        setBackgroundResource(0);
    }

    public void setMenuItemSelected(boolean selected) {
        if (selected) {
            setSelected(true);
            //container.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.greyish));
            title.setTextColor(ContextCompat.getColor(getContext(), R.color.cerulean));
            setBackgroundColor(Color.TRANSPARENT);
            setBackgroundResource(R.drawable.rect_stroke_grey);
        } else {
            setDefaults();
        }
    }


    public interface OnNavMenuItemClick {
        void onNavMenuItemClick(String title);
    }

}
