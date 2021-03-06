package com.restamenu.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Contact;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Location;
import com.restamenu.model.content.Promotion;
import com.restamenu.model.content.Restaurant;
import com.restamenu.util.AndroidUtils;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.ServiceButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ChangeServiceListener listener;
    private List<AdapterItemType> items;
    private int selectedService;
    private int spinnerSelectedPosition = 0;
    private Restaurant restaurant;
    Context context;


    private CategoryClickListener categoryClickListener;

    public RestaurantsAdapter(ChangeServiceListener listener) {
        this.listener = listener;
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

    @Override
    public int getItemCount() {
        return items.size();
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

    public void setSpinnerSelectedPosition(int position) {
        this.spinnerSelectedPosition = position;
    }

    public void setSelectedService(int selectedService) {
        this.selectedService = selectedService;
    }

    private void changeService(int selectedService) {
        this.selectedService = selectedService;
        Logger.log("Service changed to: " + this.selectedService);
        listener.onServiceChanged(this.selectedService);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType().getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        context = parent.getContext();

        switch (viewType) {
//            case 0:
//                rootView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.order_type_item, parent, false);
//                return new OrderTypePhoneViewHolder(rootView);

//            case 1:
//                rootView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.order_type_item, parent, false);
//                return new OrderTypeTabletViewHolder(rootView);
            case 2:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item_title, parent, false);
                return new TitleViewHolder(rootView);
            case 3:
            case 4:
            case 5:
            case 6:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item_list, parent, false);
                return new ListViewHolder(rootView);
            case 7:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item_text, parent, false);
                return new AboutViewHolder(rootView);
            case 8:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item_list, parent, false);
                return new ListViewHolder(rootView);
            case 9:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item_image, parent, false);
                return new ImageViewHolder(rootView);
            case 10:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item_list, parent, false);
                return new ListViewHolder(rootView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AdapterItemType item = items.get(position);
        switch (holder.getItemViewType()) {
//            case 0: {
//                AdapterItemType<List<Integer>> serviceItemType = (AdapterItemType<List<Integer>>) item;
//                OrderTypePhoneViewHolder viewHolder = (OrderTypePhoneViewHolder) holder;
//                ArrayList<ServiceType> serviceTypes = new ArrayList<>();
//                serviceTypes.add(ServiceType.RESTAURANT);
//                serviceTypes.add(ServiceType.TAKEAWAY);
//                serviceTypes.add(ServiceType.DELIVERY);
//
//                OrderTypeSpinnerAdapter orderTypeSpinnerAdapter = new OrderTypeSpinnerAdapter(holder.itemView.getContext(),
//                        serviceTypes, serviceItemType.getData());
//                viewHolder.spinner.setAdapter(orderTypeSpinnerAdapter);
//                viewHolder.spinner.setDropDownVerticalOffset((int) viewHolder.itemView.getContext().getResources().getDimension(R.dimen.order_type_header_item_height));
//                viewHolder.spinner.setSelection(spinnerSelectedPosition);
//                viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        changeService((position + 1));
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//
//                break;
//            }
            case 1: {
                AdapterItemType<List<Integer>> serviceItemType = (AdapterItemType<List<Integer>>) item;
                OrderTypeTabletViewHolder viewHolder = (OrderTypeTabletViewHolder) holder;

                switch (selectedService) {
                    case 1:
                        viewHolder.buttonAtRestaurant.setServiceSelected(true);
                        viewHolder.buttonTakeAway.setServiceSelected(false);
                        viewHolder.buttonDelivery.setServiceSelected(false);
                        break;
                    case 2:
                        viewHolder.buttonTakeAway.setServiceSelected(true);
                        viewHolder.buttonAtRestaurant.setServiceSelected(false);
                        viewHolder.buttonDelivery.setServiceSelected(false);
                        break;
                    case 3:
                        viewHolder.buttonDelivery.setServiceSelected(true);
                        viewHolder.buttonAtRestaurant.setServiceSelected(false);
                        viewHolder.buttonTakeAway.setServiceSelected(false);
                        break;
                }

                if (serviceItemType.getData().size() < 3) {
                    if (!serviceItemType.getData().contains(1)) {
                        viewHolder.buttonAtRestaurant.setAvailable(false);
                    }
                    if (!serviceItemType.getData().contains(2)) {
                        viewHolder.buttonTakeAway.setAvailable(false);
                    }
                    if (!serviceItemType.getData().contains(3)) {
                        viewHolder.buttonDelivery.setAvailable(false);
                    }
                }

                viewHolder.buttonDelivery.setOnClickListener(view -> {
                    if (selectedService != 3) {
                        changeService(3);
                        notifyItemChanged(position);
                    }
                });

                viewHolder.buttonTakeAway.setOnClickListener(view -> {
                    if (selectedService != 2) {
                        changeService(2);
                        notifyItemChanged(position);
                    }
                });

                viewHolder.buttonAtRestaurant.setOnClickListener(view -> {
                    if (selectedService != 1) {
                        changeService(1);
                        notifyItemChanged(position);
                    }
                });
                break;
            }
            case 2: {
                TitleViewHolder viewHolder = (TitleViewHolder) holder;
                String aboutTitle = viewHolder.itemView.getContext().getString(R.string.about_restaurant_text);

                if (!item.getTitle().equals(aboutTitle))
                    viewHolder.title.setText(item.getTitle());
                else {
                    viewHolder.title.setText(item.getTitle());
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)viewHolder.title.getLayoutParams();
                    params.setMargins(0, params.topMargin, 0, (int) AndroidUtils.dpToPixel(8, viewHolder.itemView.getContext()));

                    viewHolder.title.setLayoutParams(params);
                }

                break;
            }
            // MENU_PHONE
            case 3: {
                AdapterItemType<List<Category>> categoryItemType = (AdapterItemType<List<Category>>) item;

                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));

                CategoriesAdapter categoriesAdapter = new CategoriesAdapter();
                categoriesAdapter.setOnCategoryClickListener(categoryClickListener);
//                categoriesAdapter.setRestaurantId(Integer.parseInt(item.getTitle()));
                categoriesAdapter.setItems(categoryItemType.getData());

                viewHolder.recycler.setAdapter(categoriesAdapter);
                break;
            }
            // MENU_TABLET
            case 4: {
                AdapterItemType<List<Category>> categoryItemType = (AdapterItemType<List<Category>>) item;

                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new GridLayoutManager(viewHolder.itemView.getContext(), 3));

                CategoriesAdapter categoriesAdapter = new CategoriesAdapter();
                categoriesAdapter.setRestaurantTitle(item.getTitle());
                categoriesAdapter.setItems(categoryItemType.getData());

                viewHolder.recycler.setAdapter(categoriesAdapter);
                break;
            }
            // Promotion
            case 5: {
                AdapterItemType<List<Promotion>> promotionsItemType = (AdapterItemType<List<Promotion>>) item;

                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                viewHolder.recycler.setClipToPadding(false);
                viewHolder.recycler.setPadding(0, 0, (int) AndroidUtils.dpToPixel(16, viewHolder.itemView.getContext()), 0);


                PromotionsAdapter promotionsAdapter = new PromotionsAdapter();
                promotionsAdapter.setItems(promotionsItemType.getData());

                viewHolder.recycler.setAdapter(promotionsAdapter);
                break;
            }
            // Gallery
            case 6: {
                AdapterItemType<List<Image>> galleryItemType = (AdapterItemType<List<Image>>) item;

                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));

                GalleryAdapter galleryAdapter = new GalleryAdapter();
                galleryAdapter.setItems(galleryItemType.getData());

                viewHolder.recycler.setAdapter(galleryAdapter);
                break;
            }
            // About
            case 7: {
                AboutViewHolder viewHolder = (AboutViewHolder) holder;
                viewHolder.content.setText(Html.fromHtml(item.getTitle()));
                break;
            }
            // Contacts
            case 8: {
                AdapterItemType<List<Contact>> contactsItemType = (AdapterItemType<List<Contact>>) item;

                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));

                ContactsAdapter contactsAdapter = new ContactsAdapter();
                contactsAdapter.setItems(contactsItemType.getData());

                viewHolder.recycler.setAdapter(contactsAdapter);
                break;
            }
            // Map
            case 9: {

                AdapterItemType<Location> mapItem = (AdapterItemType<Location>) item;
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;

                // load map
                String backgroundUrl = item.getTitle().substring(1, mapItem.getTitle().length());
                Glide.with(holder.itemView.getContext())
                        .load(BuildConfig.BASE_URL + backgroundUrl)
                        .apply(new RequestOptions()
                                .override(800, 800)
                                .fitCenter())
                        .into(imageViewHolder.image);
                imageViewHolder.image.setVisibility(View.VISIBLE);
                imageViewHolder.image.setOnClickListener(view -> showMap(mapItem.getData()));
                break;
            }
            case 10: {
                AdapterItemType<List<Contact>> contactsItemType = (AdapterItemType<List<Contact>>) item;

                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));

                ContactsAdapter contactsAdapter = new ContactsAdapter();
                contactsAdapter.setItems(contactsItemType.getData());

                viewHolder.recycler.setAdapter(contactsAdapter);
            }
        }
    }

    public void openMapInBrowser(Location location) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri geoLocation = Uri.parse("https://www.google.com.ua/maps/place/" + restaurant.getName() + "/@" + location.getGeo() + ",21z" );
        intent.setData(geoLocation);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
    public void showMap(Location location) {
        Uri gmmIntentUri = Uri.parse("geo:" + location.getGeo() + "?q=restaurants");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(mapIntent);
        else
            openMapInBrowser(location);
    }


    public CategoryClickListener getCategoryClickListener() {
        return categoryClickListener;
    }

    public void setCategoryClickListener(CategoryClickListener categoryClickListener) {
        this.categoryClickListener = categoryClickListener;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public interface ChangeServiceListener {
        void onServiceChanged(int serviceId);
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
        }
    }

//    class OrderTypePhoneViewHolder extends RecyclerView.ViewHolder {
//
//        //private TextView title;
//        //private ImageView image;
//        private CustomSpinner spinner;
//
//
//        public OrderTypePhoneViewHolder(View itemView) {
//            super(itemView);
//
//            spinner = itemView.findViewById(R.id.order_type_spinner);
//            //image = itemView.findViewById(R.id.image);
//        }
//    }

    class OrderTypeTabletViewHolder extends RecyclerView.ViewHolder {

        //private LinearLayout layout;
        private ServiceButton buttonDelivery;
        private ServiceButton buttonTakeAway;
        private ServiceButton buttonAtRestaurant;


        public OrderTypeTabletViewHolder(View itemView) {
            super(itemView);
            //layout = itemView.findViewById(R.id)
            buttonDelivery = itemView.findViewById(R.id.button_delivery);
            buttonTakeAway = itemView.findViewById(R.id.button_takeaway);
            buttonAtRestaurant = itemView.findViewById(R.id.button_at_restaurant);
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recycler;

        public ListViewHolder(View itemView) {
            super(itemView);
            recycler = itemView.findViewById(R.id.recycler);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;


        public ImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.map_image);

        }
    }

    class AboutViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        public AboutViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }


//    class ContactsViewHolder extends RecyclerView.ViewHolder {
//        public ContactsViewHolder(View itemView) {
//            super(itemView);
//        }
//    }

}
