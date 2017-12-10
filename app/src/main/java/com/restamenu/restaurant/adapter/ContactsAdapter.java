package com.restamenu.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Contact> items;

    public ContactsAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<Contact> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<Contact> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_contact_item, parent, false);

        return new ContactViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Contact contact = items.get(position);

        ContactViewHolder contactViewHolder = (ContactViewHolder) holder;

        contactViewHolder.title.setText(contact.getTitle());
        contactViewHolder.content.setText(contact.getContent());

        if (position == items.size() - 1)
            contactViewHolder.divider.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private View divider;
        private TextView title;
        private TextView content;
        private ImageView icon;

        public ContactViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.contact_title);
            content = itemView.findViewById(R.id.contact_content);
            icon = itemView.findViewById(R.id.contact_icon);
            divider = itemView.findViewById(R.id.contact_divider);
        }
    }

}