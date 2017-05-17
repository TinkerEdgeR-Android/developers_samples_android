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
import android.view.View;
import android.view.autofill.AutofillValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Predefined model of data on an autofillable login page.
 */
public class LoginCredential implements DatasetModel {
    private final String username;
    private final String password;
    private String datasetName;

    public LoginCredential(String username, String password) {
        this(username, password, null);
    }

    public LoginCredential(String username, String password, String datasetName) {
        this.username = username;
        this.password = password;
        this.datasetName = datasetName;
    }

    public static LoginCredential fromJson(JSONObject jsonObject) {
        try {
            return new LoginCredential(jsonObject.getString("username"),
                    jsonObject.getString("password"),
                    jsonObject.getString("datasetName"));
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    @Override
    public void applyToFields(AutofillFieldsCollection autofillFieldsCollection,
            Dataset.Builder datasetBuilder) {
        List<AutofillField> usernameFields =
                autofillFieldsCollection.getFieldsForHint(View.AUTOFILL_HINT_USERNAME);
        List<AutofillField> passwordFields =
                autofillFieldsCollection.getFieldsForHint(View.AUTOFILL_HINT_PASSWORD);

        for (int i = 0; i < usernameFields.size(); i++) {
            AutofillField field = usernameFields.get(i);
            datasetBuilder.setValue(field.getId(),
                    AutofillValue.forText(username));
        }

        for (int i = 0; i < passwordFields.size(); i++) {
            AutofillField field = passwordFields.get(i);
            datasetBuilder.setValue(field.getId(),
                    AutofillValue.forText(password));
        }
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("datasetName", datasetName);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginCredential that = (LoginCredential) o;

        if (!username.equals(that.username)) return false;
        if (!password.equals(that.password)) return false;
        return datasetName != null ?
                datasetName.equals(that.datasetName) : that.datasetName == null;

    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (datasetName != null ? datasetName.hashCode() : 0);
        return result;
    }
}
