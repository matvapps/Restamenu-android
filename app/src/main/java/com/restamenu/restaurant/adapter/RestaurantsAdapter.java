package com.restamenu.restaurant.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.restamenu.R;

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
                        .inflate(R.layout.restaurant_card_nearby, parent, false);
                return new ListViewHolder(rootView);
            case 7:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item_text, parent, false);
                return new AboutViewHolder(rootView);
            case 8:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_card_nearby, parent, false);
                return new ContactsViewHolder(rootView);
            case 9:
                //Map view holder
                return null;
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
            case 3: {
                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
                //TODO create and set adapter
                //Get and cast data, that needed in adapter: List<String> data = (List<String>)item.getData();
                //viewHolder.recycler.setAdapter();
                break;
            }
            case 4: {
                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new GridLayoutManager(viewHolder.itemView.getContext(), 3));
                //TODO create and set adapter
                //viewHolder.recycler.setAdapter();
                break;
            }
            case 5: {
                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                //TODO create and set adapter
                //viewHolder.recycler.setAdapter();
                break;
            }
            case 6: {
                ListViewHolder viewHolder = (ListViewHolder) holder;
                viewHolder.recycler.setHasFixedSize(true);
                viewHolder.recycler.setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                //TODO create and set adapter
                //viewHolder.recycler.setAdapter();
                break;
            }
            case 7: {
                AboutViewHolder viewHolder = (AboutViewHolder) holder;
                viewHolder.content.setText(item.getTitle());
                break;
            }
            case 8: {
                break;
            }
            case 9: {
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
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
