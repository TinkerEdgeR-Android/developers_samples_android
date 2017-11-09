package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Context;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;

public class MyLauncherChildLayoutManager extends CurvedChildLayoutManager {

    /**
     * How much should we scale the icon at most.
     */
    private static final float MAX_ICON_PROGRESS = 0.65f;

    public MyLauncherChildLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void updateChild(View child, WearableRecyclerView parent) {
        super.updateChild(child, parent);

        // Figure out % progress from top to bottom.
        float centerOffset = (child.getHeight() / 2.0f) / parent.getHeight();

        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        // Normalize for center.
        float progressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
        // Adjust to the maximum scale.
        progressToCenter = Math.min(progressToCenter, MAX_ICON_PROGRESS);

        child.setScaleX(1 - progressToCenter);
        child.setScaleY(1 - progressToCenter);
    }
}