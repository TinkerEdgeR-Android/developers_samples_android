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

package com.example.android.autofill.service.datasource.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.autofill.service.model.AutofillDataset;
import com.example.android.autofill.service.model.FilledAutofillField;

import java.util.Collection;
import java.util.List;

@Dao
public interface AutofillDao {
    /**
     * Fetches a map of dataset names to associated autofill fields. It should only return a dataset
     * if that dataset has an autofill field associate with the view the user is focused on.
     *
     * @param focusedAutofillHints Filtering parameter; represents the hints associated with the
     *                             view the user is focused on.
     * @param allAutofillHints     Filtering parameter; represents all of the hints associated with
     *                             all of the views on the page.
     * @return Map of dataset names to associated autofill fields.
     */
    @Query("SELECT * FROM FilledAutofillField, AutofillDataset" +
            " WHERE AutofillDataset.id = FilledAutofillField.datasetId" +
            " AND FilledAutofillField.hint IN (:allAutofillHints) AND AutofillDataset.id in " +
            "(SELECT datasetId FROM FilledAutofillField WHERE hint in (:focusedAutofillHints))")
    List<AutofillDatasetField> getFilledAutofillFields(
            List<String> focusedAutofillHints,
            List<String> allAutofillHints);

    /**
     * @param autofillFields Collection of autofill fields to be saved to the db.
     */
    @Insert
    void saveFilledAutofillFields(
            Collection<FilledAutofillField> autofillFields);

    @Insert
    void saveAutofillDataset(AutofillDataset datasets);

    @Query("DELETE FROM AutofillDataset")
    void clearAll();

    /**
     * Intermediate POJO class for Room.
     */
    class AutofillDatasetField {
        @Embedded public final AutofillDataset dataset;
        @Embedded public final FilledAutofillField filledAutofillField;

        public AutofillDatasetField(AutofillDataset dataset,
                FilledAutofillField filledAutofillField) {
            this.dataset = dataset;
            this.filledAutofillField = filledAutofillField;
        }
    }
}
