package com.restamenu.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.category.CategoryActivity;
import com.restamenu.data.preferences.KeyValueStorage;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Contact;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Language;
import com.restamenu.model.content.Location;
import com.restamenu.model.content.Promotion;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.adapter.AdapterItemType;
import com.restamenu.restaurant.adapter.CategoriesAdapter;
import com.restamenu.restaurant.adapter.CategoryClickListener;
import com.restamenu.restaurant.adapter.ContactsAdapter;
import com.restamenu.restaurant.adapter.GalleryAdapter;
import com.restamenu.restaurant.adapter.ItemType;
import com.restamenu.restaurant.adapter.OrderTypeSpinnerAdapter;
import com.restamenu.restaurant.adapter.PromotionsAdapter;
import com.restamenu.restaurant.adapter.RestaurantsAdapter;
import com.restamenu.restaurant.adapter.ServiceType;
import com.restamenu.util.ListUtils;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.CustomSpinner;
import com.restamenu.views.custom.CustomSwipeToRefresh;
import com.restamenu.views.custom.ServiceButton;
import com.restamenu.views.custom.StickyScrollView;
import com.restamenu.views.setting.OnSettingItemChanged;
import com.restamenu.views.setting.SettingListener;
import com.restamenu.views.setting.SettingView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BasePresenterActivity<RestaurantsPresenter, RestaurantView, Restaurant>
        implements RestaurantView, CategoryClickListener, RestaurantsAdapter.ChangeServiceListener,
        CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener, OnSettingItemChanged,
        CustomSpinner.OnSpinnerEventsListener, SettingListener {

    public static final String KEY_RESTAURANT_ID = "key_rest_id";

    private Integer restaurantId;
    private int positionForHeader;
    //Only for tablet
    private int selectedService;

    private RestaurantsAdapter adapter;
    private Restaurant restaurant;

    private StickyScrollView scrollView;

    private boolean favorite = false;
    private View favouriteContainer;
    private View settingBtn;
    private ImageView favouriteIconImage;
    private TextView favouriteText;
    private TextView restaurantTitleView;
    private TextView restaurantTypeView;
    private TextView restaurantAddressView;
    private TextView restaurantPhoneView;
    //    private TextView restaurantOpeningHours;
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
    private CustomSpinner orderSpinner;
    private CustomSwipeToRefresh swipeRefreshLayout;
    private TextView categoryTitleView;
    private View logo;
    private View transparentViewTop;
    private View transparentViewBottom;

    private CategoriesAdapter categoriesAdapter;
    private PromotionsAdapter promotionsAdapter;
    private ContactsAdapter contactsAdapter;
    private NestedScrollView nestedScrollView;

    private KeyValueStorage keyValueStorage;

    private SettingView settingView;

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

        keyValueStorage = new KeyValueStorage(this);

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
        return R.layout.toolbar_restaurant;
    }

    private boolean isFavourite() {
        // TODO:
        return favorite;
    }

    @Override
    protected void initViews() {
        scrollBounds = new Rect();

        galleryList = findViewById(R.id.recycler_gallery);
        restaurantTitleView = findViewById(R.id.restaurant_title);
        restaurantTypeView = findViewById(R.id.restaurant_type);
        restaurantAddressView = findViewById(R.id.restaurant_address);
        restaurantPhoneView = findViewById(R.id.restaurant_phone);
        favouriteIconImage = findViewById(R.id.favourite_icon_image);
        favouriteText = findViewById(R.id.favourite_text);
//        restaurantOpeningHours = findViewById(R.id.restaurant_opening_hours);
        favouriteContainer = findViewById(R.id.favourite_container);
        restaurantImage = findViewById(R.id.restaurant_image);
        restaurantBackground = findViewById(R.id.restaurant_background);
        recycler = findViewById(R.id.recycler);
        settingView = findViewById(R.id.settings_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        nestedScrollView = findViewById(R.id.nested_scroll_container);
        categoryTitleView = findViewById(R.id.categories_list_title);
        logo = findViewById(R.id.logo);
        transparentViewTop = findViewById(R.id.transparent_view_top);
        transparentViewBottom = findViewById(R.id.transparent_view_bottom);
        orderSpinner = findViewById(R.id.order_type_spinner);

        galleryAdapter = new GalleryAdapter();
        settingView.setOnSettingItemChanged(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        //TODO:
        favouriteContainer.setOnClickListener(view -> {
            if (isFavourite()) {
                favouriteText.setText("Add to favorite");
                favouriteIconImage.setImageResource(R.drawable.ic_fav_noact);
                favorite = false;
            } else {
                favouriteText.setText("In favorite");
                favouriteIconImage.setImageResource(R.drawable.ic_favorite_active);
                favorite = true;
            }
        });

        settingView.setSettingListener(this);
        logo.setOnClickListener(view -> scrollToTop());

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
            categoriesListRecycle.setNestedScrollingEnabled(false);

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

            String pattern = "Menu sections for %s";


            serviceDelivery.setOnClickListener(view -> {
                String name = serviceDelivery.getTitleText();
                categoryTitleView.setText(String.format(pattern, name.toLowerCase()));
                changeService(3);
            });

            serviceTakeAway.setOnClickListener(view -> {
                String name = serviceTakeAway.getTitleText();
                categoryTitleView.setText(String.format(pattern, name.toLowerCase()));
                changeService(2);
            });

            serviceAtRestaurant.setOnClickListener(view -> {
                String name = serviceAtRestaurant.getTitleText();
                categoryTitleView.setText(String.format(pattern, name.toLowerCase()));
                changeService(1);
            });

            initNavigationMenu();

            scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                Logger.log("Y: " + scrollY + " Old Y: " + oldScrollY);
                //Check for categories recycler is being visible
                if (categoriesListRecycle.getLocalVisibleRect(scrollBounds)) {
                    if (!categoriesListRecycle.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < categoriesListRecycle.getHeight()) {
                        if (scrollY < oldScrollY && checkedTreeItem != 0) {
                            //Scrolling to top of scrollView and became partially visible
                            checkedTreeItem = 0;
                            navigationTree.check(R.id.nav_menu);
                        }
                    } else {
                        //Became visible
                        if (scrollY < oldScrollY && checkedTreeItem != 0) {
                            //Scroll to top of the scrollView
                            Log.d("Scroll", "Categories check");
                            checkedTreeItem = 0;
                            navigationTree.check(R.id.nav_menu);
                        }
                    }
                } else {
                    //Log.d("Scroll", "Categories not visible");
                }

                //Check for promotions recycler is being visible
                if (promotionsListRecycle.getLocalVisibleRect(scrollBounds)) {
                    if (!promotionsListRecycle.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < promotionsListRecycle.getHeight()) {
                        //Log.d("Scroll", "Gallery appear parcialy");
                    } else {
                        if (scrollY > oldScrollY && checkedTreeItem != 1) {
                            //Scrolling to bottom of the scrollView
                            checkedTreeItem = 1;
                            navigationTree.check(R.id.nav_promotions);
                        } else if (scrollY < oldScrollY && checkedTreeItem != 1) {
                            //Scrolling to top of the scrollView
                            checkedTreeItem = 1;
                            navigationTree.check(R.id.nav_promotions);
                        }
                    }
                } else {
                    //Log.d("Scroll", "Promotions not visible");
                }

                //Check for gallery recycler is being visible
                if (galleryListRecycle.getLocalVisibleRect(scrollBounds)) {
                    if (!galleryListRecycle.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < galleryListRecycle.getHeight()) {
                        //Log.d("Scroll", "Gallery appear parcialy");
                    } else {
                        //Log.d("Scroll", "Gallery visible");
                        if (scrollY > oldScrollY && checkedTreeItem != 2) {
                            checkedTreeItem = 2;
                            navigationTree.check(R.id.nav_photo);
                            //Log.d("Scroll", "Gallery check");
                        }
                    }
                } else {
                    //Log.d("Scroll", "Gallery not visible");
                }

            });
        } else {

            settingView.setSettingPopupOnDismissListener(() -> {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                int width = size.x;
                KeyValueStorage keyValueStorage = new KeyValueStorage(this);
                presenter.loadData(width, keyValueStorage.getLanguageId());
            });

            nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                        .getScrollY()));

                if (nestedScrollView.getScrollY() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            });

            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recycler.setNestedScrollingEnabled(false);

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

        if (isTablet()) {
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
            adapter.setRestaurant(restaurant);
            int aboutTitlePos = getResources().getInteger(R.integer.about_title_pos);
            int aboutTextPos = getResources().getInteger(R.integer.about_text_pos);

            //add about
            adapter.change(new AdapterItemType<>(getString(R.string.about_restaurant_text), null, ItemType.TITLE), aboutTitlePos);
            adapter.change(new AdapterItemType<>(data.getInformation(), null, ItemType.ABOUT), aboutTextPos);

            // add map
            int mapPos = getResources().getInteger(R.integer.map_pos);
            adapter.change(new AdapterItemType<Location>(restaurant.getLocation().getImage(), restaurant.getLocation(), ItemType.MAP), mapPos);

            // add order
//            int selectServicePos = getResources().getInteger(R.integer.select_service_pos);
            positionForHeader = 0;
            if (data.getServices().size() < 3)
                if (data.getServices().contains(1)) {
                    positionForHeader = 0;
                } else if (data.getServices().contains(2)) {
                    positionForHeader = 1;
                } else if (data.getServices().contains(3)) {
                    positionForHeader = 2;
                } else {
                    positionForHeader = -1;
                }

//            if (positionForHeader != -1)
//                adapter.setSpinnerSelectedPosition(positionForHeader);

//            adapter.change(new AdapterItemType<>(getString(R.string.select_service_text), data.getServices(), ItemType.ORDER_TYPE_PHONE), selectServicePos);

            initSpinner(data.getServices());

        } else {
            categoriesAdapter.setOnCategoryClickListener(this);

            // add restaurant image
            String restaurantImageUrl = restaurant.getImage();
            Glide.with(this).load(BuildConfig.BASE_URL +
                    restaurantImageUrl.substring(1, restaurantImageUrl.length()) + BuildConfig.IMAGE_WIDTH_400).into(restaurantImage);

            // add about
            aboutContentText.setText(Html.fromHtml(data.getInformation()));

            //add map
            String backgroundUrl = restaurant.getLocation().getImage();
            Glide
                    .with(this)
                    .load(BuildConfig.BASE_URL + backgroundUrl.substring(1, backgroundUrl.length()))
                    .apply(new RequestOptions()
                            .override(800, 800)
                            .fitCenter())
                    .into(mapImageView);

            mapImageView.setOnClickListener(view -> showMap(restaurant.getLocation()));

            if (ListUtils.isEmpty(data.getServices()))
                return;
            selectedService = data.getServices().get(0);

            serviceAtRestaurant.performClick();

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

        if (isTablet()) {
            scrollView.getHitRect(scrollBounds);
        }

    }

    public void openMapInBrowser(Location location) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri geoLocation = Uri.parse("https://www.google.com.ua/maps/place/" + restaurant.getName() + "/@" + location.getGeo() + ",21z");
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showMap(Location location) {
        Uri gmmIntentUri = Uri.parse("geo:" + location.getGeo() + "?q=restaurants");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null)
            startActivity(mapIntent);
        else
            openMapInBrowser(location);
    }

    public void scrollToTop() {
        if (isTablet()) {
            scrollView.scrollTo(0, 0);
        } else {
            nestedScrollView.scrollTo(0, 0);
        }
    }


    private void initSpinner(List<Integer> services) {
        Logger.log("initSpinner");

        ArrayList<ServiceType> serviceTypes = new ArrayList<>();
        serviceTypes.add(ServiceType.RESTAURANT);
        serviceTypes.add(ServiceType.TAKEAWAY);
        serviceTypes.add(ServiceType.DELIVERY);

        OrderTypeSpinnerAdapter orderTypeSpinnerAdapter = new OrderTypeSpinnerAdapter(this,
                serviceTypes, services);
        orderSpinner.setAdapter(orderTypeSpinnerAdapter);
        orderSpinner.setDropDownVerticalOffset((int) getResources().getDimension(R.dimen.order_type_header_item_height));

        if (positionForHeader != -1)
            orderSpinner.setSelection(positionForHeader);

        orderSpinner.setSpinnerEventsListener(this);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeService((position + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        // format socials
        StringBuilder socialNetworks = new StringBuilder();
        for (int i = 0; i < data.getSocial().size(); i++) {
            if (i < data.getPhones().size() - 1) {
                socialNetworks.append(data.getSocial().get(i)).append(stringDivider);
            } else {
                socialNetworks.append(data.getSocial().get(i));
            }
        }

        // format phones
        StringBuilder phones = new StringBuilder();
        for (int i = 0; i < data.getPhones().size(); i++) {
            if (i < data.getPhones().size() - 1) {
                phones.append(data.getPhones().get(i)).append(stringDivider);
            } else {
                phones.append(data.getPhones().get(i));
            }
        }


        contacts.add(new Contact("Opening Hours", "Mon - Fri 10:00 - 23:00"));
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
    public void setLanguages(@NonNull List<Language> languages) {
        List<Language> restLangList = new ArrayList<>();

        for (Integer langId : restaurant.getLanguages()) {
            for (int i = 0; i < languages.size(); i++) {
                if (languages.get(i).getLanguage_id() == langId) {
                    restLangList.add(languages.get(i));
                }
            }
        }

        settingView.setLanguages(restLangList);
    }

    @Override
    public void setCurrencies(@NonNull List<Currency> currencies) {
        List<Currency> restCurrList = new ArrayList<>();

        for (Integer currId : restaurant.getCurrencies()) {
            for (int i = 0; i < currencies.size(); i++) {
                if (currencies.get(i).getCurrency_id() == currId) {
                    restCurrList.add(currencies.get(i));
                }
            }
        }
        settingView.setCurrencies(restCurrList);
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

        if (isTablet()) {
            scrollView.getHitRect(scrollBounds);
        }

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
        if (isTablet()) {
            scrollView.getHitRect(scrollBounds);
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

        if (isTablet()) {
            scrollView.getHitRect(scrollBounds);
        }
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
        KeyValueStorage keyValueStorage = new KeyValueStorage(this);
        presenter.loadData(width, keyValueStorage.getLanguageId());

    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void hideEmptyView() {
    }

    @Override
    public void onCategoryClicked(int categoryId) {

        CategoryActivity.start(RestaurantActivity.this, restaurant.getId(),
                restaurant.getName(), 1, categoryId,
                restaurant.getCurrencies(), restaurant.getLanguages());
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
    public void onSpinnerOpened(Spinner spin) {
        if (!isTablet()) {
            transparentViewBottom.setVisibility(View.VISIBLE);
            transparentViewTop.setVisibility(View.VISIBLE);
        } else {

        }
    }

    @Override
    public void onSpinnerClosed(Spinner spin) {
        if (!isTablet()) {
            transparentViewBottom.setVisibility(View.INVISIBLE);
            transparentViewTop.setVisibility(View.INVISIBLE);
        } else {

        }
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
                scrollView.post(() -> {
                            scrollView.scrollTo(0, 0);
                            scrollView.scrollTo(0, findViewById(R.id.categories_list_title).getTop());
                        }
                );
                break;
            case R.id.nav_promotions:
                if (checkedTreeItem == 1)
                    return;
                checkedTreeItem = 1;
                scrollView.post(() -> {
                    scrollView.scrollTo(0, 0);
                    scrollView.scrollTo(0, findViewById(R.id.promotions_list_title).getTop());
                });
                break;
            case R.id.nav_photo:
                if (checkedTreeItem == 2)
                    return;
                checkedTreeItem = 2;
                scrollView.post(() -> {
                    scrollView.scrollTo(0, 0);
                    scrollView.scrollTo(0, findViewById(R.id.gallery_list_title).getTop());
                });
                break;
            case R.id.nav_about:
                if (checkedTreeItem == 3)
                    return;
                checkedTreeItem = 3;
                scrollView.post(() -> {
                    scrollView.scrollTo(0, 0);
                    scrollView.scrollTo(0, findViewById(R.id.about_text_title).getTop());
                });
                break;
            case R.id.nav_contacts:
                if (checkedTreeItem == 4)
                    return;
                checkedTreeItem = 4;
                scrollView.post(() -> {
                            scrollView.scrollTo(0, 0);
                            scrollView.scrollTo(0, findViewById(R.id.contact_list_title).getTop());
                        }
                );
                break;
        }
    }

    @Override
    public void finish() {
        // dismiss setting popup window if it's showing on phone
        if (!isTablet()) {
            if (settingView.getSettingPopup().isShowing()) {
                settingView.dismissSettingPopup();
                return;
            }
        }

        super.finish();
    }

    @Override
    public void onRefresh() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        KeyValueStorage keyValueStorage = new KeyValueStorage(this);
        presenter.loadData(width, keyValueStorage.getLanguageId());
    }

    @Override
    public void showLoading(boolean show) {
        super.showLoading(show);

        if (!show) {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onLanguageChanged(Language language) {

    }

    @Override
    public void onCurrencyChanged(Currency currency) {

    }


    @Override
    public void popupOpened() {
        showTransparentView(true);
    }

    @Override
    public void popupClosed() {
        showTransparentView(false);
    }
}
