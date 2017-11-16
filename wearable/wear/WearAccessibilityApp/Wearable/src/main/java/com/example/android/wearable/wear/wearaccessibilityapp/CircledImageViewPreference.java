package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Context;
import android.os.CountDownTimer;
import android.preference.Preference;
import android.support.wearable.view.CircledImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CircledImageViewPreference extends Preference {

    private CircledImageView mCircledImage;
    private TextView mCircledImageText;
    private CountDownTimer mCountDownTimer;
    private int mColorPrimaryDark;
    private int mColorAccent;
    private boolean mIsLoading;
    private Context mContext;

    public CircledImageViewPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.colorPrimaryDark, typedValue, true);
        mColorPrimaryDark = typedValue.data;
        mContext.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        mColorAccent = typedValue.data;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View circledImageViewLayout = inflater.inflate(R.layout.circled_image_layout, null);

        mCircledImage = circledImageViewLayout.findViewById(R.id.circled_image_view);
        mCircledImageText = circledImageViewLayout.findViewById(R.id.circled_image_text);

        setStartCircledImageView();
        return circledImageViewLayout;
    }

    @Override
    protected void onClick() {
        mIsLoading = !mIsLoading;
        if (mIsLoading) {
            setLoadingCircledImageView();
        } else {
            setStartCircledImageView();
            cancelCountDownTimer();
        }
    }

    public void setStartCircledImageView() {
        mCircledImageText.setText(R.string.start);
        mCircledImage.setImageResource(R.drawable.start);
        mCircledImage.setCircleBorderColor(mColorPrimaryDark);
        mCircledImage.setProgress(1);
        mIsLoading = false;
    }

    public void setLoadingCircledImageView() {
        mCircledImageText.setText(R.string.loading);
        mCircledImage.setImageResource(R.drawable.stop);
        mCircledImage.setCircleBorderColor(mColorAccent);
        mCountDownTimer = new CountDownTimer(10000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                float timeElapsed = (10000.0f - millisUntilFinished) / 10000.0f;
                mCircledImage.setProgress(timeElapsed);
            }

            @Override
            public void onFinish() {
                setStartCircledImageView();
            }
        }.start();
    }

    public void cancelCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}