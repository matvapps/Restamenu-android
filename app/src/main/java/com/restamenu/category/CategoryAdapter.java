package com.restamenu.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;
import com.restamenu.model.content.Product;
import com.restamenu.restaurant.adapter.AdapterItemType;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.OnVerticalScrollListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alexandr.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<AdapterItemType> items;
    private OnNavigationButtonClickListener listener;
    private List<Category> categories;
    private int categoryIndex = 0;
    private Context context;
    private String keyword = "";
    private Language currentLanguage = new Language();
    private Currency currentCurrency = new Currency();


    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
        notifyDataSetChanged();
    }

    public Currency getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(Currency currentCurrency) {
        this.currentCurrency = currentCurrency;
        notifyDataSetChanged();
    }



    public CategoryAdapter(OnNavigationButtonClickListener listener) {
        this.listener = listener;
        categories = new ArrayList<>();
        items = new ArrayList<>();
    }

    public void addItems(List<AdapterItemType> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<AdapterItemType> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void add(AdapterItemType item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void change(AdapterItemType newItem, int position) {
        if (position < 0 || position > items.size()) {
            Logger.log("Position not valid: pos = " + position);
            return;
        }
        items.set(position, newItem);
        notifyItemChanged(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        context = parent.getContext();

        switch (viewType) {
            case 22:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_category_header, parent, false);
                return new HeaderViewHolder(rootView);
            case 23:
            case 25:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_category_layout, parent, false);
                return new ListViewHolder(rootView);
            case 24:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_category_footer, parent, false);
                return new FooterViewHolder(rootView);

            default:
                return null;
        }
    }


    private AdapterItemType<List<Product>> getItem() {
        return items.get(1);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AdapterItemType item = items.get(position);
        switch (holder.getItemViewType()) {
            //Header
            case 22: {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

                headerViewHolder.btnNext.setOnClickListener(this);
                headerViewHolder.btnPrevious.setOnClickListener(this);

                CategoryTitleListAdapter categoryTitleListAdapter = new CategoryTitleListAdapter();
                categoryTitleListAdapter.setItems(categories);


                headerViewHolder.categoryTitleList.setOrientation(Orientation.HORIZONTAL);
                headerViewHolder.categoryTitleList.setSlideOnFling(false);
                headerViewHolder.categoryTitleList.setItemTransitionTimeMillis(430);
                headerViewHolder.categoryTitleList.setAdapter(categoryTitleListAdapter);
                headerViewHolder.categoryTitleList.scrollToPosition(categoryIndex);

                headerViewHolder.categoryTitleList.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
                    @Override
                    public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

                    }

                    @Override
                    public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
                        if (categoryIndex > adapterPosition) {
                            listener.onPreviousCategory();
                        } else if (categoryIndex < adapterPosition) {
                            listener.onNextCategory();
                        }
                    }

                    @Override
                    public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {

                    }
                });


                headerViewHolder.buttonFind.setOnClickListener(view -> {
                    keyword = headerViewHolder.findEditText.getText().toString();
                    listener.onPerformSearch(keyword, getItem().getData());
                    keyword = "";
                });


                break;
            }
            //Tablet Product Menu
            case 23: {
                AdapterItemType<List<Product>> productsItemType = (AdapterItemType<List<Product>>) item;

                ListViewHolder listViewHolder = (ListViewHolder) holder;

                listViewHolder.recycler.setLayoutManager(new GridLayoutManager(listViewHolder.itemView.getContext(), 4));

                ProductAdapter productAdapter = new ProductAdapter();

                productAdapter.setCurrencyIcon(currentCurrency.getSymbol());

                listViewHolder.recycler.setAdapter(productAdapter);
                listViewHolder.recycler.setNestedScrollingEnabled(false);

                listViewHolder.recycler.addOnScrollListener(new OnVerticalScrollListener() {
                    @Override
                    public void onScrolledDown(int dy) {
                        super.onScrolledUp(dy);
                        listener.onSwipeRefreshEnabled(false);
                    }

                    @Override
                    public void onScrolledToTop() {
                        super.onScrolledToTop();
                        listener.onSwipeRefreshEnabled(true);
                    }
                });

                if (productsItemType.getData() != null) {
                    productAdapter.setItems(productsItemType.getData());
                    productAdapter.findProductBy(keyword);
                }

                 break;
            }
            // Phone Product menu
            case 25: {

                Logger.log("onBindViewHolder phone product menu");
                AdapterItemType<List<Product>> productsItemType = (AdapterItemType<List<Product>>) item;

                ListViewHolder listViewHolder = (ListViewHolder) holder;

                listViewHolder.recycler.setLayoutManager(new GridLayoutManager(listViewHolder.itemView.getContext(), 1));
                listViewHolder.recycler.setNestedScrollingEnabled(false);

                ProductAdapter productAdapter = new ProductAdapter();
                productAdapter.setCurrencyIcon(currentCurrency.getSymbol());
                listViewHolder.recycler.setAdapter(productAdapter);

                if (productsItemType.getData() != null)
                    productAdapter.setItems(productsItemType.getData());
                break;
            }
            //Footer
            case 24: {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

                footerViewHolder.btnNext.setOnClickListener(this);
                footerViewHolder.btnPrevious.setOnClickListener(this);

                footerViewHolder.nextCategoryTitle.setText(getNextCategory().getName());
                footerViewHolder.previousCategoryTitle.setText(getPreviousCategory().getName());

                break;
            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_arrow_right:
            case R.id.next_category_button:
                listener.onNextCategory();
                break;
            case R.id.category_arrow_left:
            case R.id.previous_category_button:
                listener.onPreviousCategory();
                break;

        }
    }

    public Category getNextCategory() {
        int newCategoryIndex = (categoryIndex + 1) % categories.size();
        return categories.get(newCategoryIndex);
    }

    public Category getPreviousCategory() {
        int newCategoryIndex = (categoryIndex - 1) % categories.size();
        if (newCategoryIndex <  0)
            newCategoryIndex = categories.size() - 1;

        return categories.get(newCategoryIndex);
    }


    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }


    public interface OnNavigationButtonClickListener {
        void onNextCategory();
        void onPreviousCategory();
        void onPerformSearch(String keyword, List<Product> products);
        void onSwipeRefreshEnabled(boolean enabled);
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType().getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        View categoryHeaderBackgound;
        View btnNext;
        View btnPrevious;
        DiscreteScrollView categoryTitleList;
        EditText findEditText;
        View buttonFind;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            categoryHeaderBackgound = itemView.findViewById(R.id.category_header_background);
            btnNext = itemView.findViewById(R.id.category_arrow_right);
            btnPrevious = itemView.findViewById(R.id.category_arrow_left);
            categoryTitleList = itemView.findViewById(R.id.category_title_list);
            buttonFind = itemView.findViewById(R.id.button_find);
            findEditText = itemView.findViewById(R.id.search_edit_text);
            findEditText.clearFocus();


        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView nextCategoryTitle;
        TextView previousCategoryTitle;
        View btnNext;
        View btnPrevious;

        public FooterViewHolder(View itemView) {
            super(itemView);

            nextCategoryTitle = itemView.findViewById(R.id.next_category_title);
            previousCategoryTitle = itemView.findViewById(R.id.previous_category_title);
            btnNext = itemView.findViewById(R.id.next_category_button);
            btnPrevious = itemView.findViewById(R.id.previous_category_button);

        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recycler;

        public ListViewHolder(View itemView) {
            super(itemView);
            recycler = itemView.findViewById(R.id.recycler);
        }
    }
}
