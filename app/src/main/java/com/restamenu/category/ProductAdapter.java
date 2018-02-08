package com.restamenu.category;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.model.content.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by Alexandr
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> items;
    private List<Product> defaultList;

    ProductAdapter() {
        items = new ArrayList<>();
        defaultList = new ArrayList<>();
    }

    public void addItems(List<Product> data) {
        defaultList.addAll(data);
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<Product> data) {
        items.clear();
        defaultList.clear();

        defaultList.addAll(data);
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void updateItems(List<Product> data) {
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
            holder.productPriceSub.setVisibility(View.INVISIBLE);
        } else {
            holder.productPriceSub.setText(String.format("%s%d", "$", product.getPriceOriginal()));
        }

        //for (int i = 0; i < items.get(position).getSpecial().size(); i++)
        //    Logger.log(items.get(position).getSpecial().get(i));

        SpecialsAdapter specialsAdapter = new SpecialsAdapter();
        holder.serviceList.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), HORIZONTAL, false));
        holder.serviceList.setAdapter(specialsAdapter);


        String imageUrl = product.getImages().get(0);
        if (!imageUrl.equals("")) {
            String path = BuildConfig.BASE_URL + imageUrl.substring(1, imageUrl.length()) + BuildConfig.IMAGE_WIDTH_400;
            Picasso.with(holder.itemView.getContext()).load(path).into(holder.productImage);
        }
        specialsAdapter.setItems(items.get(position).getSpecial());


        holder.productPrice.setText(String.format("%s%d", "$", product.getPrice()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Product getItem(int position) {
        return items.get(position);
    }

    public void findProductBy(String characters) {
        List<Product> result = new ArrayList<>();

        for (int i = 0; i < defaultList.size(); i++) {
            Product product = defaultList.get(i);

            if (product.getName().toLowerCase()
                    .contains(characters.toLowerCase())) {

                result.add(product);

            }
        }

        updateItems(result);
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private TextView productDescription;
        //        private TextView productMass;
        private TextView productPrice;
        private TextView productPriceSub;
        private RecyclerView serviceList;
        private EditText productQuantity;
        private View buyContainerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
//            productMass = itemView.findViewById(R.id.product_mass);
            serviceList = itemView.findViewById(R.id.service_list);
            productPrice = itemView.findViewById(R.id.product_price);
            productPriceSub = itemView.findViewById(R.id.product_price_sub);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            buyContainerView = itemView.findViewById(R.id.buy_container);

        }


    }

}
