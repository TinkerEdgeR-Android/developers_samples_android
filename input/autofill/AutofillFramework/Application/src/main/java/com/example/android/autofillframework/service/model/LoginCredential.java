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


import org.json.JSONException;
import org.json.JSONObject;

public class LoginCredential implements DatasetModel {
    private final String username;
    private final String password;
    private String datasetName;

    LoginCredential(String username, String password) {
        this(username, password, null);
    }

    LoginCredential(String username, String password, String datasetName) {
        this.username = username;
        this.password = password;
    }

    public static LoginCredential fromJson(JSONObject jsonObject) {
        LoginCredential loginCredential = null;
        try {
            loginCredential = new LoginCredential(jsonObject.getString("username"),
                    jsonObject.getString("password"));
        } catch (JSONException e) {
            return null;
        }
        return loginCredential;
    }

    @Override
    public String getDatasetName() {
        return username;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
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
        return datasetName != null ? datasetName.equals(that.datasetName) : that.datasetName == null;

    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (datasetName != null ? datasetName.hashCode() : 0);
        return result;
    }
}
