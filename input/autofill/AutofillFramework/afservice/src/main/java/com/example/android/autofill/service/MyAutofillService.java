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
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
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

import com.example.android.autofill.service.data.AutofillDataBuilder;
import com.example.android.autofill.service.data.ClientAutofillDataBuilder;
import com.example.android.autofill.service.data.ClientViewMetadata;
import com.example.android.autofill.service.data.ClientViewMetadataBuilder;
import com.example.android.autofill.service.data.DataCallback;
import com.example.android.autofill.service.data.adapter.DatasetAdapter;
import com.example.android.autofill.service.data.adapter.ResponseAdapter;
import com.example.android.autofill.service.data.source.PackageVerificationDataSource;
import com.example.android.autofill.service.data.source.local.DigitalAssetLinksRepository;
import com.example.android.autofill.service.data.source.local.LocalAutofillDataSource;
import com.example.android.autofill.service.data.source.local.SharedPrefsPackageVerificationRepository;
import com.example.android.autofill.service.data.source.local.dao.AutofillDao;
import com.example.android.autofill.service.data.source.local.db.AutofillDatabase;
import com.example.android.autofill.service.model.DalCheck;
import com.example.android.autofill.service.model.DalInfo;
import com.example.android.autofill.service.model.DatasetWithFilledAutofillFields;
import com.example.android.autofill.service.settings.MyPreferences;
import com.example.android.autofill.service.util.AppExecutors;
import com.example.android.autofill.service.util.Util;
import static com.example.android.autofill.service.util.Util.DalCheckRequirement;
import java.util.List;

import static com.example.android.autofill.service.data.adapter.ResponseAdapter.CLIENT_STATE_PARTIAL_ID_TEMPLATE;
import static com.example.android.autofill.service.util.Util.AUTOFILL_ID_FILTER;
import static com.example.android.autofill.service.util.Util.bundleToString;
import static com.example.android.autofill.service.util.Util.dumpStructure;
import static com.example.android.autofill.service.util.Util.findNodeByFilter;
import static com.example.android.autofill.service.util.Util.logVerboseEnabled;
import static com.example.android.autofill.service.util.Util.logd;
import static com.example.android.autofill.service.util.Util.loge;
import static com.example.android.autofill.service.util.Util.logv;
import static com.example.android.autofill.service.util.Util.logw;

public class MyAutofillService extends AutofillService {

    private LocalAutofillDataSource mLocalAutofillDataSource;
    private DigitalAssetLinksRepository mDalRepository;
    private PackageVerificationDataSource mPackageVerificationRepository;
    private AutofillDataBuilder mAutofillDataBuilder;
    private DatasetAdapter mDatasetAdapter;
    private ResponseAdapter mResponseAdapter;
    private ClientViewMetadata mClientViewMetadata;

    @Override
    public void onCreate() {
        super.onCreate();
        Util.setLoggingLevel(MyPreferences.getInstance(this).getLoggingLevel());
        SharedPreferences localAfDataSourceSharedPrefs =
                getSharedPreferences(LocalAutofillDataSource.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        AutofillDao autofillDao = AutofillDatabase.getInstance(this).autofillDao();
        mLocalAutofillDataSource = LocalAutofillDataSource.getInstance(localAfDataSourceSharedPrefs,
                autofillDao, new AppExecutors());
        mDalRepository = DigitalAssetLinksRepository.getInstance(getPackageManager());
        mPackageVerificationRepository = SharedPrefsPackageVerificationRepository.getInstance(this);
    }

    @Override
    public void onFillRequest(@NonNull FillRequest request,
            @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback callback) {
        AssistStructure structure = request.getFillContexts()
                .get(request.getFillContexts().size() - 1).getStructure();
        StructureParser parser = new StructureParser(structure);
        mDatasetAdapter = new DatasetAdapter(parser);
        ClientViewMetadataBuilder clientViewMetadataBuilder = new ClientViewMetadataBuilder(parser);
        mClientViewMetadata = clientViewMetadataBuilder.buildClientViewMetadata();
        mResponseAdapter = new ResponseAdapter(this, mClientViewMetadata,
                getPackageName(), mDatasetAdapter, request.getClientState());
        String packageName = structure.getActivityComponent().getPackageName();
        if (!mPackageVerificationRepository.putPackageSignatures(packageName)) {
            callback.onFailure(getString(R.string.invalid_package_signature));
            return;
        }
        final Bundle clientState = request.getClientState();
        if (logVerboseEnabled()) {
            logv("onFillRequest(): clientState=%s", bundleToString(clientState));
            dumpStructure(structure);
        }
        cancellationSignal.setOnCancelListener(() ->
                logw("Cancel autofill not implemented in this sample.")
        );
        // Check user's settings for authenticating Responses and Datasets.
        boolean responseAuth = MyPreferences.getInstance(this).isResponseAuth();
        if (responseAuth) {
            // If the entire Autofill Response is authenticated, AuthActivity is used
            // to generate Response.
            IntentSender sender = AuthActivity.getAuthIntentSenderForResponse(this);
            RemoteViews remoteViews = RemoteViewsHelper.viewsWithAuth(getPackageName(),
                    getString(R.string.autofill_sign_in_prompt));
            FillResponse response = mResponseAdapter.buildResponse(sender, remoteViews);
            if (response != null) {
                callback.onSuccess(response);
            }
        } else {
            boolean datasetAuth = MyPreferences.getInstance(this).isDatasetAuth();
            mLocalAutofillDataSource.getAutofillDatasets(mClientViewMetadata.getAllHints(),
                    new DataCallback<List<DatasetWithFilledAutofillFields>>() {
                        @Override
                        public void onLoaded(List<DatasetWithFilledAutofillFields> datasets) {
                            FillResponse response = mResponseAdapter.buildResponse(datasets,
                                    datasetAuth);
                            callback.onSuccess(response);
                        }

                        @Override
                        public void onDataNotAvailable(String msg, Object... params) {
                            logw(msg, params);
                            callback.onFailure(String.format(msg, params));
                        }
                    });
        }
    }

    @Override
    public void onSaveRequest(@NonNull SaveRequest request, @NonNull SaveCallback callback) {
        List<FillContext> fillContexts = request.getFillContexts();
        int size = fillContexts.size();
        AssistStructure structure = fillContexts.get(size - 1).getStructure();
        StructureParser parser = new StructureParser(structure);
        mAutofillDataBuilder = new ClientAutofillDataBuilder(parser);
        ClientViewMetadataBuilder clientViewMetadataBuilder = new ClientViewMetadataBuilder(parser);
        mClientViewMetadata = clientViewMetadataBuilder.buildClientViewMetadata();
        String packageName = structure.getActivityComponent().getPackageName();
        if (!mPackageVerificationRepository.putPackageSignatures(packageName)) {
            callback.onFailure(getString(R.string.invalid_package_signature));
            return;
        }
        Bundle clientState = request.getClientState();
        if (logVerboseEnabled()) {
            logv("onSaveRequest(): clientState=%s", bundleToString(clientState));
        }
        dumpStructure(structure);

        // TODO: hardcode check for partial username
        if (clientState != null) {
            String usernameKey = String.format(CLIENT_STATE_PARTIAL_ID_TEMPLATE,
                    View.AUTOFILL_HINT_USERNAME);
            AutofillId usernameId = clientState.getParcelable(usernameKey);
            logd("client state for %s: %s", usernameKey, usernameId);
            if (usernameId != null) {
                String passwordKey = String.format(CLIENT_STATE_PARTIAL_ID_TEMPLATE,
                        View.AUTOFILL_HINT_PASSWORD);
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
        checkWebDomainAndBuildAutofillData(packageName, callback);
    }

    private void checkWebDomainAndBuildAutofillData(String packageName, SaveCallback callback) {
        String webDomain;
        try {
            webDomain = mClientViewMetadata.getWebDomain();
        } catch(SecurityException e) {
            logw(e.getMessage());
            callback.onFailure(getString(R.string.security_exception));
            return;
        }
        if (webDomain != null && webDomain.length() > 0) {
            DalCheckRequirement req = MyPreferences.getInstance(this).getDalCheckRequirement();
            mDalRepository.checkValid(req, new DalInfo(webDomain, packageName),
                    new DataCallback<DalCheck>() {
                        @Override
                        public void onLoaded(DalCheck dalCheck) {
                            if (dalCheck.linked) {
                                logd("Domain %s is valid for %s", webDomain, packageName);
                                buildAndSaveAutofillData();
                            } else {
                                loge("Could not associate web domain %s with app %s",
                                        webDomain, packageName);
                                callback.onFailure(getString(R.string.dal_exception));
                            }
                        }

                        @Override
                        public void onDataNotAvailable(String msg, Object... params) {
                            logw(msg, params);
                            callback.onFailure(getString(R.string.dal_exception));
                        }
                    });
        } else {
            logd("no web domain");
            buildAndSaveAutofillData();
        }
    }

    private void buildAndSaveAutofillData() {
        int datasetNumber = mLocalAutofillDataSource.getDatasetNumber();
        List<DatasetWithFilledAutofillFields> datasetsWithFilledAutofillFields =
                mAutofillDataBuilder.buildDatasetsByPartition(datasetNumber);
        mLocalAutofillDataSource.saveAutofillDatasets(datasetsWithFilledAutofillFields);
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
