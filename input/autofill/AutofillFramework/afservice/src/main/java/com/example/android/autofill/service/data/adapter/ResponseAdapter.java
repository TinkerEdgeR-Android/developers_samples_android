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

package com.example.android.autofill.service.data.adapter;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveInfo;
import android.view.autofill.AutofillId;
import android.widget.RemoteViews;

import com.example.android.autofill.service.AuthActivity;
import com.example.android.autofill.service.RemoteViewsHelper;
import com.example.android.autofill.service.data.ClientViewMetadata;
import com.example.android.autofill.service.model.DatasetWithFilledAutofillFields;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.autofill.service.util.Util.logd;

public class ResponseAdapter {
    public static final String CLIENT_STATE_PARTIAL_ID_TEMPLATE = "partial-%s";
    // TODO: move to settings activity and document it
    private static final boolean SUPPORT_MULTIPLE_STEPS = true;
    static int pageno = 0;
    private final Context mContext;
    private final DatasetAdapter mDatasetAdapter;
    private final String mPackageName;
    private final ClientViewMetadata mClientViewMetadata;
    private final List<ClientViewMetadata> mPreviousClientViewMetadatas;

    public ResponseAdapter(Context context, ClientViewMetadata clientViewMetadata,
            String packageName, DatasetAdapter datasetAdapter, Bundle clientState) {
        mContext = context;
        mClientViewMetadata = clientViewMetadata;
        mDatasetAdapter = datasetAdapter;
        mPackageName = packageName;
        mPreviousClientViewMetadatas = new ArrayList<>();
        if (clientState != null) {
            clientState.setClassLoader(getClass().getClassLoader());
            for (int i = pageno - 1; i >= 0; i--) {
                ClientViewMetadata previousPage = clientState.getParcelable("client-" + (pageno - 1));
                mPreviousClientViewMetadatas.add(previousPage);
            }
            logd("previous client state == " + mPreviousClientViewMetadatas);
        }
    }

    /**
     * Wraps autofill data in a Response object (essentially a series of Datasets) which can then
     * be sent back to the client View.
     */
    public FillResponse buildResponse(List<DatasetWithFilledAutofillFields> datasets,
            boolean datasetAuth) {
        FillResponse.Builder responseBuilder = new FillResponse.Builder();
        if (datasets != null) {
            for (DatasetWithFilledAutofillFields datasetWithFilledAutofillFields : datasets) {
                if (datasetWithFilledAutofillFields != null) {
                    Dataset dataset;
                    String datasetName = datasetWithFilledAutofillFields.autofillDataset
                            .getDatasetName();
                    if (datasetAuth) {
                        IntentSender intentSender = AuthActivity.getAuthIntentSenderForDataset(
                                mContext, datasetName);
                        RemoteViews remoteViews = RemoteViewsHelper.viewsWithAuth(
                                mPackageName, datasetName);
                        dataset = mDatasetAdapter.buildDataset(datasetWithFilledAutofillFields,
                                remoteViews, intentSender);
                    } else {
                        RemoteViews remoteViews = RemoteViewsHelper.viewsWithNoAuth(
                                mPackageName, datasetName);
                        dataset = mDatasetAdapter.buildDataset(datasetWithFilledAutofillFields,
                                remoteViews);
                    }
                    if (dataset != null) {
                        responseBuilder.addDataset(dataset);
                    }
                }
            }
        }
        Bundle clientState = new Bundle();
        clientState.putParcelable("client-" + (pageno++), mClientViewMetadata);
        int saveType = mClientViewMetadata.getSaveType();
        AutofillId[] autofillIds = mClientViewMetadata.getAutofillIds();
        SaveInfo saveInfo = new SaveInfo.Builder(saveType, autofillIds).build();
        responseBuilder.setSaveInfo(saveInfo);
        responseBuilder.setClientState(clientState);
        return responseBuilder.build();
//        int saveType = mClientViewMetadata.getSaveType();
//        AutofillId[] autofillIds = mClientViewMetadata.getAutofillIds();
//        List<String> allHints = mClientViewMetadata.getAllHints();
//        if (logDebugEnabled()) {
//            logd("setPartialSaveInfo() for type %s: allHints=%s, ids=%s, clientState=%s",
//                    getSaveTypeAsString(saveType), allHints, Arrays.toString(autofillIds),
//                    bundleToString(mPreviousClientState));
//        }
//        // TODO: this should be more generic, but for now it's hardcode to support just activities
//        // that have an username and a password in separate steps (like MultipleStepsSigninActivity)
//        if ((saveType != SaveInfo.SAVE_DATA_TYPE_USERNAME
//                && saveType != SaveInfo.SAVE_DATA_TYPE_PASSWORD)
//                || autofillIds.length != 1 || allHints.size() != 1) {
//            logd("Unsupported activity for partial info; returning full");
//            responseBuilder.setSaveInfo(mClientViewMetadata.getSaveInfo());
//            return responseBuilder.build();
//        }
//        int previousSaveType;
//        String previousHint;
//        if (saveType == SaveInfo.SAVE_DATA_TYPE_PASSWORD) {
//            previousHint = View.AUTOFILL_HINT_USERNAME;
//            previousSaveType = SaveInfo.SAVE_DATA_TYPE_USERNAME;
//        } else {
//            previousHint = View.AUTOFILL_HINT_PASSWORD;
//            previousSaveType = SaveInfo.SAVE_DATA_TYPE_PASSWORD;
//        }
//        String previousKey = String.format(CLIENT_STATE_PARTIAL_ID_TEMPLATE, previousHint);
//
//        AutofillId previousValue = mPreviousClientState == null ? null : mPreviousClientState
//                .getParcelable(previousKey);
//        logd("previous: %s=%s", previousKey, previousValue);
//
//        Bundle newClientState = new Bundle();
//        String key = String.format(CLIENT_STATE_PARTIAL_ID_TEMPLATE, allHints.get(0));
//        AutofillId value = autofillIds[0];
//        logd("New client state: %s = %s", key, value);
//        newClientState.putParcelable(key, value);
//
//        if (previousValue != null) {
//            AutofillId[] newIds = new AutofillId[]{previousValue, value};
//            int newSaveType = saveType | previousSaveType;
//            logd("new values: type=%s, ids=%s",
//                    getSaveTypeAsString(newSaveType), Arrays.toString(newIds));
//            newClientState.putAll(mPreviousClientState);
//            responseBuilder.setSaveInfo(new SaveInfo.Builder(newSaveType, newIds)
//                    .setFlags(SaveInfo.FLAG_SAVE_ON_ALL_VIEWS_INVISIBLE)
//                    .build())
//                    .setClientState(newClientState);
//
//            return responseBuilder.build();
//        }
//        responseBuilder.setClientState(newClientState);
//        responseBuilder.setSaveInfo(mClientViewMetadata.getSaveInfo());
//        return responseBuilder.build();
    }

    public FillResponse buildResponse(IntentSender sender, RemoteViews remoteViews) {
        FillResponse.Builder responseBuilder = new FillResponse.Builder();
        int saveType = mClientViewMetadata.getSaveType();
        AutofillId[] autofillIds = mClientViewMetadata.getAutofillIds();
        SaveInfo saveInfo = new SaveInfo.Builder(saveType, autofillIds).build();
        responseBuilder.setSaveInfo(saveInfo);
        responseBuilder.setAuthentication(autofillIds, sender, remoteViews);
        return responseBuilder.build();
    }
}
