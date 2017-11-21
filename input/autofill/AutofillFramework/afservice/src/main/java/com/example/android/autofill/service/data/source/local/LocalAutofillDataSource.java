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

package com.example.android.autofill.service.data.source.local;

import android.content.SharedPreferences;
import android.service.autofill.Dataset;

import com.example.android.autofill.service.AutofillHints;
import com.example.android.autofill.service.data.DataCallback;
import com.example.android.autofill.service.data.source.AutofillDataSource;
import com.example.android.autofill.service.data.source.local.dao.AutofillDao;
import com.example.android.autofill.service.model.AutofillDataset;
import com.example.android.autofill.service.model.DatasetWithFilledAutofillFields;
import com.example.android.autofill.service.model.FilledAutofillField;
import com.example.android.autofill.service.util.AppExecutors;

import java.util.List;

import static com.example.android.autofill.service.util.Util.logw;

public class LocalAutofillDataSource implements AutofillDataSource {
    public static final String SHARED_PREF_KEY = "com.example.android.autofill"
            + ".service.datasource.LocalAutofillDataSource";
    private static final String DATASET_NUMBER_KEY = "datasetNumber";
    private static final Object sLock = new Object();

    private static LocalAutofillDataSource sInstance;

    private final AutofillDao mAutofillDao;
    private final SharedPreferences mSharedPreferences;
    private final AppExecutors mAppExecutors;

    private LocalAutofillDataSource(SharedPreferences sharedPreferences, AutofillDao autofillDao,
            AppExecutors appExecutors) {
        mSharedPreferences = sharedPreferences;
        mAutofillDao = autofillDao;
        mAppExecutors = appExecutors;
    }

    public static LocalAutofillDataSource getInstance(SharedPreferences sharedPreferences,
            AutofillDao autofillDao, AppExecutors appExecutors) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new LocalAutofillDataSource(sharedPreferences, autofillDao,
                        appExecutors);
            }
            return sInstance;
        }
    }

    public static void clearInstance() {
        synchronized (sLock) {
            sInstance = null;
        }
    }

    @Override
    public void getAutofillDatasets(List<String> allAutofillHints,
            DataCallback<List<DatasetWithFilledAutofillFields>> datasetsCallback) {
        final List<String> storedAllAutofillHints =
                AutofillHints.convertToStoredHintNames(allAutofillHints);
        mAppExecutors.diskIO().execute(() -> {
            List<DatasetWithFilledAutofillFields> datasetsWithFilledAutofillFields = mAutofillDao
                    .getFilledAutofillFields(storedAllAutofillHints);
            mAppExecutors.mainThread().execute(() ->
                    datasetsCallback.onLoaded(datasetsWithFilledAutofillFields)
            );
        });
    }

    @Override
    public void getAutofillDataset(List<String> allAutofillHints, String datasetName,
            DataCallback<DatasetWithFilledAutofillFields> datasetsCallback) {
        mAppExecutors.diskIO().execute(() -> {
            // Room does not support TypeConverters for collections.
            List<DatasetWithFilledAutofillFields> autofillDatasetFields =
                    mAutofillDao.getFilledAutofillFieldsWithName(allAutofillHints, datasetName);
            if (autofillDatasetFields != null && !autofillDatasetFields.isEmpty()) {
                if (autofillDatasetFields.size() > 1) {
                    logw("More than 1 dataset with name %s", datasetName);
                }
                DatasetWithFilledAutofillFields dataset = autofillDatasetFields.get(0);

                mAppExecutors.mainThread().execute(() ->
                        datasetsCallback.onLoaded(dataset)
                );
            } else {
                datasetsCallback.onDataNotAvailable("No data found.");
            }
        });
    }


    @Override
    public void saveAutofillDatasets(List<DatasetWithFilledAutofillFields>
            datasetsWithFilledAutofillFields) {
        mAppExecutors.diskIO().execute(() -> {
            for (DatasetWithFilledAutofillFields datasetWithFilledAutofillFields :
                    datasetsWithFilledAutofillFields) {
                List<FilledAutofillField> filledAutofillFields =
                        datasetWithFilledAutofillFields.filledAutofillFields;
                AutofillDataset autofillDataset = datasetWithFilledAutofillFields.autofillDataset;
                mAutofillDao.saveAutofillDataset(autofillDataset);
                mAutofillDao.saveFilledAutofillFields(filledAutofillFields);
            }
        });
        incrementDatasetNumber();
    }

    @Override
    public void clear() {
        mAppExecutors.diskIO().execute(() -> {
            mAutofillDao.clearAll();
            mSharedPreferences.edit().putInt(DATASET_NUMBER_KEY, 0).apply();
        });
    }

    /**
     * For simplicity, {@link Dataset}s will be named in the form {@code dataset-X.P} where
     * {@code X} means this was the Xth group of datasets saved, and {@code P} refers to the dataset
     * partition number. This method returns the appropriate {@code X}.
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
