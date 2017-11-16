package com.example.android.wearable.wear.wearaccessibilityapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AccessibilityActivity extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility);

        ImageView accessibilityImage = findViewById(R.id.icon_image_view);
        accessibilityImage.setImageDrawable(getDrawable(R.drawable.settings_circle));

        TextView accessibilityText = findViewById(R.id.icon_text_view);
        accessibilityText.setText(R.string.accessibility_settings);

        findViewById(R.id.accessibility_button_include).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
            }
        });
    }
}