package com.example.android.wearable.wear.wearaccessibilityapp;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class RadioListActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_list);

        TextView titleView = findViewById(R.id.radio_list_title);
        titleView.setText(R.string.radio_list);
    }
}