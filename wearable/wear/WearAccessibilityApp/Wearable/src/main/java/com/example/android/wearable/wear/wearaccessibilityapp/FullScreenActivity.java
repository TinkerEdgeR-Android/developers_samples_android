package com.example.android.wearable.wear.wearaccessibilityapp;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

public class FullScreenActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_progress);
    }

}