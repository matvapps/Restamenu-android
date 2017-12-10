package com.restamenu.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Contact;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Promotion;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.adapter.AdapterItemType;
import com.restamenu.restaurant.adapter.GalleryAdapter;
import com.restamenu.restaurant.adapter.ItemType;
import com.restamenu.restaurant.adapter.OrderTypeSpinnerAdapter;
import com.restamenu.restaurant.adapter.RestaurantsAdapter;
import com.restamenu.restaurant.adapter.ServiceType;
import com.restamenu.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BasePresenterActivity<RestaurantsPresenter, RestaurantView, Restaurant> implements RestaurantView {

    public static final String KEY_RESTAURANT_ID = "key_rest_id";
    private Integer restaurantId;

    private RestaurantsAdapter adapter;
    private List<AdapterItemType> items;
    private GalleryAdapter galleryAdapter;
    private OrderTypeSpinnerAdapter orderTypeSpinnerAdapter;

    private View favouriteContainer;
    private TextView restaurantTitleView;
    private TextView restaurantTypeView;
    private TextView restaurantAddressView;
    private TextView restaurantPhoneView;
    private TextView restaurantOpeningHours;
    private RecyclerView recycler;
    private RecyclerView galleryRecycler;
    private Spinner orderTypeSpinner;


    public static void start(@NonNull Activity activity, @NonNull Integer restaurantId) {
        Intent intent = new Intent(activity, RestaurantActivity.class);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restaurantId = getIntent().getIntExtra(KEY_RESTAURANT_ID, 1);
        super.onCreate(savedInstanceState);

        //

    }

    @Override
    public void setData(@NonNull Restaurant data) {
        Logger.log("Rest: " + data.toString());

        restaurantTitleView.setText(data.getName());

        ////        restaurantTypeView;
        restaurantAddressView.setText(data.getAddress());
        restaurantPhoneView.setText(data.getPhones().get(0));
        restaurantOpeningHours.setText(data.getTiming().get(0));

        int aboutTitlePos = getResources().getInteger(R.integer.about_title_pos);
        int aboutTextPos = getResources().getInteger(R.integer.about_text_pos);

        adapter.change(new AdapterItemType("About Restaurant", null, ItemType.TITLE), aboutTitlePos);
        adapter.change(new AdapterItemType(data.getInformation(), null, ItemType.ABOUT), aboutTextPos);

        setContacts(data);
//
        if (isTablet()) {

        } else {
            ArrayList<ServiceType> serviceTypes = new ArrayList<>();
            serviceTypes.add(ServiceType.DELIVERY);
            serviceTypes.add(ServiceType.TAKEAWAY);
            serviceTypes.add(ServiceType.RESTAURANT);

            orderTypeSpinnerAdapter =
                    new OrderTypeSpinnerAdapter(RestaurantActivity.this,
                            serviceTypes, data.getServices());

            orderTypeSpinner.setAdapter(orderTypeSpinnerAdapter);
        }
    }

    private void setContacts(@NonNull Restaurant data) {

        ArrayList<Contact> contacts = new ArrayList<>();
        String stringDivider;
        ItemType itemType;

        if (isTablet()) {
            stringDivider = "\n";
            itemType = ItemType.CONTACTS_TABLET;
        }
        else {
            stringDivider = ", ";
            itemType = ItemType.CONTACTS_PHONE;
        }

        StringBuilder openingHours = new StringBuilder();
        for (String item :data.getTiming()) {
            openingHours.append(item).append(stringDivider);
        }

        StringBuilder phones = new StringBuilder();
        for (String item :data.getPhones()) {
            phones.append(item).append(stringDivider);
        }


        contacts.add(new Contact("Opening hours", openingHours.toString()));
        contacts.add(new Contact("Phone", phones.toString()));
        contacts.add(new Contact("Address", data.getAddress()));

        int contactTitlePos = getResources().getInteger(R.integer.contacts_title_pos);
        int contactListPos = getResources().getInteger(R.integer.contacts_list_pos);

        adapter.change(new AdapterItemType<>("Contacts", null, ItemType.TITLE), contactTitlePos);
        adapter.change(new AdapterItemType<>(null, contacts, itemType), contactListPos);
    }

    @Override
    public void setCategories(@NonNull List<Category> categories) {
        Logger.log("Categories: " + categories.toString());
        ItemType itemType;

        int menuSectionTitlePos = getResources().getInteger(R.integer.menu_sections_title_pos);
        int menuSectionListPos = getResources().getInteger(R.integer.menu_sections_list_pos);

        if (isTablet()) {
            adapter.change(new AdapterItemType("Menu Sections", null, ItemType.TITLE), menuSectionTitlePos);
            itemType = ItemType.MENU_TABLET;
        } else {
            itemType = ItemType.MENU_PHONE;
        }
        adapter.change(new AdapterItemType<>(null, categories, itemType), menuSectionListPos);
    }

    @Override
    public void setGallery(@NonNull List<Image> images) {
        Logger.log("Gallery: " + images.toString());

        if (isTablet()) {

            int galleryTitlePos = getResources().getInteger(R.integer.gallery_title_pos);
            int galleryListPos = getResources().getInteger(R.integer.gallery_list_pos);

            adapter.change(new AdapterItemType<>("Gallery", null, ItemType.TITLE), galleryTitlePos);
            adapter.change(new AdapterItemType<>(null, images, ItemType.GALLERY), galleryListPos);
        } else {
            galleryAdapter.setItems(images);
        }

    }

    @Override
    public void setPromotions(@NonNull List<Promotion> promotions) {
        Logger.log("Promotions: " + promotions.toString());

        int promotionsTitlePos = getResources().getInteger(R.integer.restaurant_promotions_title_pos);
        int promotionsListPos = getResources().getInteger(R.integer.restaurant_promotions_list_pos);

        adapter.change(new AdapterItemType<>("Restaurant promotions", null, ItemType.TITLE), promotionsTitlePos);
        adapter.change(new AdapterItemType<>(null, promotions, ItemType.PROMOTIONS), promotionsListPos);

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
        if (presenter == null) {
            presenter = new RestaurantsPresenter(restaurantId);
        }
        presenter.attachView(this);
        presenter.loadData();

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_restaurant);

        restaurantTitleView = findViewById(R.id.restaurant_title);
        restaurantTypeView = findViewById(R.id.restaurant_type);
        restaurantAddressView = findViewById(R.id.restaurant_address);
        restaurantPhoneView = findViewById(R.id.restaurant_phone);
        restaurantOpeningHours = findViewById(R.id.restaurant_opening_hours);
        favouriteContainer = findViewById(R.id.restaurant_favourite_container);
        orderTypeSpinner = findViewById(R.id.order_type_spinner);

        //TODO
//        favouriteContainer.setVisibility(View.INVISIBLE);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (isTablet()) {

        } else {
            galleryRecycler = findViewById(R.id.gallery_recycler);
            galleryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            galleryAdapter = new GalleryAdapter();
            galleryRecycler.setAdapter(galleryAdapter);
        }

        int restaurantContentListSize = getResources().getInteger(R.integer.restaurant_content_list_size);

        adapter = new RestaurantsAdapter();
        for (int i = 0; i < restaurantContentListSize; i++) {
            adapter.add(new AdapterItemType("", null, ItemType.TITLE));
        }

        recycler.setAdapter(adapter);

        int mapPos = getResources().getInteger(R.integer.map_pos);
        adapter.change(new AdapterItemType<Image>(null, null, ItemType.MAP), mapPos);

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_restaurant;
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isLargeLayout);
    }

}
