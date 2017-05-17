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

import android.app.assist.AssistStructure;
import android.service.autofill.SaveInfo;
import android.view.View;
import android.view.autofill.AutofillId;

/**
 * Class that represents a field that can be autofilled. It will contain a description
 * (what type data the field holds), an AutoFillId (an ID unique to the rest of the ViewStructure),
 * and a value (what data is currently in the field).
 */
public class AutofillField {
    private int saveType = 0;
    private String[] hints;
    private AutofillId id;

    // For simplicity, we will only support text values.
    // TODO support multiple value types.
    private String value;

    public AutofillField() {
    }

    public void setFrom(AssistStructure.ViewNode view) {
        id = view.getAutofillId();
        setHints(view.getAutofillHints());
    }

    public String[] getHints() {
        return hints;
    }

    public void setHints(String[] hints) {
        this.hints = hints;
        updateSaveTypeFromHints();
    }

    public int getSaveType() {
        return saveType;
    }

    public AutofillId getId() {
        return id;
    }

    public void setId(AutofillId id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AutofillField: [id=" + id + ", value=" + value + "]";
    }

    private void updateSaveTypeFromHints() {
        saveType = 0;
        if(hints == null) {
            return;
        }
        for (String hint : hints) {
            switch (hint) {
                case View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE:
                case View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY:
                case View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH:
                case View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR:
                case View.AUTOFILL_HINT_CREDIT_CARD_NUMBER:
                case View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE:
                    saveType |= SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD;
                    break;
                case View.AUTOFILL_HINT_EMAIL_ADDRESS:
                    saveType |= SaveInfo.SAVE_DATA_TYPE_EMAIL_ADDRESS;
                    break;
                case View.AUTOFILL_HINT_NAME:
                    saveType |= SaveInfo.SAVE_DATA_TYPE_GENERIC;
                    break;
                case View.AUTOFILL_HINT_PASSWORD:
                    saveType |= SaveInfo.SAVE_DATA_TYPE_PASSWORD;
                    saveType &= ~SaveInfo.SAVE_DATA_TYPE_EMAIL_ADDRESS;
                    saveType &= ~SaveInfo.SAVE_DATA_TYPE_USERNAME;
                    break;
                case View.AUTOFILL_HINT_PHONE:
                    saveType |= SaveInfo.SAVE_DATA_TYPE_GENERIC;
                    break;
                case View.AUTOFILL_HINT_POSTAL_ADDRESS:
                case View.AUTOFILL_HINT_POSTAL_CODE:
                    saveType |= SaveInfo.SAVE_DATA_TYPE_ADDRESS;
                    break;
                case View.AUTOFILL_HINT_USERNAME:
                    saveType |= SaveInfo.SAVE_DATA_TYPE_USERNAME;
                    break;
            }
        }
    }
}
