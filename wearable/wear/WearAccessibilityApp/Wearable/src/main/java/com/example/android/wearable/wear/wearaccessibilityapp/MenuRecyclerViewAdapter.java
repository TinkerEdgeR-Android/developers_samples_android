package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MenuRecyclerViewAdapter extends
    RecyclerView.Adapter<MenuRecyclerViewAdapter.Holder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<AppItem> mItems;

    public MenuRecyclerViewAdapter(Context context, List<AppItem> items) {
        this.mContext = context;
        this.mItems = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        /* Add check for viewType here if used.
           See LongListRecyclerViewAdapter for an example. */

        return new Holder(mInflater.inflate(R.layout.app_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (mItems.isEmpty()) {
            return;
        }
        final AppItem item = mItems.get(position);

        if (item.getViewType() == SampleAppConstants.HEADER_FOOTER) {
            return;
        }

        holder.bind(item);

        // Start new activity on click of specific item.
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.get(pos).launchActivity(mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getViewType();
    }


    static class Holder extends ViewHolder {
        TextView mTextView;
        ImageView mImageView;

        public Holder(final View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.icon_text_view);
            mImageView = itemView.findViewById(R.id.icon_image_view);
        }

        /**
         * Bind appItem info to main screen (displays the item).
         */
        public void bind(AppItem item) {
            mTextView.setText(item.getItemName());
            mImageView.setImageResource(item.getImageId());
        }
    }
}