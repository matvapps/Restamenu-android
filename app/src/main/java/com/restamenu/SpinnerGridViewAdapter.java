package com.restamenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.exblr.dropdownmenu.DropdownListItem;

import java.util.List;

public class SpinnerGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<DropdownListItem> mList;

    public SpinnerGridViewAdapter(Context context, List<DropdownListItem> list) {
        mContext=context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DropdownListItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_content_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.bind(position);
        return convertView;
    }

    public DropdownListItem setSelectedItem(int position) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelected(position == i);
        }
        notifyDataSetChanged();
        return mList.get(position);
    }

    private class ViewHolder {
        private TextView mTextView;

        public ViewHolder(View view) {
            mTextView = (TextView) view.findViewById(R.id.text);
        }

        public void bind(int position) {
            DropdownListItem item = mList.get(position);
            mTextView.setText(item.getText());
            if (item.isSelected()) {
                mTextView.setTextColor(mContext.getResources().getColor(R.color.dusty_red));
                mTextView.setBackgroundResource(R.drawable.circle_red_shape);
            } else {
                mTextView.setTextColor(mContext.getResources().getColor(R.color.black));
                mTextView.setBackgroundResource(R.drawable.circle_white_shape);
            }
        }
    }
}