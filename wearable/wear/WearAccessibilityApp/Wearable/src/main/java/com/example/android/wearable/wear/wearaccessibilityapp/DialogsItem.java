package com.example.android.wearable.wear.wearaccessibilityapp;

import android.app.Dialog;

import java.util.function.Supplier;

public class DialogsItem implements Item {
    private final int mItemId;
    private final Supplier<Dialog> mSupplier;

    public DialogsItem(int itemId, Supplier<Dialog> supplier) {
        mItemId = itemId;
        mSupplier = supplier;
    }

    public int getItemId() {
        return mItemId;
    }

    public Supplier<Dialog> getSupplier() {
        return mSupplier;
    }
}