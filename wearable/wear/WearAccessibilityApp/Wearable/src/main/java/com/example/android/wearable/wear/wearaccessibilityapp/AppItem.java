package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.activity.WearableActivity;

public class AppItem {
    private final String mItemName;
    private final int mImageId;
    private final int mViewType;
    private final Class mClass;

    public AppItem(String itemName, int imageId, int viewType, Class<?
            extends WearableActivity> clazz) {
        mItemName = itemName;
        mImageId = imageId;
        mViewType = viewType;
        mClass = clazz;
    }

    public AppItem(String itemName, int imageId, Class<? extends WearableActivity> clazz) {
        mItemName = itemName;
        mImageId = imageId;
        mViewType = SampleAppConstants.NORMAL;
        mClass = clazz;
    }

    public String getItemName() {
        return mItemName;
    }

    public int getImageId() {
        return mImageId;
    }

    public int getViewType() {
        return mViewType;
    }

    public void launchActivity(Context context) {
        context.startActivity(new Intent(context, mClass));
    }
}