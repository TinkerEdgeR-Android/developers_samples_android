package com.example.android.wearable.wear.wearaccessibilityapp;

import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.ImageView;

public class ImagesActivity extends WearableActivity {
    private AnimatedVectorDrawable mAnimatedVectorDrawableSwipe;
    private AnimatedVectorDrawable mAnimatedVectorDrawableTap;
    private AnimationCallback mAnimationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        // Used to repeat animation from the beginning.
        mAnimationCallback = new AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                ((AnimatedVectorDrawable) drawable).start();
            }
        };

        // Play 'swipe left' animation on loop.
        ImageView mSwipeLeftImage = findViewById(R.id.swipe_left_image);
        mAnimatedVectorDrawableSwipe = (AnimatedVectorDrawable) mSwipeLeftImage.getDrawable();
        mAnimatedVectorDrawableSwipe.start();
        mAnimatedVectorDrawableSwipe.registerAnimationCallback(mAnimationCallback);

        // Play 'tap' animation on loop.
        ImageView mTapImage = findViewById(R.id.tap_image);
        mAnimatedVectorDrawableTap = (AnimatedVectorDrawable) mTapImage.getDrawable();
        mAnimatedVectorDrawableTap.start();
        mAnimatedVectorDrawableTap.registerAnimationCallback(mAnimationCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnimatedVectorDrawableSwipe.unregisterAnimationCallback(mAnimationCallback);
        mAnimatedVectorDrawableTap.unregisterAnimationCallback(mAnimationCallback);
    }
}