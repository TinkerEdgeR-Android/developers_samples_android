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
import android.view.autofill.AutofillId;
import android.widget.RemoteViews;

import com.example.android.autofillframework.R;
import com.example.android.autofillframework.service.model.AutofillFieldsCollection;
import com.example.android.autofillframework.service.model.DatasetModel;

import java.util.HashMap;
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
    public static Dataset newDataset(Context context,
            AutofillFieldsCollection autofillFields, DatasetModel datasetModel) {
        Dataset.Builder datasetBuilder = new Dataset.Builder
                (newRemoteViews(context.getPackageName(), datasetModel.getDatasetName()));
        datasetModel.applyToFields(autofillFields, datasetBuilder);
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
    public static <T extends DatasetModel> FillResponse newResponse(Context context,
            boolean datasetAuth, AutofillFieldsCollection autofillFields, int saveType,
            HashMap<String, T> datasetModelMap) {
        FillResponse.Builder responseBuilder = new FillResponse.Builder();
        Set<String> datasetNames = datasetModelMap.keySet();
        for (String datasetName : datasetNames) {
            T datasetModel = datasetModelMap.get(datasetName);
            if (datasetAuth) {
                Dataset.Builder datasetBuilder =
                        new Dataset.Builder(newRemoteViews
                                (context.getPackageName(), datasetModel.getDatasetName()));
                IntentSender sender = AuthActivity
                        .getAuthIntentSenderForDataset(context, datasetModel.getDatasetName());
                datasetBuilder.setAuthentication(sender);
                responseBuilder.addDataset(datasetBuilder.build());
            } else {
                Dataset dataset = newDataset(context, autofillFields, datasetModel);
                responseBuilder.addDataset(dataset);
            }
        }
        if (saveType != 0) {
            AutofillId[] autofillIds = autofillFields.getAutofillIds();
            responseBuilder.setSaveInfo(new SaveInfo.Builder(saveType, autofillIds).build());
            return responseBuilder.build();
        } else {
            Log.d(TAG, "These fields are not meant to be saved by autofill.");
            return null;
        }
    }
}
