package com.example.android.wearable.wear.wearaccessibilityapp;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Menu items
        List<AppItem> items = new ArrayList<AppItem>();
        items.add(new AppItem(getString(R.string.lists),
                R.drawable.lists_circle, ListsActivity.class));
        items.add(new AppItem(getString(R.string.dialogs),
                R.drawable.dialogs_circle, DialogsActivity.class));
        items.add(new AppItem(getString(R.string.progress),
                R.drawable.progress_circle, ProgressActivity.class));
        items.add(new AppItem(getString(R.string.controls),
                R.drawable.controls_circle, ControlsActivity.class));
        items.add(new AppItem(getString(R.string.notifications),
                R.drawable.notifications_circle, NotificationsActivity.class));
        items.add(new AppItem(getString(R.string.accessibility),
                R.drawable.accessibility_circle, AccessibilityActivity.class));

        MenuRecyclerViewAdapter appListAdapter = new MenuRecyclerViewAdapter(this, items);

        WearableRecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setAdapter(appListAdapter);
        recyclerView
                .setLayoutManager(new MyLauncherChildLayoutManager(this));  // For curved layout.
        recyclerView.setCenterEdgeItems(true);
    }
}