package com.example.android.wearable.wear.wearaccessibilityapp;

import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;

public class OpenOnPhoneAnimationActivity extends WearableActivity {
    private AnimationCallback mAnimationCallback;
    private AnimatedVectorDrawable mAnimatedVectorDrawablePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_on_phone_animation);

        mAnimationCallback = new AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                // Go back to main Dialogs screen after animation.
                finish();
            }
        };

        // Play 'swipe left' animation only once.
        ImageView phoneImage = findViewById(R.id.open_on_phone_animation_image);
        mAnimatedVectorDrawablePhone =
                (AnimatedVectorDrawable) phoneImage.getDrawable();
        mAnimatedVectorDrawablePhone.registerAnimationCallback(mAnimationCallback);
        mAnimatedVectorDrawablePhone.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnimatedVectorDrawablePhone.unregisterAnimationCallback(mAnimationCallback);
    }
}