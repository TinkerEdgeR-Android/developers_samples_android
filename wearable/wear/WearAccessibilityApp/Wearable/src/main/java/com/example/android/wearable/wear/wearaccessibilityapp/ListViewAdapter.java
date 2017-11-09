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

public class ListViewAdapter<T extends Item> extends ArrayAdapter<T> {
    private final LayoutInflater mInflater;
    private List<T> mItems;

    public ListViewAdapter(@NonNull Context context, @NonNull List<T> items) {
        super(context, R.layout.list_item_layout, items);
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_layout, parent, false);
            holder = new Holder();
            holder.mTextView = convertView.findViewById(R.id.item_text);
            holder.mImageView = convertView.findViewById(R.id.item_image);
            convertView.setTag(holder); // Cache the holder for future use.
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.mTextView.setText(mItems.get(position).getItemId());
        return convertView;
    }

    private static class Holder {
        TextView mTextView;
        ImageView mImageView;
    }
}