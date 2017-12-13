package com.restamenu.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Contact;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Promotion;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.adapter.AdapterItemType;
import com.restamenu.restaurant.adapter.CategoryClickListener;
import com.restamenu.restaurant.adapter.GalleryAdapter;
import com.restamenu.restaurant.adapter.ItemType;
import com.restamenu.restaurant.adapter.RestaurantsAdapter;
import com.restamenu.util.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BasePresenterActivity<RestaurantsPresenter, RestaurantView, Restaurant>
        implements RestaurantView, CategoryClickListener, RestaurantsAdapter.ChangeServiceListener {

    public static final String KEY_RESTAURANT_ID = "key_rest_id";
    private Integer restaurantId;

    private RestaurantsAdapter adapter;
    private GalleryAdapter galleryAdapter;
    private Restaurant restaurant;

    private View favouriteContainer;
    private TextView restaurantTitleView;
    private TextView restaurantTypeView;
    private TextView restaurantAddressView;
    private TextView restaurantPhoneView;
    private TextView restaurantOpeningHours;
    private ImageView restaurantImage;
    private ImageView restaurantBackground;
    private ImageView mapImage;
    private RecyclerView recycler;
    private RecyclerView galleryRecycler;
    private TextView toolbarRestaurantTitle;
    //private Spinner orderTypeSpinner;


    public static void start(@NonNull Activity activity, @NonNull Integer restaurantId) {
        Intent intent = new Intent(activity, RestaurantActivity.class);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restaurantId = getIntent().getIntExtra(KEY_RESTAURANT_ID, 1);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setData(@NonNull Restaurant data) {
        Logger.log("Rest: " + data.toString());

        restaurant = data;
        restaurantTitleView.setText(data.getName());
        restaurantAddressView.setText(data.getAddress());
        restaurantPhoneView.setText(data.getPhones().get(0));
        restaurantOpeningHours.setText(data.getTiming().get(0));

        int aboutTitlePos = getResources().getInteger(R.integer.about_title_pos);
        int aboutTextPos = getResources().getInteger(R.integer.about_text_pos);

        adapter.change(new AdapterItemType<>("About Restaurant", null, ItemType.TITLE), aboutTitlePos);
        adapter.change(new AdapterItemType<>(data.getInformation(), null, ItemType.ABOUT), aboutTextPos);

        setContacts(data);

        //load restaurant background
        String backgroundUrl = restaurant.getBackground();
        Picasso.with(this).load(BuildConfig.BASE_URL +
                backgroundUrl.substring(1, backgroundUrl.length())).into(restaurantBackground);

        // add map
//        if (!isTablet()) {
            int mapPos = getResources().getInteger(R.integer.map_pos);
            adapter.change(new AdapterItemType<Image>(restaurant.getLocation().getImage(), null, ItemType.MAP), mapPos);
//        } //else {
//            String mapBackgroundUrl = data.getLocation().getImage().substring(1, data.getLocation().getImage().length());
//            Picasso.with(RestaurantActivity.this).load(BuildConfig.BASE_URL + mapBackgroundUrl).into(mapImage);
//        }

        ItemType itemType;
        int selectServicePos = getResources().getInteger(R.integer.select_service_pos);
        if (isTablet()) {
            itemType = ItemType.ORDER_TYPE_TABLET;
        } else {
            itemType = ItemType.ORDER_TYPE_PHONE;
        }
        adapter.setSelectedService(data.getServices().get(0));
        adapter.change(new AdapterItemType<>("Select service", data.getServices(), itemType), selectServicePos);
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
            stringDivider = " ";
            itemType = ItemType.CONTACTS_PHONE;
        }

        StringBuilder openingHours = new StringBuilder();
        for (String item :data.getTiming()) {
            openingHours.append(item).append(stringDivider);
        }

        StringBuilder socialNetworks = new StringBuilder();
        for (String item :data.getSocial())
            socialNetworks.append(item).append(stringDivider);

        StringBuilder phones = new StringBuilder();
        for (String item :data.getPhones()) {
            phones.append(item).append(stringDivider);
        }

        contacts.add(new Contact("Opening Hours", openingHours.toString()));
        contacts.add(new Contact("Phones", phones.toString()));

        if (isTablet()) {
            contacts.add(new Contact("Social Networks", socialNetworks.toString()));
        } else {
            contacts.add(new Contact("Address", data.getAddress()));
        }

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
            adapter.change(new AdapterItemType<>("Menu Sections", null, ItemType.TITLE), menuSectionTitlePos);
            itemType = ItemType.MENU_TABLET;
        } else {
            itemType = ItemType.MENU_PHONE;
        }
        adapter.change(new AdapterItemType<>(restaurant.getName(), categories, itemType), menuSectionListPos);
    }

    @Override
    public void setGallery(@NonNull List<Image> images) {
        Logger.log("Gallery: " + images.toString());

        if (isTablet()) {

            int galleryTitlePos = getResources().getInteger(R.integer.gallery_title_pos);
            int galleryListPos = getResources().getInteger(R.integer.gallery_list_pos);

            adapter.change(new AdapterItemType<>("Gallery of Restaurant", null, ItemType.TITLE), galleryTitlePos);
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
    public void setInstitutions(@NonNull List<Institute> institutions) {
        StringBuilder instituteText = new StringBuilder();
        for (int i = 0; i < restaurant.getInstitutes().size(); i++) {
            if (i < restaurant.getInstitutes().size() - 1)
                instituteText.append(getInstituteName(institutions, restaurant.getInstitutes().get(i))).append(" & ");
            else
                instituteText.append(getInstituteName(institutions, restaurant.getInstitutes().get(i))).append("");
        }

        restaurantTypeView.setText(instituteText.toString());
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

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;

        presenter.loadData(width);

    }

    @Override
    protected void initViews() {

        restaurantTitleView = findViewById(R.id.restaurant_title);
        restaurantTypeView = findViewById(R.id.restaurant_type);
        restaurantAddressView = findViewById(R.id.restaurant_address);
        restaurantPhoneView = findViewById(R.id.restaurant_phone);
        restaurantOpeningHours = findViewById(R.id.restaurant_opening_hours);
        favouriteContainer = findViewById(R.id.restaurant_favourite_container);
        restaurantImage = findViewById(R.id.restaurant_image);
        restaurantBackground = findViewById(R.id.restaurant_background);
        mapImage = findViewById(R.id.map_image);


        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (!isTablet()) {
            galleryRecycler = findViewById(R.id.gallery_recycler);
            galleryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            galleryAdapter = new GalleryAdapter();
            galleryRecycler.setAdapter(galleryAdapter);
        }

        int restaurantContentListSize = getResources().getInteger(R.integer.restaurant_content_list_size);

        adapter = new RestaurantsAdapter(this);
        for (int i = 0; i < restaurantContentListSize; i++) {
            adapter.add(new AdapterItemType<>("", null, ItemType.TITLE));
        }

        recycler.setAdapter(adapter);

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

    @Override
    public void onCategoryClicked(int categoryId) {
        // TODO: start CategoryActivity
        //CategoryActivity.start(this, restaurantId,1,  categoryId);
    }

    private String getInstituteName(List<Institute> instituteList, int instituteId) {
        for (int i = 0; i < instituteList.size(); i++) {
            if (instituteList.get(i).getId() == instituteId)
                return instituteList.get(i).getName();
        }
        return "";
    }

    @Override
    public void onServiceChanged(int serviceId) {
        presenter.changeCategories(serviceId);
    }
}
