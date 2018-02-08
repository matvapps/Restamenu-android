package com.restamenu.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Contact;
import com.restamenu.util.AndroidUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Contact> items;
    private Context context;

    private final String CONTACT_FB = "fb.com";
    private final String CONTACT_INSTAGRAM = "instagram";

    private final String HYPERLINK_PATTERN = "%s <a href=%s >%s</a><br/>";

    private final String FACEBOOK_TITLE = "facebook: ";
    private final String INSTAGRAM_TITLE = "instagram: ";

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
        context = holder.itemView.getContext();

        contactViewHolder.title.setText(contact.getTitle());
        contactViewHolder.content.setText(contact.getContent());

        if (position == items.size() - 1)
            contactViewHolder.divider.setVisibility(View.INVISIBLE);

        switch (contact.getTitle()) {

            case "Phones": {
                contactViewHolder.icon.setImageResource(R.drawable.ic_calldown);
                Linkify.addLinks(contactViewHolder.content, Linkify.PHONE_NUMBERS);
                AndroidUtils.stripUnderlines(contactViewHolder.content);
                break;
            }
            case "Opening Hours": {
                contactViewHolder.icon.setImageResource(R.drawable.ic_timedown);
                break;
            }
            case "Social Networks": {
                contactViewHolder.icon.setImageResource(R.drawable.ic_soc_networks);

                List<String> socials = Arrays.asList(contact.getContent().split("\n"));
                contactViewHolder.content.setText(Html.fromHtml(formatSocials(socials)));
                AndroidUtils.stripUnderlines(contactViewHolder.content);
                contactViewHolder.content.setMovementMethod(LinkMovementMethod.getInstance());

                break;
            }
            case "Address": {
                contactViewHolder.icon.setImageResource(R.drawable.ic_place_black);
                break;
            }

        }

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
    private String formatSocials(List<String> socials) {

        StringBuilder socialNetworks = new StringBuilder();
        String socialTitle = "";
        String hyperText = "";
        for (String item : socials) {
            if (item.contains(CONTACT_FB)) {
                hyperText = item.substring(item.lastIndexOf("/"));
                hyperText = hyperText.replace("/", "@");
                socialTitle = FACEBOOK_TITLE;
            } else if (item.contains(CONTACT_INSTAGRAM)) {
                hyperText = item.substring(item.lastIndexOf("/"));
                socialTitle = INSTAGRAM_TITLE;
            }

            item = String.format(HYPERLINK_PATTERN, socialTitle, item, hyperText);
            socialNetworks.append(item).append("\n");
        }


        return socialNetworks.toString();
    }

}