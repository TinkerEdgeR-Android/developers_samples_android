package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;

public class ZoomImageActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        // Check if integer was actually given.
        if(!(getIntent().hasExtra(getString(R.string.intent_extra_image)))) {
            throw new NotFoundException("Expecting extras");
        }

        // Grab the resource id from extras and set the image resource.
        int value = getIntent().getIntExtra(getString(R.string.intent_extra_image), 0);
        ImageView expandedImage = findViewById(R.id.expanded_image);
        expandedImage.setImageResource(value);
    }
}