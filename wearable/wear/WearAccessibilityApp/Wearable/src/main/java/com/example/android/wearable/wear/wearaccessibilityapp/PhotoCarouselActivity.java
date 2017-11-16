package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PhotoCarouselActivity extends WearableActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_carousel);

        // Grab reference to each image in activity_photo_carousel.
        ImageView catImage = findViewById(R.id.cat_image);
        catImage.setTag(R.drawable.cats);
        catImage.setOnClickListener(this);

        ImageView dogImage = findViewById(R.id.dog_image);
        dogImage.setTag(R.drawable.dog);
        dogImage.setOnClickListener(this);

        ImageView hamsterImage = findViewById(R.id.hamster_image);
        hamsterImage.setTag(R.drawable.hamster);
        hamsterImage.setOnClickListener(this);

        ImageView birdImage = findViewById(R.id.bird_image);
        birdImage.setTag(R.drawable.birds);
        birdImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ZoomImageActivity.class);
        intent.putExtra(getString(R.string.intent_extra_image),
                (int) v.getTag());
        startActivity(intent);
    }
}