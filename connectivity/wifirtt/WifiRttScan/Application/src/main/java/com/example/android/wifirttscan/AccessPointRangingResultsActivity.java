/*
 * Copyright (C) 2018 Google Inc. All Rights Reserved.
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
package com.example.android.wifirttscan;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

/** Displays ranging information about a particular access point chosen by the user. */
public class AccessPointRangingResultsActivity extends AppCompatActivity {
    private static final String TAG = "APRRActivity";

    public static final String SCAN_RESULT_EXTRA =
            "com.example.android.wifirttscan.extra.SCAN_RESULT";

    private static final int STATISTIC_WINDOW_SIZE_DEFAULT = 50;
    private static final int RANGING_PERIOD_MILLISECONDS_DEFAULT = 1000;

    private ScanResult mScanResult;

    private TextView mSsidTextView;
    private TextView mBssidTextView;

    private TextView mRange;
    private TextView mRangeMean;
    private TextView mRangeSD;
    private TextView mRangeSDMean;
    private TextView mRssi;
    private TextView mSuccessesInBurst;
    private TextView mSuccessRatio;

    private EditText mStatsWindowSize;
    private EditText mRangingPeriod;

    private int mStatisticWindowSize;
    private int mRangingPeriodMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_point_ranging_results);

        mSsidTextView = findViewById(R.id.ssid);
        mBssidTextView = findViewById(R.id.bssid);

        mRange = findViewById(R.id.range_value);
        mRangeMean = findViewById(R.id.range_mean_value);
        mRangeSD = findViewById(R.id.range_sd_value);
        mRangeSDMean = findViewById(R.id.range_sd_mean_value);
        mRssi = findViewById(R.id.rssi_value);
        mSuccessesInBurst = findViewById(R.id.successes_in_burst_value);
        mSuccessRatio = findViewById(R.id.success_ratio_value);

        mStatsWindowSize = findViewById(R.id.stats_window_size_edit_value);
        mStatisticWindowSize = STATISTIC_WINDOW_SIZE_DEFAULT;
        mStatsWindowSize.setText(mStatisticWindowSize + "");

        mRangingPeriod = findViewById(R.id.ranging_period_edit_value);
        mRangingPeriodMilliseconds = RANGING_PERIOD_MILLISECONDS_DEFAULT;
        mRangingPeriod.setText(mRangingPeriodMilliseconds + "");

        Intent intent = getIntent();
        mScanResult = intent.getParcelableExtra(SCAN_RESULT_EXTRA);

        mSsidTextView.setText(mScanResult.SSID);
        mBssidTextView.setText(mScanResult.BSSID);

        // TODO (jewalker): Implement Ranging Request.
    }
}
