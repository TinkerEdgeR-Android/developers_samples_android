package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppItemListViewAdapter extends ArrayAdapter<AppItem> {
    private final LayoutInflater mInflater;
    private List<AppItem> mItems;

    public AppItemListViewAdapter(@NonNull Context context, @NonNull List<AppItem> items) {
        super(context, R.layout.app_item_layout, items);
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.shifted_app_item_layout, parent, false);
            holder = new Holder();
            holder.mTextView = convertView.findViewById(R.id.shifted_icon_text_view);
            holder.mImageView = convertView.findViewById(R.id.shifted_icon_image_view);
            convertView.setTag(holder); // Cache holder for future use.
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.mTextView.setText(mItems.get(position).getItemName());
        holder.mImageView.setImageResource(mItems.get(position).getImageId());

        return convertView;
    }

    private static class Holder {
        TextView mTextView;
        ImageView mImageView;
    }
}