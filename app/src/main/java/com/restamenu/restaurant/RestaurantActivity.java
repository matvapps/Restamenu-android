package com.restamenu.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.restamenu.restaurant.adapter.CategoriesAdapter;
import com.restamenu.restaurant.adapter.CategoryClickListener;
import com.restamenu.restaurant.adapter.ContactsAdapter;
import com.restamenu.restaurant.adapter.GalleryAdapter;
import com.restamenu.restaurant.adapter.ItemType;
import com.restamenu.restaurant.adapter.PromotionsAdapter;
import com.restamenu.restaurant.adapter.RestaurantsAdapter;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.NavMenuButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BasePresenterActivity<RestaurantsPresenter, RestaurantView, Restaurant>
        implements RestaurantView, CategoryClickListener, RestaurantsAdapter.ChangeServiceListener, NavMenuButton.OnNavMenuItemClick {

    public static final String KEY_RESTAURANT_ID = "key_rest_id";

    private final String CONTACT_FB = "fb.com";
    private final String CONTACT_INSTAGRAM = "instagram";

    private final String HYPERLINK_PATTERN = "<a href='%s'> %s </a>";

    private final String FACEBOOK_TITLE = "facebook";
    private final String INSTAGRAM_TITLE = "instagram";

    private Integer restaurantId;

    private RestaurantsAdapter adapter;
    private Restaurant restaurant;

    private View favouriteContainer;
    private TextView restaurantTitleView;
    private TextView restaurantTypeView;
    private TextView restaurantAddressView;
    private TextView restaurantPhoneView;
    private TextView restaurantOpeningHours;
    private ImageView restaurantImage;
    private ImageView restaurantBackground;
    private RecyclerView recycler;

    // phone and tablet item
    private GalleryAdapter galleryAdapter;

    // tablet item's
    private RecyclerView categoriesListRecycle;
    private RecyclerView promotionsListRecycle;
    private RecyclerView galleryListRecycle;
    private RecyclerView contactsListRecycle;
    private TextView aboutContentText;
    private ImageView mapImageView;

    private CategoriesAdapter categoriesAdapter;
    private PromotionsAdapter promotionsAdapter;
    private ContactsAdapter contactsAdapter;


    private NavMenuButton toMenuBtn;
    private NavMenuButton toPhotoBtn;
    private NavMenuButton toAboutBtn;
    private NavMenuButton toContactsBtn;


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

        if (!isTablet()) {

            int aboutTitlePos = getResources().getInteger(R.integer.about_title_pos);
            int aboutTextPos = getResources().getInteger(R.integer.about_text_pos);

            //add about
            adapter.change(new AdapterItemType<>("About Restaurant", null, ItemType.TITLE), aboutTitlePos);
            adapter.change(new AdapterItemType<>(data.getInformation(), null, ItemType.ABOUT), aboutTextPos);

            // add map
            int mapPos = getResources().getInteger(R.integer.map_pos);
            adapter.change(new AdapterItemType<Image>(restaurant.getLocation().getImage(), null, ItemType.MAP), mapPos);

            // add order
            int selectServicePos = getResources().getInteger(R.integer.select_service_pos);
            adapter.setSelectedService(data.getServices().get(0));
            adapter.change(new AdapterItemType<>("Select service", data.getServices(), ItemType.ORDER_TYPE_PHONE), selectServicePos);
        } else {

            // add about
            aboutContentText.setText(Html.fromHtml(data.getInformation()));

            //add map
            String backgroundUrl = restaurant.getLocation().getImage();
            Picasso.with(this).load(BuildConfig.BASE_URL +
                    backgroundUrl.substring(1, backgroundUrl.length())).into(mapImageView);


        }

        //load restaurant background
        String backgroundUrl = restaurant.getBackground();
        Picasso.with(this).load(BuildConfig.BASE_URL +
                backgroundUrl.substring(1, backgroundUrl.length())).into(restaurantBackground);


        setContacts(data);

    }

    private void setContacts(@NonNull Restaurant data) {

        ArrayList<Contact> contacts = new ArrayList<>();
        String stringDivider;
        ItemType itemType;

        if (isTablet()) {
            stringDivider = "\n";
            itemType = ItemType.CONTACTS_TABLET;
        } else {
            stringDivider = " ";
            itemType = ItemType.CONTACTS_PHONE;
        }

        // format hours
        StringBuilder openingHours = new StringBuilder();
        for (String item : data.getTiming()) {
            openingHours.append(item).append(stringDivider);
        }

        // format contacts
        StringBuilder socialNetworks = new StringBuilder();
        String socialTitle = "";
        String hyperText = "";
        for (String item : data.getSocial()) {
            if (item.contains(CONTACT_FB)) {
                hyperText = item.substring(item.lastIndexOf("/"));
                hyperText = hyperText.replace("/", "@");
                socialTitle = FACEBOOK_TITLE + ": ";
            } else if (item.contains(CONTACT_INSTAGRAM)) {
                hyperText = item.substring(item.lastIndexOf("/"));
                socialTitle = INSTAGRAM_TITLE + ": ";
            }

            item = Html.fromHtml(String.format(HYPERLINK_PATTERN, item, hyperText)).toString();
            socialNetworks.append(socialTitle).append(item).append(stringDivider);
        }

        // format phones
        StringBuilder phones = new StringBuilder();
        for (String item : data.getPhones()) {
            phones.append(item).append(stringDivider);
        }

        contacts.add(new Contact("Opening Hours", openingHours.toString()));
        contacts.add(new Contact("Phones", phones.toString()));

        if (isTablet()) {
            contacts.add(new Contact("Social Networks", socialNetworks.toString()));
            contactsAdapter.setItems(contacts);
        } else {
            contacts.add(new Contact("Address", data.getAddress()));

            int contactTitlePos = getResources().getInteger(R.integer.contacts_title_pos);
            int contactListPos = getResources().getInteger(R.integer.contacts_list_pos);

            adapter.change(new AdapterItemType<>("Contacts", null, ItemType.TITLE), contactTitlePos);
            adapter.change(new AdapterItemType<>(null, contacts, itemType), contactListPos);
        }
    }

    @Override
    public void setCategories(@NonNull List<Category> categories) {
        Logger.log("Categories: " + categories.toString());

        if (!isTablet()) {
            int menuSectionListPos = getResources().getInteger(R.integer.menu_sections_list_pos);
            adapter.change(new AdapterItemType<>(restaurant.getName(), categories, ItemType.MENU_PHONE), menuSectionListPos);
        } else {
            categoriesAdapter.setItems(categories);
        }
    }

    @Override
    public void setGallery(@NonNull List<Image> images) {
        Logger.log("Gallery: " + images.toString());
        galleryAdapter.setItems(images);
    }

    @Override
    public void setPromotions(@NonNull List<Promotion> promotions) {
        Logger.log("Promotions: " + promotions.toString());

        if (isTablet()) {
            promotionsAdapter.setItems(promotions);
        } else {
            int promotionsTitlePos = getResources().getInteger(R.integer.restaurant_promotions_title_pos);
            int promotionsListPos = getResources().getInteger(R.integer.restaurant_promotions_list_pos);

            adapter.change(new AdapterItemType<>("Restaurant promotions", null, ItemType.TITLE), promotionsTitlePos);
            adapter.change(new AdapterItemType<>(null, promotions, ItemType.PROMOTIONS), promotionsListPos);
        }

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

    private void initNavigationMenu() {
        toMenuBtn = findViewById(R.id.nav_btn_menu);
        toPhotoBtn = findViewById(R.id.nav_btn_photo);
        toAboutBtn = findViewById(R.id.nav_btn_about);
        toContactsBtn = findViewById(R.id.nav_btn_contacts);


        toMenuBtn.setNavMenuItemClick(this);
        toPhotoBtn.setNavMenuItemClick(this);
        toAboutBtn.setNavMenuItemClick(this);
        toContactsBtn.setNavMenuItemClick(this);
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
        recycler = findViewById(R.id.recycler);
        galleryListRecycle = findViewById(R.id.item_recycler_gallery_list);


        galleryListRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        galleryAdapter = new GalleryAdapter();
        galleryListRecycle.setAdapter(galleryAdapter);


        if (isTablet()) {

            categoriesListRecycle = findViewById(R.id.item_recycler_categories_list);
            promotionsListRecycle = findViewById(R.id.item_recycler_promotions_list);
            contactsListRecycle = findViewById(R.id.item_recycler_contacts_list);
            aboutContentText = findViewById(R.id.about_text_content);
            mapImageView = findViewById(R.id.map_image_view);

            categoriesListRecycle.setHasFixedSize(true);
            categoriesListRecycle.setLayoutManager(new GridLayoutManager(RestaurantActivity.this, 3));

            promotionsListRecycle.setHasFixedSize(true);
            promotionsListRecycle.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this, LinearLayoutManager.HORIZONTAL, false));

            contactsListRecycle.setHasFixedSize(true);
            contactsListRecycle.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this, LinearLayoutManager.HORIZONTAL, false));


            categoriesAdapter = new CategoriesAdapter();
            promotionsAdapter = new PromotionsAdapter();
            contactsAdapter = new ContactsAdapter();


            categoriesListRecycle.setAdapter(categoriesAdapter);
            promotionsListRecycle.setAdapter(promotionsAdapter);
            contactsListRecycle.setAdapter(contactsAdapter);

            galleryAdapter.setUseScrollIt(true);


            initNavigationMenu();
        } else {
            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            int restaurantContentListSize = getResources().getInteger(R.integer.restaurant_content_list_size);
            // push to adapter fake data
            adapter = new RestaurantsAdapter(this);
            for (int i = 0; i < restaurantContentListSize; i++) {
                adapter.add(new AdapterItemType<>("", null, ItemType.TITLE));
            }
            recycler.setAdapter(adapter);
        }


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

    @Override
    public void onNavMenuItemClick(String title) {

        switch (title) {
            case "Menu": {
                toMenuBtn.setMenuItemSelected(true);
                toPhotoBtn.setMenuItemSelected(false);
                toAboutBtn.setMenuItemSelected(false);
                toContactsBtn.setMenuItemSelected(false);

                break;
            }
            case "Photo": {
                toMenuBtn.setMenuItemSelected(false);
                toPhotoBtn.setMenuItemSelected(true);
                toAboutBtn.setMenuItemSelected(false);
                toContactsBtn.setMenuItemSelected(false);

                recycler.setNestedScrollingEnabled(true);
                recycler.smoothScrollToPosition(4);


                break;
            }
            case "About": {
                toMenuBtn.setMenuItemSelected(false);
                toPhotoBtn.setMenuItemSelected(false);
                toAboutBtn.setMenuItemSelected(true);
                toContactsBtn.setMenuItemSelected(false);


                break;
            }
            case "Contacts": {
                toMenuBtn.setMenuItemSelected(false);
                toPhotoBtn.setMenuItemSelected(false);
                toAboutBtn.setMenuItemSelected(false);
                toContactsBtn.setMenuItemSelected(true);
                break;
            }
        }
    }

}
