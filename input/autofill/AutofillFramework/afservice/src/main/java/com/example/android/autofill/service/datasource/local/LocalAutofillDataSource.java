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

package com.example.android.autofill.service.datasource.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.autofill.service.datasource.AutofillDataSource;
import com.example.android.autofill.service.datasource.DataCallback;
import com.example.android.autofill.service.datasource.local.dao.AutofillDao;
import com.example.android.autofill.service.datasource.local.db.AutofillDatabase;
import com.example.android.autofill.service.model.AutofillDataset;
import com.example.android.autofill.service.model.FilledAutofillField;
import com.example.android.autofill.service.model.FilledAutofillFieldCollection;
import com.example.android.autofill.service.util.AppExecutors;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LocalAutofillDataSource implements AutofillDataSource {
    private static final String SHARED_PREF_KEY = "com.example.android.autofill"
            + ".service.datasource.LocalAutofillDataSource";
    private static final String DATASET_NUMBER_KEY = "datasetNumber";
    private static final Object sLock = new Object();

    private static LocalAutofillDataSource sInstance;

    private final AutofillDao mAutofillDao;
    private final SharedPreferences mSharedPreferences;
    private final AppExecutors mAppExecutors;

    private LocalAutofillDataSource(Context context, AppExecutors appExecutors) {
        mSharedPreferences = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        mAutofillDao = AutofillDatabase.getInstance(context).autofillDao();
        mAppExecutors = appExecutors;
    }

    public static LocalAutofillDataSource getInstance(Context context, AppExecutors appExecutors) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new LocalAutofillDataSource(context, appExecutors);
            }
            return sInstance;
        }
    }

    @Override
    public void getFilledAutofillFieldCollection(List<String> focusedAutofillHints,
            List<String> allAutofillHints,
            DataCallback<HashMap<String, FilledAutofillFieldCollection>> datasetsCallback) {
        mAppExecutors.diskIO().execute(() -> {
            List<AutofillDao.AutofillDatasetField> autofillDatasetFields =
                    mAutofillDao.getFilledAutofillFields(focusedAutofillHints, allAutofillHints);
            // Convert to hashmap; Room does not support TypeConverters for list.
            HashMap<String, FilledAutofillFieldCollection> map = new HashMap<>();
            for (AutofillDao.AutofillDatasetField autofillDatasetField : autofillDatasetFields) {
                String datasetName = autofillDatasetField.dataset.getDatasetName();
                if (!map.containsKey(datasetName)) {
                    map.put(datasetName, new FilledAutofillFieldCollection(autofillDatasetField.dataset));
                }
                map.get(datasetName).add(autofillDatasetField.filledAutofillField);
            }
            mAppExecutors.mainThread().execute(() ->
                    datasetsCallback.onLoaded(map)
            );
        });
    }

    @Override
    public void saveFilledAutofillFieldCollection(FilledAutofillFieldCollection
            filledAutofillFieldCollection) {
        mAppExecutors.diskIO().execute(() -> {
            Collection<FilledAutofillField> filledAutofillFields =
                    filledAutofillFieldCollection.getAllFields();
            AutofillDataset autofillDataset = filledAutofillFieldCollection.getDataset();
            mAutofillDao.saveAutofillDataset(autofillDataset);
            mAutofillDao.saveFilledAutofillFields(filledAutofillFields);
            incrementDatasetNumber();
        });
    }

    @Override
    public void clear() {
        mAppExecutors.diskIO().execute(() -> {
            mAutofillDao.clearAll();
            mSharedPreferences.edit().putInt(DATASET_NUMBER_KEY, 0).apply();
        });
    }

    /**
     * For simplicity, datasets will be named in the form "dataset-X" where X means
     * this was the Xth dataset saved.
     */
    public int getDatasetNumber() {
        return mSharedPreferences.getInt(DATASET_NUMBER_KEY, 0);
    }

    /**
     * Every time a dataset is saved, this should be called to increment the dataset number.
     * (only important for this service's dataset naming scheme).
     */
    private void incrementDatasetNumber() {
        mSharedPreferences.edit().putInt(DATASET_NUMBER_KEY, getDatasetNumber() + 1).apply();
    }
}
