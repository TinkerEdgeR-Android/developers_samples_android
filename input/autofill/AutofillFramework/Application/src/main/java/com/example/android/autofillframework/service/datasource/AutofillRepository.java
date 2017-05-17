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
package com.example.android.autofillframework.service.datasource;

import com.example.android.autofillframework.service.model.CreditCardInfo;
import com.example.android.autofillframework.service.model.DatasetModel;
import com.example.android.autofillframework.service.model.LoginCredential;

import java.util.HashMap;

public interface AutofillRepository {

    /**
     * Gets LoginCredential that was originally saved with this {@code datasetName}.
     */
    HashMap<String, LoginCredential> getLoginCredentials();

    /**
     * Saves LoginCredential under this datasetName.
     */
    void saveLoginCredential(DatasetModel loginCredential);

    HashMap<String, CreditCardInfo> getCreditCardInfo();

    void saveCreditCardInfo(DatasetModel creditCardInfo);
}
