package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.activity.WearableActivity;

public class ListsItem implements Item {
    private final int mItemId;
    private final Class mClass;

    public ListsItem(int itemId, Class<? extends WearableActivity> clazz) {
        mItemId = itemId;
        mClass = clazz;
    }

    public int getItemId() {
        return mItemId;
    }

    public void launchActivity(Context context) {
        context.startActivity(new Intent(context, mClass));
    }
}