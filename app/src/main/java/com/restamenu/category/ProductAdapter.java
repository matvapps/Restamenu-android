package com.restamenu.category;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> items;

    ProductAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<Product> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<Product> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_product_item, parent, false);

        return new ProductViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ProductAdapter.ProductViewHolder holder, int position) {
        Product product = items.get(position);

        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());

        int priceOriginal = product.getPriceOriginal();
        int price = product.getPrice();

        if (priceOriginal == price) {
            holder.productPrice.setText(String.format("%d", product.getPrice()));
            holder.productPriceSub.setVisibility(View.INVISIBLE);
        } else {
            holder.productPrice.setText(String.format("%d", product.getPrice()));
            holder.productPriceSub.setText(String.format("%d", product.getPriceOriginal()));
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private View rootView;
        private ImageView productImage;
        private TextView productName;
        private TextView productDescription;
        private TextView productMass;
        private TextView productPrice;
        private TextView productPriceSub;
        private EditText productQuantity;
        private View buyContainerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productMass = itemView.findViewById(R.id.product_mass);
            productPrice = itemView.findViewById(R.id.product_price);
            productPriceSub = itemView.findViewById(R.id.product_price_sub);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            buyContainerView = itemView.findViewById(R.id.buy_container);

        }


    }

}
