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
 * Predefined model of data on an autofillable credit card info page.
 */
public class CreditCardInfo implements DatasetModel {
    private String creditCardNumber;
    private String datasetName;

    public CreditCardInfo(String creditCardNumber) {
        this(null, creditCardNumber);
    }

    public CreditCardInfo(String datasetName, String creditCardNumber) {
        this.datasetName = datasetName;
        this.creditCardNumber = creditCardNumber;
    }

    public static CreditCardInfo fromJson(JSONObject jsonObject) {
        try {
            String creditCardNumber = jsonObject.getString("creditCardNumber");
            String datasetName = jsonObject.getString("datasetName");
            return new CreditCardInfo(datasetName, creditCardNumber);
        } catch (JSONException e) {
            return null;
        }
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("creditCardNumber", creditCardNumber);
            jsonObject.put("datasetName", datasetName);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    @Override
    public String getDatasetName() {
        return datasetName;
    }

    @Override
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    @Override
    public void applyToFields(AutofillFieldsCollection autofillFieldsCollection,
            Dataset.Builder datasetBuilder) {
        List<AutofillField> creditCardNumberFields =
                autofillFieldsCollection.getFieldsForHint(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER);

        for (int i = 0; i < creditCardNumberFields.size(); i++) {
            AutofillField field = creditCardNumberFields.get(i);
            datasetBuilder.setValue(field.getId(),
                    AutofillValue.forText(creditCardNumber));
        }
    }
}
