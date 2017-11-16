package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProgressBarPreference extends Preference {

    public ProgressBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressBarPreference(Context context) {
        super(context);
    }


    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.progress_bar_with_text_layout, parent, false);
    }
}
