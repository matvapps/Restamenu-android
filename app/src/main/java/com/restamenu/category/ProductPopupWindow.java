package com.restamenu.category;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Product;
import com.restamenu.views.custom.HorizontalPicker;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Alexandr.
 */

public class ProductPopupWindow extends PopupWindow implements View.OnClickListener {

    private Context context;

    private Product product;
    private int productQuantity;
    private String categoryName;

    private View actionCancelView;
    private View actionAddToFavView;
    private View actionAddToOrderView;
    private CircleIndicator productGalleryView;
    private TextView categoryNameView;
    private TextView productNameView;
    private TextView productComponentsView;
    private TextView productDescriptionView;
    private TextView productWeightView;
    private TextView productCostView;
    private HorizontalPicker productPickerView;
    private RecyclerView additionsListView;


    public ProductPopupWindow(Context context) {
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.popup_product_item, null);

        actionCancelView = rootView.findViewById(R.id.action_cancel);
        actionAddToFavView = rootView.findViewById(R.id.action_add_to_fav);
        actionAddToOrderView = rootView.findViewById(R.id.action_add_to_order);
        productGalleryView = rootView.findViewById(R.id.product_gallery);
        categoryNameView = rootView.findViewById(R.id.category_name);
        productNameView = rootView.findViewById(R.id.product_name);
        productComponentsView = rootView.findViewById(R.id.product_components);
        productDescriptionView = rootView.findViewById(R.id.product_description);
        productWeightView = rootView.findViewById(R.id.product_weight);
        productCostView = rootView.findViewById(R.id.product_cost);
        productPickerView = rootView.findViewById(R.id.product_quantity_picker);
        additionsListView = rootView.findViewById(R.id.product_additions_list);

        actionCancelView.setOnClickListener(this);
        actionAddToOrderView.setOnClickListener(this);
        actionAddToFavView.setOnClickListener(this);

        initData();

        setContentView(rootView);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

    }

    public void show(View anchorView) {
        if (isTablet()) {

        } else {
            showAtLocation(anchorView, Gravity.CENTER, 0,0);
        }
    }

    public void initData() {
        if (product != null) {
            categoryNameView.setText(categoryName);
            productNameView.setText(product.getName());
//            productComponentsView.setText(product.getComponents());
            productDescriptionView.setText(product.getDescription());
//            productWeightView.setText(product.getWeight());
            productCostView.setText(String.valueOf(getCost()));
            productPickerView.setQuantity(productQuantity);
        }
    }

    private float getCost() {
        return productQuantity * product.getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_cancel: {

                break;
            }
            case R.id.action_add_to_fav: {

                break;
            }
            case R.id.action_add_to_order: {

                break;
            }
        }
    }

    protected boolean isTablet() {
        return context.getResources().getBoolean(R.bool.isLargeLayout);
    }


}
