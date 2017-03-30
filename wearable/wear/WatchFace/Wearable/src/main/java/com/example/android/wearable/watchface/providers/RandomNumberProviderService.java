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

package com.example.android.wearable.watchface.providers;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.complications.ComplicationManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ComplicationText;
import android.util.Log;

import java.util.Locale;

/**
 * Example Watch Face Complication data provider provides a random number on every update.
 */
public class RandomNumberProviderService extends ComplicationProviderService {

    private static final String TAG = "RandomNumberProvider";

    /*
     * Called when a complication has been activated. The method is for any one-time
     * (per complication) set-up.
     *
     * You can continue sending data for the active complicationId until onComplicationDeactivated()
     * is called.
     */
    @Override
    public void onComplicationActivated(
            int complicationId, int dataType, ComplicationManager complicationManager) {
        Log.d(TAG, "onComplicationActivated(): " + complicationId);
    }

    /*
     * Called when the complication needs updated data from your provider. There are four scenarios
     * when this will happen:
     *
     *   1. An active watch face complication is changed to use this provider
     *   2. A complication using this provider becomes active
     *   3. The period of time you specified in the manifest has elapsed (UPDATE_PERIOD_SECONDS)
     *   4. You triggered an update from your own class via the
     *       ProviderUpdateRequester.requestUpdate() method.
     */
    @Override
    public void onComplicationUpdate(
            int complicationId, int dataType, ComplicationManager complicationManager) {
        Log.d(TAG, "onComplicationUpdate() id: " + complicationId);


        // Retrieve your data, in this case, we simply create a random number to display.
        int randomNumber = (int) Math.floor(Math.random() * 10);

        String randomNumberText =
                String.format(Locale.getDefault(), "%d!", randomNumber);

        // Create Tap Action so that the user can trigger an update by tapping the complication.
        Intent updateIntent =
                new Intent(getApplicationContext(), UpdateComplicationDataService.class);
        updateIntent.setAction(UpdateComplicationDataService.ACTION_UPDATE_COMPLICATION);
        // We pass the complication id, so we can only update the specific complication tapped.
        updateIntent.putExtra(UpdateComplicationDataService.EXTRA_COMPLICATION_ID, complicationId);

        PendingIntent pendingIntent = PendingIntent.getService(
                getApplicationContext(),
                // Set the requestCode to the complication id. This ensures the system doesn't
                // combine other PendingIntents with the same context with this one (basically it
                // would then reuse the Extra you set in the initial PendingIntent). If you don't
                // do this and multiple complications with your data are active, every PendingIntent
                // assigned for tap, would use the same complication id (first one created).
                complicationId,
                updateIntent,
                0);

        ComplicationData complicationData = null;

        switch (dataType) {
            case ComplicationData.TYPE_RANGED_VALUE:
                complicationData = new ComplicationData.Builder(ComplicationData.TYPE_RANGED_VALUE)
                        .setValue(randomNumber)
                        .setMinValue(0)
                        .setMaxValue(10)
                        .setShortText(ComplicationText.plainText(randomNumberText))
                        .setTapAction(pendingIntent)
                        .build();
                break;
            case ComplicationData.TYPE_SHORT_TEXT:
                complicationData = new ComplicationData.Builder(ComplicationData.TYPE_SHORT_TEXT)
                        .setShortText(ComplicationText.plainText(randomNumberText))
                        .setTapAction(pendingIntent)
                        .build();
                break;
            case ComplicationData.TYPE_LONG_TEXT:
                complicationData = new ComplicationData.Builder(ComplicationData.TYPE_LONG_TEXT)
                        .setLongText(
                                ComplicationText.plainText("Random Number: " + randomNumberText))
                        .setTapAction(pendingIntent)
                        .build();
                break;
            default:
                if (Log.isLoggable(TAG, Log.WARN)) {
                    Log.w(TAG, "Unexpected complication type " + dataType);
                }
        }

        if (complicationData != null) {
            // In this case, we have the complication id and ask the system to update only that
            // complication.
            //
            // You could also call ProviderUpdateRequester.requestUpdateAll() instead, which
            // requests that the system call onComplicationUpdate on the specified provider for ALL
            // active complications using that provider without requiring the id (a bit easier than
            // tracking complication id throughout app).
            complicationManager.updateComplicationData(complicationId, complicationData);

        } else {
            // If no data is sent, we still need to inform the ComplicationManager, so the update
            // job can finish and the wake lock isn't held any longer than necessary.
            complicationManager.noUpdateRequired(complicationId);
        }
    }

    /*
     * Called when the complication has been deactivated.
     */
    @Override
    public void onComplicationDeactivated(int complicationId) {
        Log.d(TAG, "onComplicationDeactivated(): " + complicationId);
    }
}