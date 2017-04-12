/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.wearable.watchface.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.complications.ComplicationProviderInfo;
import android.support.wearable.complications.ProviderChooserIntent;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;

import com.example.android.wearable.watchface.R;
import com.example.android.wearable.watchface.models.ComplicationsSimpleWatchFaceSettingsConfigData;
import com.example.android.wearable.watchface.watchfaces.ComplicationSimpleWatchFaceService;

/**
 * The watch-side config activity for {@link ComplicationSimpleWatchFaceService}, which
 * allows for setting the left and right complications of watch face along with the marker (watch
 * face arms, ticks) color, unread notifications toggle, and background complication.
 */
public class ComplicationSimpleConfigActivity extends Activity {

    private static final String TAG = ComplicationSimpleConfigActivity.class.getSimpleName();

    static final int COMPLICATION_CONFIG_REQUEST_CODE = 888;

    private WearableRecyclerView mConfigWearableRecyclerView;

    private ComplicationSimpleRecyclerViewAdapter mWearableRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complication_simple_config);

        mWearableRecyclerViewAdapter =
                new ComplicationSimpleRecyclerViewAdapter(
                        ComplicationsSimpleWatchFaceSettingsConfigData.getSettingsData());

        mConfigWearableRecyclerView =
                (WearableRecyclerView) findViewById(R.id.wearable_recycler_view);

        // Aligns the first and last items on the list vertically centered on the screen.
        mConfigWearableRecyclerView.setCenterEdgeItems(true);

        // Improves performance because we know changes in content do not change the layout size of
        // the RecyclerView.
        mConfigWearableRecyclerView.setHasFixedSize(true);

        mConfigWearableRecyclerView.setAdapter(mWearableRecyclerViewAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPLICATION_CONFIG_REQUEST_CODE
                && resultCode == RESULT_OK) {

            // Retrieves information for selected Complication provider.
            ComplicationProviderInfo complicationProviderInfo =
                    data.getParcelableExtra(ProviderChooserIntent.EXTRA_PROVIDER_INFO);

            Log.d(TAG, "Selected Provider: " + complicationProviderInfo);
        }
    }
}