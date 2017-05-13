package com.example.android.autofillframework.service.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import com.example.android.autofillframework.service.model.CreditCardInfo;
import com.example.android.autofillframework.service.model.DatasetModel;
import com.example.android.autofillframework.service.model.LoginCredential;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

/**
 * Singleton autofill data repository, that stores autofill fields to SharedPreferences.
 * DISCLAIMER, you should not store sensitive fields like user data unencrypted. This is only done
 * here for simplicity and learning purposes.
 */
public class LocalAutofillRepository implements AutofillRepository {
    private static final String SHARED_PREF_KEY = "com.example.android.autofillframework.service";
    private static final String LOGIN_CREDENTIAL_DATASETS_KEY = "loginCredentialDatasets";
    private static final String CREDIT_CARD_INFO_DATASETS_KEY = "creditCardInfoDatasets";
    private static final String DATASET_NUMBER_KEY = "datasetNumber";

    private static LocalAutofillRepository sInstance;

    private final SharedPreferences mPrefs;

    // TODO prepend with autofill data set in Settings.
    private LocalAutofillRepository(Context context) {
        mPrefs = context.getApplicationContext().getSharedPreferences(SHARED_PREF_KEY,
                Context.MODE_PRIVATE);
    }

    public static LocalAutofillRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocalAutofillRepository(context);
        }
        return sInstance;
    }

    @Override
    public HashMap<String, LoginCredential> getLoginCredentials() {
        try {
            HashMap<String, LoginCredential> loginCredentials = new HashMap<>();
            Set<String> loginCredentialStringSet =
                    mPrefs.getStringSet(LOGIN_CREDENTIAL_DATASETS_KEY, new ArraySet<String>());
            for (String loginCredentialString : loginCredentialStringSet) {
                LoginCredential loginCredential = LoginCredential
                        .fromJson(new JSONObject(loginCredentialString));
                if (loginCredential != null) {
                    loginCredentials.put(loginCredential.getDatasetName(), loginCredential);
                }
            }
            return loginCredentials;
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public HashMap<String, CreditCardInfo> getCreditCardInfo() {
        try {
            HashMap<String, CreditCardInfo> creditCardInfoMap = new HashMap<>();
            Set<String> creditCardInfoStringSet =
                    mPrefs.getStringSet(CREDIT_CARD_INFO_DATASETS_KEY, new ArraySet<String>());
            for (String creditCardInfoString : creditCardInfoStringSet) {
                CreditCardInfo creditCardInfo = CreditCardInfo
                        .fromJson(new JSONObject(creditCardInfoString));
                if (creditCardInfo != null) {
                    creditCardInfoMap.put(creditCardInfo.getDatasetName(), creditCardInfo);
                }
            }
            return creditCardInfoMap;
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public void saveLoginCredential(DatasetModel loginCredential) {
        saveDatasetModel(loginCredential, LOGIN_CREDENTIAL_DATASETS_KEY);
    }

    @Override
    public void saveCreditCardInfo(DatasetModel creditCardInfo) {
        saveDatasetModel(creditCardInfo, CREDIT_CARD_INFO_DATASETS_KEY);
    }

    private void saveDatasetModel(DatasetModel datasetModel, String key) {
        String datasetName = "dataset-" + getDatasetNumber();
        datasetModel.setDatasetName(datasetName);
        Set<String> datasetModelStringSet =
                mPrefs.getStringSet(key, new ArraySet<String>());
        datasetModelStringSet.add(datasetModel.toJson().toString());
        mPrefs.edit().putStringSet(key, datasetModelStringSet).apply();
        incrementDatasetNumber();
    }

    /**
     * For simplicity, datasets will be named in the form "dataset-X" where X means
     * this was the Xth dataset saved.
     */
    private int getDatasetNumber() {
        return mPrefs.getInt(DATASET_NUMBER_KEY, 0);
    }

    /**
     * Every time a dataset is saved, this should be called to increment the dataset number.
     * (only important for this service's dataset naming scheme).
     */
    private void incrementDatasetNumber() {
        mPrefs.edit().putInt(DATASET_NUMBER_KEY, getDatasetNumber() + 1).apply();
    }
}