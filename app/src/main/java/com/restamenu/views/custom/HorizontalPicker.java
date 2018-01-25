package com.restamenu.views.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.restamenu.R;

/**
 * Created by Alexandr.
 */

public class HorizontalPicker extends LinearLayout implements View.OnClickListener{

    private int quantity = 1;

    private TextView quantityText;

    private View buttonAdd;
    private View buttonSub;

    public HorizontalPicker(Context context) {
        super(context);
        initView();
    }

    public HorizontalPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HorizontalPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        inflate(getContext(), R.layout.product_picker, this);

        quantityText = findViewById(R.id.product_quantity_text);

        buttonAdd = findViewById(R.id.btn_add);
        buttonSub = findViewById(R.id.btn_sub);

        buttonSub.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add: {
                quantity++;
                quantityText.setText(String.valueOf(quantity));
                break;
            }
            case R.id.btn_sub: {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
                break;
            }
        }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
