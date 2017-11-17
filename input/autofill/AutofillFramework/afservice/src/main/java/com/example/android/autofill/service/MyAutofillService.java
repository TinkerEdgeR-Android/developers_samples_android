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
package com.example.android.autofill.service;

import android.app.assist.AssistStructure;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveRequest;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.autofill.AutofillId;
import android.widget.RemoteViews;

import com.example.android.autofill.service.datasource.DataCallback;
import com.example.android.autofill.service.datasource.PackageVerificationDataSource;
import com.example.android.autofill.service.datasource.local.LocalAutofillDataSource;
import com.example.android.autofill.service.datasource.local.DigitalAssetLinksRepository;
import com.example.android.autofill.service.datasource.local.SharedPrefsPackageVerificationRepository;
import com.example.android.autofill.service.model.FilledAutofillFieldCollection;
import com.example.android.autofill.service.settings.MyPreferences;
import com.example.android.autofill.service.util.AppExecutors;
import com.example.android.autofill.service.util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.android.autofill.service.AutofillHelper.CLIENT_STATE_PARTIAL_ID_TEMPLATE;
import static com.example.android.autofill.service.util.Util.AUTOFILL_ID_FILTER;
import static com.example.android.autofill.service.util.Util.bundleToString;
import static com.example.android.autofill.service.util.Util.dumpStructure;
import static com.example.android.autofill.service.util.Util.findNodeByFilter;
import static com.example.android.autofill.service.util.Util.logVerboseEnabled;
import static com.example.android.autofill.service.util.Util.logd;
import static com.example.android.autofill.service.util.Util.logv;
import static com.example.android.autofill.service.util.Util.logw;

public class MyAutofillService extends AutofillService {

    private LocalAutofillDataSource mLocalAutofillDataSource;
    private DigitalAssetLinksRepository mDalRepository;
    private PackageVerificationDataSource mPackageVerificationRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        Util.setLoggingLevel(MyPreferences.getInstance(this).getLoggingLevel());
        mLocalAutofillDataSource = LocalAutofillDataSource.getInstance(this, new AppExecutors());
        mDalRepository = DigitalAssetLinksRepository.getInstance(this);
        mPackageVerificationRepository = SharedPrefsPackageVerificationRepository.getInstance(this);
    }

    @Override
    public void onFillRequest(@NonNull FillRequest request,
            @NonNull CancellationSignal cancellationSignal,
            @NonNull FillCallback callback) {
        AssistStructure structure = request.getFillContexts()
                .get(request.getFillContexts().size() - 1).getStructure();
        String packageName = structure.getActivityComponent().getPackageName();
        if (!mPackageVerificationRepository.putPackageSignatures(packageName)) {
            callback.onFailure(
                    getApplicationContext().getString(R.string.invalid_package_signature));
            return;
        }
        final Bundle clientState = request.getClientState();
        if (logVerboseEnabled()) {
            logv("onFillRequest(): clientState=%s", bundleToString(clientState));
        }
        dumpStructure(structure);

        cancellationSignal.setOnCancelListener(() ->
                logw("Cancel autofill not implemented in this sample.")
        );
        // Parse AutoFill data in Activity
        StructureParser parser = new StructureParser(getApplicationContext(), structure,
                mLocalAutofillDataSource, mDalRepository);
        // TODO: try / catch on other places (onSave, auth activity, etc...)
        try {
            parser.parseForFill();
        } catch (SecurityException e) {
            // TODO: handle cases where DAL didn't pass by showing a custom UI asking the user
            // to confirm the mapping. Might require subclassing SecurityException.
            logw(e, "Security exception handling %s", request);
            callback.onFailure(e.getMessage());
            return;
        }
        AutofillFieldMetadataCollection autofillFields = parser.getAutofillFields();
        FillResponse.Builder responseBuilder = new FillResponse.Builder();
        // Check user's settings for authenticating Responses and Datasets.
        boolean responseAuth = MyPreferences.getInstance(this).isResponseAuth();
        AutofillId[] autofillIds = autofillFields.getAutofillIds();
        if (responseAuth && !Arrays.asList(autofillIds).isEmpty()) {
            // If the entire Autofill Response is authenticated, AuthActivity is used
            // to generate Response.
            IntentSender sender = AuthActivity.getAuthIntentSenderForResponse(this);
            RemoteViews presentation = AutofillHelper
                    .newRemoteViews(getPackageName(), getString(R.string.autofill_sign_in_prompt),
                            R.drawable.ic_lock_black_24dp);
            responseBuilder
                    .setAuthentication(autofillIds, sender, presentation);
            callback.onSuccess(responseBuilder.build());
        } else {
            boolean datasetAuth = MyPreferences.getInstance(this).isDatasetAuth();
            mLocalAutofillDataSource.getFilledAutofillFieldCollection(
                    autofillFields.getFocusedHints(), autofillFields.getAllHints(),
                    new DataCallback<HashMap<String, FilledAutofillFieldCollection>>() {
                        @Override
                        public void onLoaded(HashMap<String, FilledAutofillFieldCollection>
                                clientFormDataMap) {
                            FillResponse response = AutofillHelper.newResponse
                                    (MyAutofillService.this, clientState, datasetAuth,
                                            autofillFields, clientFormDataMap);
                            callback.onSuccess(response);
                        }

                        @Override
                        public void onDataNotAvailable(String msg, Object... params) {
                            logw(msg, params);
                        }
                    });
        }
    }

    @Override
    public void onSaveRequest(@NonNull SaveRequest request, @NonNull SaveCallback callback) {
        List<FillContext> fillContexts = request.getFillContexts();
        int size = fillContexts.size();
        AssistStructure structure = fillContexts.get(size - 1).getStructure();
        String packageName = structure.getActivityComponent().getPackageName();
        if (!mPackageVerificationRepository.putPackageSignatures(packageName)) {
            callback.onFailure(getApplicationContext().getString(R.string.invalid_package_signature));
            return;
        }
        Bundle clientState = request.getClientState();
        if (logVerboseEnabled()) {
            logv("onSaveRequest(): clientState=%s", bundleToString(clientState));
        }
        dumpStructure(structure);

        // TODO: hardcode check for partial username
        if (clientState != null) {
            String usernameKey =
                    String.format(CLIENT_STATE_PARTIAL_ID_TEMPLATE, View.AUTOFILL_HINT_USERNAME);
            AutofillId usernameId = clientState.getParcelable(usernameKey);
            logd("client state for %s: %s", usernameKey, usernameId);
            if (usernameId != null) {
                String passwordKey =
                        String.format(CLIENT_STATE_PARTIAL_ID_TEMPLATE, View.AUTOFILL_HINT_PASSWORD);
                AutofillId passwordId = clientState.getParcelable(passwordKey);

                logd("Scanning %d contexts for username ID %s and password ID %s.", size,
                        usernameId, passwordId);
                AssistStructure.ViewNode usernameNode =
                        findNodeByFilter(fillContexts, usernameId, AUTOFILL_ID_FILTER);
                AssistStructure.ViewNode passwordNode =
                        findNodeByFilter(fillContexts, passwordId, AUTOFILL_ID_FILTER);
                String username = null, password = null;
                if (usernameNode != null) {
                    username = usernameNode.getAutofillValue().getTextValue().toString();
                }
                if (passwordNode != null) {
                    password = passwordNode.getAutofillValue().getTextValue().toString();
                }

                if (username != null && password != null) {
                    logd("user: %s, pass: %s", username, password);
                    // TODO: save it
                    callback.onFailure("TODO: save " + username + "/" + password);
                    return;
                } else {
                    logw(" missing user (%s) or pass (%s)", username, password);
                }
            }
        }

        StructureParser parser = new StructureParser(getApplicationContext(), structure,
                mLocalAutofillDataSource, mDalRepository);
        parser.parseForSave();
        FilledAutofillFieldCollection filledAutofillFieldCollection = parser.getClientFormData();
        mLocalAutofillDataSource.saveFilledAutofillFieldCollection(filledAutofillFieldCollection);
    }

    @Override
    public void onConnected() {
        logd("onConnected");
    }

    @Override
    public void onDisconnected() {
        logd("onDisconnected");
    }
}
