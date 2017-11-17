/*
 * Copyright 2017, The Android Open Source Project
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

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.example.android.autofill.service.datasource.local.dao.AutofillDao;
import com.example.android.autofill.service.datasource.local.db.AutofillDatabase;
import com.example.android.autofill.service.model.AutofillDataset;
import com.example.android.autofill.service.model.FilledAutofillField;
import com.google.common.collect.ImmutableList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AutofillDaoTest {
    private final AutofillDataset mDataset =
            new AutofillDataset(UUID.randomUUID().toString(), "dataset-1");
    private final FilledAutofillField mUsernameField =
            new FilledAutofillField(mDataset.getId(), View.AUTOFILL_HINT_USERNAME, "login");
    private final FilledAutofillField mPasswordField =
            new FilledAutofillField(mDataset.getId(), View.AUTOFILL_HINT_PASSWORD, "password");

    private List<FilledAutofillField> mAutofillFields;
    private AutofillDatabase mDatabase;

    @Before
    public void setup() {
        mAutofillFields = new ArrayList<>();
        mAutofillFields.add(mUsernameField);
        mAutofillFields.add(mPasswordField);

        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AutofillDatabase.class).build();

    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void insertFilledAutofillFieldAndGet() {
        // When inserting a page's autofill fields.
        mDatabase.autofillDao().saveAutofillDataset(mDataset);
        mDatabase.autofillDao().saveFilledAutofillFields(mAutofillFields);

        // Represents all hints of all fields on page.
        List<String> allHints = ImmutableList.of(View.AUTOFILL_HINT_USERNAME,
                View.AUTOFILL_HINT_PASSWORD);

        // When loading the autofill fields we just saved, while focused on 'username'.
        List<String> usernameFocusedHints = ImmutableList.of(View.AUTOFILL_HINT_USERNAME);
        List<AutofillDao.AutofillDatasetField> loadedOnUsername = mDatabase.autofillDao()
                .getFilledAutofillFields(usernameFocusedHints, allHints);

        // When loading the autofill fields, while focused on 'password'.
        List<String> passwordFocusedHints = ImmutableList.of(View.AUTOFILL_HINT_PASSWORD);

                List<AutofillDao.AutofillDatasetField> loadedOnPassword = mDatabase.autofillDao()
                .getFilledAutofillFields(passwordFocusedHints, allHints);

        AutofillDatasetFieldComparator comparator = new AutofillDatasetFieldComparator();
        loadedOnPassword.sort(comparator);
        loadedOnUsername.sort(comparator);
        assertThat(loadedOnUsername, is(loadedOnPassword));
    }

    private class AutofillDatasetFieldComparator implements
            Comparator<AutofillDao.AutofillDatasetField> {
        @Override
        public int compare(AutofillDao.AutofillDatasetField o1,
                AutofillDao.AutofillDatasetField o2) {
            if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return 1;
            }

            int first = o1.filledAutofillField.getHint().compareTo(
                    o2.filledAutofillField.getHint());
            if (first == 0) {
                return o1.filledAutofillField.getDatasetId().compareTo(
                        o2.filledAutofillField.getDatasetId());
            } else {
                return first;
            }
        }
    }
}
