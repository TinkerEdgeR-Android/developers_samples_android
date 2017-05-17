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
package com.example.android.autofillframework.service.model;

import android.service.autofill.Dataset;
import android.view.autofill.AutofillId;

import org.json.JSONObject;

/**
 * Blueprint for Objects that are the underlying data model for Autofill Datasets.
 * Implementations should contain values that are meant to populate autofillable fields.
 */
public interface DatasetModel {

    /**
     * Returns the name of the {@link Dataset}.
     */
    String getDatasetName();

    /**
     * Sets the {@link Dataset} name.
     */
    void setDatasetName(String datasetName);

    /**
     * Populates a {@link Dataset.Builder} with appropriate values for each {@link AutofillId}
     * in a {@code AutofillFieldsCollection}.
     */
    void applyToFields(AutofillFieldsCollection autofillFieldsCollection,
            Dataset.Builder datasetBuilder);

    JSONObject toJson();
}
