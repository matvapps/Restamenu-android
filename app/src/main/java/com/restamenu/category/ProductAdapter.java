package com.restamenu.category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.model.content.Product;

import java.util.ArrayList;
import java.util.List;

import carbon.widget.FrameLayout;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by Alexandr
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> items;
    private List<Product> defaultList;
    private String currencyIcon = "$";

    private final String pricePattern = "%d %s";

    private final int imageWidthPixels = 512;
    private final int imageHeightPixels = 384;

    ProductAdapter() {
        items = new ArrayList<>();
        defaultList = new ArrayList<>();
    }

    public List<Product> getItems() {
        return items;
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

//        TODO: Uncomment
//        holder.productDescription.setText(product.getDescription());

        int priceOriginal = product.getPriceOriginal();
        int price = product.getPrice();
        int priceOld = product.getPrice_old();

//        if (holder.rootCardView != null) {
//            holder.rootCardView.setElevation(30f);
//            holder.rootCardView.setTranslationZ(30f);
//        }

        holder.productQuantityContainer.setOnClickListener(view -> {
            holder.productQuantity.requestFocus();
            holder.productQuantity.setText("");
        });

        holder.productPrice.setText(String.format(pricePattern, price, currencyIcon));

        if (!currencyIcon.equals("AED") || !isTablet(holder.itemView.getContext()))
            holder.productPrice.setText(String.format(pricePattern, price, currencyIcon));
        else {
            int priceSize = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.land_category_product_price_text_size);
            int priceLessSize = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.land_category_product_price_text_less_size);

            SpannableString spanPrice = new SpannableString(String.valueOf(price));
            spanPrice.setSpan(new AbsoluteSizeSpan(priceSize), 0, String.valueOf(price).length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            SpannableString spanCurrency = new SpannableString("AED");
            spanCurrency.setSpan(new AbsoluteSizeSpan(priceLessSize), 0, "AED".length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            CharSequence finalText = TextUtils.concat(spanPrice, " ", spanCurrency);

            holder.productPrice.setText(finalText);
        }


        if (priceOld != 0) {
            holder.productPriceSub.setText(String.format(pricePattern, priceOld, currencyIcon));
            holder.productPriceSub.setPaintFlags(holder.productPriceSub.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (!currencyIcon.equals("$")) {
            holder.productPriceSub.setText(String.format(pricePattern, priceOriginal, "$"));
        }


        SpecialsAdapter specialsAdapter = new SpecialsAdapter();
        holder.serviceList.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), HORIZONTAL, false));
        holder.serviceList.setAdapter(specialsAdapter);


        String imageUrl = product.getImages().get(0);
        if (!imageUrl.equals("")) {
            String path = BuildConfig.BASE_URL + imageUrl.substring(1, imageUrl.length()) + BuildConfig.IMAGE_WIDTH_400;
            Glide
                    .with(holder.itemView.getContext())
                    .load(path)
                    .apply(new RequestOptions()
                            .placeholder(R.color.greyish)
                            .override(imageWidthPixels, imageHeightPixels))
                    .into(holder.productImage);
        }
        specialsAdapter.setItems(items.get(position).getSpecial());

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

    public String getCurrencyIcon() {
        return currencyIcon;
    }

    public void setCurrencyIcon(String currencyIcon) {
        this.currencyIcon = currencyIcon;
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {

        private View btnNext;
        private View btnPrev;

        private TextView nextCategory;
        private TextView prevCategory;

        public FooterViewHolder(View itemView) {
            super(itemView);

            btnNext = itemView.findViewById(R.id.next_category_button);
            btnPrev = itemView.findViewById(R.id.previous_category_button);
            nextCategory = itemView.findViewById(R.id.next_category_title);
            prevCategory = itemView.findViewById(R.id.previous_category_title);

        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView productImage;
        private TextView productName;
        private TextView productDescription;
        private FrameLayout rootCardView;
        //        private TextView productMass;
        private TextView productPrice;
        private TextView productPriceSub;
        private RecyclerView serviceList;
        private EditText productQuantity;
        private View buyContainerView;
        private View productQuantityContainer;

        public ProductViewHolder(View itemView) {
            super(itemView);

            rootCardView = itemView.findViewById(R.id.root_cardview);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productQuantityContainer = itemView.findViewById(R.id.product_quantity_container);
//            productMass = itemView.findViewById(R.id.product_mass);
            serviceList = itemView.findViewById(R.id.service_list);
            productPrice = itemView.findViewById(R.id.product_price);
            productPriceSub = itemView.findViewById(R.id.product_price_sub);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            buyContainerView = itemView.findViewById(R.id.buy_container);

        }


    }

    private boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isLargeLayout);
    }

}
