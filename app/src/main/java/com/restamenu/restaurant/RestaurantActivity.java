package com.restamenu.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.category.CategoryActivity;
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
import com.restamenu.util.ListUtils;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.ServiceButton;
import com.restamenu.views.custom.StickyScrollView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BasePresenterActivity<RestaurantsPresenter, RestaurantView, Restaurant>
        implements RestaurantView, CategoryClickListener, RestaurantsAdapter.ChangeServiceListener, CompoundButton.OnCheckedChangeListener {

    public static final String KEY_RESTAURANT_ID = "key_rest_id";

    private final String CONTACT_FB = "fb.com";
    private final String CONTACT_INSTAGRAM = "instagram";

    private final String HYPERLINK_PATTERN = "<a href='%s'> %s </a>";

    private final String FACEBOOK_TITLE = "facebook";
    private final String INSTAGRAM_TITLE = "instagram";

    private Integer restaurantId;

    //Only for tablet
    private int selectedService;

    private RestaurantsAdapter adapter;
    private Restaurant restaurant;

    private StickyScrollView scrollView;

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
    private DiscreteScrollView galleryList;
    private RecyclerView contactsListRecycle;
    private TextView aboutContentText;
    private ImageView mapImageView;

    private CategoriesAdapter categoriesAdapter;
    private PromotionsAdapter promotionsAdapter;
    private ContactsAdapter contactsAdapter;


    private RadioGroup navigationTree;
    private RadioButton toMenuBtn;
    private RadioButton toPromotionsBtn;
    private RadioButton toPhotoBtn;
    private RadioButton toAboutBtn;
    private RadioButton toContactsBtn;

    private ServiceButton serviceAtRestaurant;
    private ServiceButton serviceTakeAway;
    private ServiceButton serviceDelivery;

    private Rect scrollBounds = new Rect();
    private int checkedTreeItem;


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
    protected boolean showToolbarBackStack() {
        return true;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_restaurant;
    }

    @Override
    protected int getToolbarLayoutId() {
        return R.layout.include_toolbar_restaurant;
    }

    @Override
    protected void initViews() {
        scrollBounds = new Rect();

        galleryList = findViewById(R.id.recycler_gallery);
        restaurantTitleView = findViewById(R.id.restaurant_title);
        restaurantTypeView = findViewById(R.id.restaurant_type);
        restaurantAddressView = findViewById(R.id.restaurant_address);
        restaurantPhoneView = findViewById(R.id.restaurant_phone);
        restaurantOpeningHours = findViewById(R.id.restaurant_opening_hours);
        favouriteContainer = findViewById(R.id.restaurant_favourite_container);
        restaurantImage = findViewById(R.id.restaurant_image);
        restaurantBackground = findViewById(R.id.restaurant_background);
        recycler = findViewById(R.id.recycler);


        galleryAdapter = new GalleryAdapter();


        if (isTablet()) {
            scrollView = findViewById(R.id.scroll_container);

            galleryListRecycle = findViewById(R.id.recycler_gallery_list);
            categoriesListRecycle = findViewById(R.id.recycler_categories_list);
            promotionsListRecycle = findViewById(R.id.recycler_promotions_list);
            contactsListRecycle = findViewById(R.id.recycler_contacts_list);
            aboutContentText = findViewById(R.id.about_text_content);
            mapImageView = findViewById(R.id.map_image_view);

            categoriesListRecycle.setHasFixedSize(true);
            categoriesListRecycle.setLayoutManager(new GridLayoutManager(RestaurantActivity.this, 3));

            promotionsListRecycle.setHasFixedSize(true);
            promotionsListRecycle.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this, LinearLayoutManager.HORIZONTAL, false));

            contactsListRecycle.setHasFixedSize(true);
            contactsListRecycle.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this, LinearLayoutManager.HORIZONTAL, false));

            galleryListRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            galleryListRecycle.setAdapter(galleryAdapter);

            categoriesAdapter = new CategoriesAdapter();
            promotionsAdapter = new PromotionsAdapter();
            contactsAdapter = new ContactsAdapter();

            categoriesListRecycle.setAdapter(categoriesAdapter);
            promotionsListRecycle.setAdapter(promotionsAdapter);
            contactsListRecycle.setAdapter(contactsAdapter);

            galleryAdapter.setUseScrollIt(true);

            serviceAtRestaurant = findViewById(R.id.button_at_restaurant);
            serviceTakeAway = findViewById(R.id.button_takeaway);
            serviceDelivery = findViewById(R.id.button_delivery);

            serviceDelivery.setOnClickListener(view -> changeService(3));

            serviceTakeAway.setOnClickListener(view -> changeService(2));

            serviceAtRestaurant.setOnClickListener(view -> changeService(1));

            initNavigationMenu();

            scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                Logger.log("Y: " + scrollY + " Old Y: " + oldScrollY);
                //Check for categories recycler is being visible
                if (categoriesListRecycle.getLocalVisibleRect(scrollBounds)) {
                    if (!categoriesListRecycle.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < categoriesListRecycle.getHeight()) {
                        //Logger.log("Categories appear parcialy");
                    } else {
                        Logger.log("Categories appeared");
                        //Scrolling to top
                        if (scrollY < oldScrollY && checkedTreeItem != 0) {
                            checkedTreeItem = 0;
                            navigationTree.check(R.id.nav_menu);
                        }
                    }
                } else {
                    //Logger.log("Categories not visible");
                }

                //Check for promotions recycler is being visible
                if (promotionsListRecycle.getLocalVisibleRect(scrollBounds)) {
                    if (!promotionsListRecycle.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < promotionsListRecycle.getHeight()) {
                        //Logger.log("Gallery appear parcialy");
                    } else {
                        if (scrollY > oldScrollY && checkedTreeItem != 1) {
                            checkedTreeItem = 1;
                            navigationTree.check(R.id.nav_promotions);
                            Logger.log("Promotions appeared fully");
                        }
                    }
                } else {
                    Logger.log("Promotions not visible");
                }

                //Check for gallery recycler is being visible
                if (galleryListRecycle.getLocalVisibleRect(scrollBounds)) {
                    if (!galleryListRecycle.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < galleryListRecycle.getHeight()) {
                        //Logger.log("Gallery appear parcialy");
                    } else {
                        if (scrollY > oldScrollY && checkedTreeItem != 2) {
                            checkedTreeItem = 2;
                            navigationTree.check(R.id.nav_photo);
                            Logger.log("Gallery appeared fully");
                        }
                    }
                } else {
                    Logger.log("Gallery not visible");
                }

            });
        } else {
            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            int restaurantContentListSize = getResources().getInteger(R.integer.restaurant_content_list_size);
            // push to adapter fake data
            adapter = new RestaurantsAdapter(this);
            for (int i = 0; i < restaurantContentListSize; i++) {
                adapter.add(new AdapterItemType<>("", null, ItemType.TITLE));
            }
            recycler.setAdapter(adapter);
            galleryList.setAdapter(galleryAdapter);

            galleryList.setOrientation(Orientation.HORIZONTAL);
            galleryList.setSlideOnFling(true);
            galleryList.setItemTransitionTimeMillis(430);
            galleryList.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.85f)
                    .setPivotX(Pivot.X.LEFT)
                    .setPivotY(Pivot.Y.CENTER)
                    .build());
        }




    }

    private void changeService(int selectedService) {
        if (this.selectedService == selectedService)
            return;

        this.selectedService = selectedService;
        Logger.log("Service changed to: " + this.selectedService);

        switch (selectedService) {
            case 1:
                serviceAtRestaurant.setServiceSelected(true);
                serviceTakeAway.setServiceSelected(false);
                serviceDelivery.setServiceSelected(false);
                break;
            case 2:
                serviceTakeAway.setServiceSelected(true);
                serviceAtRestaurant.setServiceSelected(false);
                serviceDelivery.setServiceSelected(false);
                break;
            case 3:
                serviceDelivery.setServiceSelected(true);
                serviceAtRestaurant.setServiceSelected(false);
                serviceTakeAway.setServiceSelected(false);
                break;
        }

        presenter.changeCategories(this.selectedService);
    }

    private void initNavigationMenu() {
        navigationTree = findViewById(R.id.nav_container);
        toMenuBtn = findViewById(R.id.nav_menu);
        toPromotionsBtn = findViewById(R.id.nav_promotions);
        toPhotoBtn = findViewById(R.id.nav_photo);
        toAboutBtn = findViewById(R.id.nav_about);
        toContactsBtn = findViewById(R.id.nav_contacts);

        toMenuBtn.setOnCheckedChangeListener(this);
        toPromotionsBtn.setOnCheckedChangeListener(this);
        toPhotoBtn.setOnCheckedChangeListener(this);
        toAboutBtn.setOnCheckedChangeListener(this);
        toContactsBtn.setOnCheckedChangeListener(this);

    }

    @Override
    public void setData(@NonNull Restaurant data) {
        Logger.log("Restaurant: " + data.toString());

        restaurant = data;
        restaurantTitleView.setText(data.getName());
        restaurantAddressView.setText(data.getAddress());
        restaurantPhoneView.setText(data.getPhones().get(0));
        //TODO: uncomment
        //        restaurantOpeningHours.setText(data.getTiming().get(0));


        if (!isTablet()) {

            int aboutTitlePos = getResources().getInteger(R.integer.about_title_pos);
            int aboutTextPos = getResources().getInteger(R.integer.about_text_pos);

            //add about
            adapter.change(new AdapterItemType<>(getString(R.string.about_restaurant_text), null, ItemType.TITLE), aboutTitlePos);
            adapter.change(new AdapterItemType<>(data.getInformation(), null, ItemType.ABOUT), aboutTextPos);

            // add map
            int mapPos = getResources().getInteger(R.integer.map_pos);
            adapter.change(new AdapterItemType<Image>(restaurant.getLocation().getImage(), null, ItemType.MAP), mapPos);

            // add order
            int selectServicePos = getResources().getInteger(R.integer.select_service_pos);
            adapter.setSelectedService(data.getServices().get(0));
            adapter.change(new AdapterItemType<>(getString(R.string.select_service_text), data.getServices(), ItemType.ORDER_TYPE_PHONE), selectServicePos);
        } else {
//            categoriesAdapter.setRestaurantId(restaurant.getId());

            categoriesAdapter.setOnCategoryClickListener(this);

            // add restaurant image
            String restaurantImageUrl = restaurant.getImage();
            Glide.with(this).load(BuildConfig.BASE_URL +
                    restaurantImageUrl.substring(1, restaurantImageUrl.length())).into(restaurantImage);

            // add about
            aboutContentText.setText(Html.fromHtml(data.getInformation()));

            //add map
            String backgroundUrl = restaurant.getLocation().getImage();
            Glide.with(this).load(BuildConfig.BASE_URL +
                    backgroundUrl.substring(1, backgroundUrl.length())).into(mapImageView);

            if (ListUtils.isEmpty(data.getServices()))
                return;
            selectedService = data.getServices().get(0);
            //Set up select service buttons
            switch (selectedService) {
                case 1:
                    serviceAtRestaurant.setServiceSelected(true);
                    serviceTakeAway.setServiceSelected(false);
                    serviceDelivery.setServiceSelected(false);
                    break;
                case 2:
                    serviceTakeAway.setServiceSelected(true);
                    serviceAtRestaurant.setServiceSelected(false);
                    serviceDelivery.setServiceSelected(false);
                    break;
                case 3:
                    serviceDelivery.setServiceSelected(true);
                    serviceAtRestaurant.setServiceSelected(false);
                    serviceTakeAway.setServiceSelected(false);
                    break;
                default:
                    serviceTakeAway.setServiceSelected(false);
                    serviceAtRestaurant.setServiceSelected(false);
                    serviceDelivery.setServiceSelected(false);
                    break;
            }


            if (data.getServices().size() < 3) {
                if (!data.getServices().contains(1)) {
                    serviceAtRestaurant.setAvailable(false);
                }
                if (!data.getServices().contains(2)) {
                    serviceTakeAway.setAvailable(false);
                }
                if (!data.getServices().contains(3)) {
                    serviceDelivery.setAvailable(false);
                }

            }


        }

        //load restaurant background
        String backgroundUrl = restaurant.getBackground();
        Glide.with(this).load(BuildConfig.BASE_URL +
                backgroundUrl.substring(1, backgroundUrl.length())).into(restaurantBackground);


        setContacts(data);

        if (isTablet())
            scrollView.getHitRect(scrollBounds);

    }

    private void setContacts(@NonNull Restaurant data) {
        ArrayList<Contact> contacts = new ArrayList<>();
        String stringDivider;
        ItemType itemType;

        if (isTablet()) {
            stringDivider = "\n";
            itemType = ItemType.CONTACTS_TABLET;
        } else {
            stringDivider = ", ";
            itemType = ItemType.CONTACTS_PHONE;
        }

        //TODO:
//        // format hours
//        StringBuilder openingHours = new StringBuilder();
//        for (String item : data.getTiming()) {
//            openingHours.append(item).append(stringDivider);
//        }

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

//        contacts.add(new Contact("Opening Hours", openingHours.toString()));
        contacts.add(new Contact(getString(R.string.phones_text), phones.toString()));

        if (isTablet()) {
            contacts.add(new Contact(getString(R.string.social_networks_text), socialNetworks.toString()));
            contactsAdapter.setItems(contacts);
        } else {
            contacts.add(new Contact(getString(R.string.adress_text), data.getAddress()));

            int contactTitlePos = getResources().getInteger(R.integer.contacts_title_pos);
            int contactListPos = getResources().getInteger(R.integer.contacts_list_pos);

            adapter.change(new AdapterItemType<>(getString(R.string.contact_restaurant_text), null, ItemType.TITLE), contactTitlePos);
            adapter.change(new AdapterItemType<>(null, contacts, itemType), contactListPos);
        }
    }

    @Override
    public void setCategories(@NonNull List<Category> categories) {
        Logger.log("Categories: " + categories.toString());

        if (!isTablet()) {
            int menuSectionListPos = getResources().getInteger(R.integer.menu_sections_list_pos);
            adapter.setCategoryClickListener(this);
            adapter.change(new AdapterItemType<>("", categories, ItemType.MENU_PHONE), menuSectionListPos);
        } else {
            categoriesAdapter.setItems(categories);
            scrollView.getHitRect(scrollBounds);

        }
    }

    @Override
    public void setGallery(@NonNull List<Image> images) {
        Logger.log("Gallery: " + images.toString());
        galleryAdapter.setItems(images);

        if (isTablet())
            scrollView.getHitRect(scrollBounds);

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
        if (isTablet())
            scrollView.getHitRect(scrollBounds);

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

        if (isTablet())
            scrollView.getHitRect(scrollBounds);
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
    public void showEmptyView() {
    }

    @Override
    public void hideEmptyView() {
    }

    @Override
    public void onCategoryClicked(int categoryId) {
        CategoryActivity.start(RestaurantActivity.this, restaurant.getId(),1,  categoryId, restaurant.getName());
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked)
            return;
        switch (buttonView.getId()) {
            case R.id.nav_menu:
                if (checkedTreeItem == 0)
                    return;
                checkedTreeItem = 0;
                scrollView.post(() -> scrollView.smoothScrollTo(0, findViewById(R.id.categories_list_title).getTop() - 20));
                break;
            case R.id.nav_promotions:
                if (checkedTreeItem == 1)
                    return;
                checkedTreeItem = 1;
                scrollView.post(() -> scrollView.smoothScrollTo(0, findViewById(R.id.promotions_list_title).getTop() - 20));
                break;
            case R.id.nav_photo:
                if (checkedTreeItem == 2)
                    return;
                checkedTreeItem = 2;
                scrollView.post(() -> scrollView.smoothScrollTo(0, findViewById(R.id.gallery_list_title).getTop() - 20));
                break;
            case R.id.nav_about:
                if (checkedTreeItem == 3)
                    return;
                checkedTreeItem = 3;
                scrollView.post(() -> scrollView.smoothScrollTo(0, findViewById(R.id.about_text_title).getTop() - 20));
                break;
            case R.id.nav_contacts:
                if (checkedTreeItem == 4)
                    return;
                checkedTreeItem = 4;
                scrollView.post(() -> scrollView.smoothScrollTo(0, findViewById(R.id.contact_list_title).getTop() - 20));
                break;
        }
    }


}
