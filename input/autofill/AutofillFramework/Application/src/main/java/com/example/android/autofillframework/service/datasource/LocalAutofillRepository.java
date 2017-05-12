package com.example.android.autofillframework.service.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

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
    private static final String LOGIN_CREDENTIAL_DATASETS_KEY = "loginCredentialDatasetNames";

    private static LocalAutofillRepository sInstance;

    private final SharedPreferences mPrefs;
    private int datasetNumber = 0;

    private LocalAutofillRepository(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREF_KEY,
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
    public void saveLoginCredential(LoginCredential loginCredential) {
        String datasetName = "dataset-" + datasetNumber++;
        loginCredential.setDatasetName(datasetName);
        Set<String> loginCredentialStringSet =
                mPrefs.getStringSet(LOGIN_CREDENTIAL_DATASETS_KEY, new ArraySet<String>());
        loginCredentialStringSet.add(loginCredential.toJson().toString());
        mPrefs.edit().putStringSet(LOGIN_CREDENTIAL_DATASETS_KEY, loginCredentialStringSet).apply();
    }
}