package com.restamenu.restaurant.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Contact;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Promotion;
import com.restamenu.util.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AdapterItemType> items;

    public RestaurantsAdapter() {
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
    public int getItemViewType(int position) {
//
//        if (items.get(position).getType() == ItemType.TITLE
//                && items.get(position).getTitle().equals(""))
//            return -1;

        return items.get(position).getType().getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        switch (viewType) {
            case 0:
            case 1:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_card_nearby, parent, false);
                return new OrderTypeViewHolder(rootView);
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
            case 0: {
                OrderTypeViewHolder viewHolder = (OrderTypeViewHolder) holder;
                //TODO
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                TitleViewHolder viewHolder = (TitleViewHolder) holder;
                viewHolder.title.setText(item.getTitle());
                break;
            }
            // MENU_PHONE
            case 3: {
                AdapterItemType<List<Category>> categoryItemType = (AdapterItemType<List<Category>>) item;

                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));

                CategoriesAdapter categoriesAdapter = new CategoriesAdapter();
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
            case 9: {
                // TODO:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
//                imageViewHolder.image.setImageResource(R.drawable.test_map);

                // load map
                // TODO:
                String backgroundUrl = item.getTitle().replace("width", "?width");
                Picasso.with(holder.itemView.getContext()).load(BuildConfig.BASE_URL + backgroundUrl).into(imageViewHolder.image);

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

    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
        }
    }

    class OrderTypeViewHolder extends RecyclerView.ViewHolder {
        public OrderTypeViewHolder(View itemView) {
            super(itemView);
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


    class ContactsViewHolder extends RecyclerView.ViewHolder {
        public ContactsViewHolder(View itemView) {
            super(itemView);
        }
    }

}
