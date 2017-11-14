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

package com.example.android.autofill.service.datasource.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.autofill.service.datasource.local.dao.AutofillDao;
import com.example.android.autofill.service.model.AutofillDataset;
import com.example.android.autofill.service.model.FilledAutofillField;

@Database(entities = {FilledAutofillField.class, AutofillDataset.class}, version = 1)
public abstract class AutofillDatabase extends RoomDatabase {

    private static final Object sLock = new Object();
    private static AutofillDatabase sInstance;

    public static AutofillDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AutofillDatabase.class, "AutofillSample.db")
                        .build();
            }
            return sInstance;
        }
    }

    public abstract AutofillDao autofillDao();

}