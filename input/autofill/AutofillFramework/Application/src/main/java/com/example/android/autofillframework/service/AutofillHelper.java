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
package com.example.android.autofillframework.service;

import android.content.Context;
import android.content.IntentSender;
import android.service.autofill.Dataset;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveInfo;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;

import com.example.android.autofillframework.R;
import com.example.android.autofillframework.service.model.AutofillField;
import com.example.android.autofillframework.service.model.LoginCredential;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.example.android.autofillframework.CommonUtil.TAG;

/**
 * This is a class containing helper methods for building Autofill Datasets and Responses.
 */
public final class AutofillHelper {

    /**
     * Wraps autofill data in a LoginCredential  Dataset object which can then be sent back to the
     * client View.
     */
    public static Dataset newCredentialDataset(Context context, List<AutofillField> autofillFields, LoginCredential loginCredential) {
        Dataset.Builder datasetBuilder = new Dataset.Builder
                (newRemoteViews(context.getPackageName(), loginCredential.getDatasetName()));
        // Not using enhanced for loop on Collection to prevent extra iterator allocation.
        for (int i = 0; i < autofillFields.size(); i++) {
            AutofillField field = autofillFields.get(i);
            boolean shouldBreak = false;
            for (String autofillHint : field.getHints()) {
                switch (autofillHint) {
                    case View.AUTOFILL_HINT_USERNAME:
                        datasetBuilder.setValue(field.getId(), AutofillValue.forText(loginCredential.getUsername()));
                        shouldBreak = true;
                        break;
                    case View.AUTOFILL_HINT_PASSWORD:
                        datasetBuilder.setValue(field.getId(), AutofillValue.forText(loginCredential.getPassword()));
                        shouldBreak = true;
                        break;
                }
                if (shouldBreak) {
                    break;
                }
            }
        }
        return datasetBuilder.build();
    }

    public static RemoteViews newRemoteViews(String packageName, String remoteViewsText) {
        RemoteViews presentation = new RemoteViews(packageName, R.layout.list_item);
        presentation.setTextViewText(R.id.text1, remoteViewsText);
        return presentation;
    }

    /**
     * Wraps autofill data in a Response object (essentially a series of Datasets) which can then
     * be sent back to the client View.
     */
    public static FillResponse newCredentialResponse(Context context, boolean datasetAuth,
            List<AutofillField> autofillFields, int saveType,
            HashMap<String, LoginCredential> loginCredentialMap) {
        FillResponse.Builder responseBuilder = new FillResponse.Builder();
        Set<String> datasetNames = loginCredentialMap.keySet();
        for (String datasetName : datasetNames) {
            LoginCredential loginCredential = loginCredentialMap.get(datasetName);
            if (datasetAuth) {
                Dataset.Builder datasetBuilder =
                        new Dataset.Builder(newRemoteViews(context.getPackageName(), loginCredential.getDatasetName()));
                IntentSender sender =
                        AuthActivity.getAuthIntentSenderForDataset(context, loginCredential.getDatasetName());
                datasetBuilder.setAuthentication(sender);
                responseBuilder.addDataset(datasetBuilder.build());
            } else {
                Dataset dataset = newCredentialDataset(context, autofillFields, loginCredential);
                responseBuilder.addDataset(dataset);
            }
        }
        if (saveType != 0) {
            AutofillId[] autofillIds = getAutofillIds(autofillFields);
            responseBuilder.setSaveInfo(new SaveInfo.Builder(saveType, autofillIds).build());
            return responseBuilder.build();
        } else {
            Log.d(TAG, "No Autofill data found.");
            return null;
        }
    }

    public static AutofillId[] getAutofillIds(List<AutofillField> autofillFields) {
        AutofillId[] autofillIds = new AutofillId[autofillFields.size()];
        for (int i = 0; i < autofillFields.size(); i++) {
            autofillIds[i] = autofillFields.get(i).getId();
        }
        return autofillIds;
    }
}
