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
package com.example.android.autofillframework.service;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.app.assist.AssistStructure.WindowNode;
import android.util.Log;
import android.view.View;

import com.example.android.autofillframework.service.model.AutofillField;
import com.example.android.autofillframework.service.model.AutofillFieldsCollection;
import com.example.android.autofillframework.service.model.CreditCardInfo;
import com.example.android.autofillframework.service.model.DatasetModel;
import com.example.android.autofillframework.service.model.LoginCredential;

import static com.example.android.autofillframework.CommonUtil.TAG;

/**
 * Parser for an AssistStructure object. This is invoked when the Autofill Service receives an
 * AssistStructure from the client Activity, representing its View hierarchy. In this basic
 * sample, it only parses the hierarchy looking for Username and Password fields based on their
 * resource IDs.
 */
final class StructureParser {

    public static final int CLIENT_PAGE_TYPE_UNRECOGNIZED = 0;
    public static final int CLIENT_PAGE_TYPE_LOGIN = 1;
    public static final int CLIENT_PAGE_TYPE_CREDIT_CARD_INFO = 2;

    private final AutofillFieldsCollection mAutofillFields = new AutofillFieldsCollection();
    private final AssistStructure structure;
    private int mClientPageType = CLIENT_PAGE_TYPE_UNRECOGNIZED;
    private DatasetModel mDatasetModel;

    StructureParser(AssistStructure structure) {
        this.structure = structure;
    }

    /**
     * Depth first search of AssistStructure in search of Views whose resource ID entry is
     * "usernameField" or "passwordField"
     */
    void parse() {
        Log.d(TAG, "Parsing structure for " + structure.getActivityComponent());
        int nodes = structure.getWindowNodeCount();
        for (int i = 0; i < nodes; i++) {
            WindowNode node = structure.getWindowNodeAt(i);
            ViewNode view = node.getRootViewNode();
            parseLocked(view);
        }
        updateClientPageType();
    }

    private void parseLocked(ViewNode view) {
        if (view.getAutofillType() != View.AUTOFILL_TYPE_NONE) {
            AutofillField field = new AutofillField();
            field.setFrom(view);
            mAutofillFields.add(field);
        }
        int childrenSize = view.getChildCount();
        if (childrenSize > 0) {
            for (int i = 0; i < childrenSize; i++) {
                parseLocked(view.getChildAt(i));
            }
        }
    }

    public AutofillFieldsCollection getAutofillFields() {
        return mAutofillFields;
    }

    public int getSaveTypes() {
        return mAutofillFields.getSaveType();
    }

    private void updateClientPageType() {
        if (mAutofillFields.containsHint(View.AUTOFILL_HINT_USERNAME) &&
                mAutofillFields.containsHint(View.AUTOFILL_HINT_PASSWORD)) {
            mClientPageType = CLIENT_PAGE_TYPE_LOGIN;
            // This only takes the value from one view with USERNAME hint.
            // In a real service we would save all of the username values.
            String username = mAutofillFields.getFieldsForHint
                    (View.AUTOFILL_HINT_USERNAME).get(0).getValue();
            String password = mAutofillFields.getFieldsForHint
                    (View.AUTOFILL_HINT_PASSWORD).get(0).getValue();
            mDatasetModel = new LoginCredential(username, password);
        } else if (mAutofillFields.containsHint(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER)) {
            mClientPageType = CLIENT_PAGE_TYPE_CREDIT_CARD_INFO;
            String creditCardNumber = mAutofillFields.getFieldsForHint
                    (View.AUTOFILL_HINT_CREDIT_CARD_NUMBER).get(0).getValue();
            mDatasetModel = new CreditCardInfo(creditCardNumber);
        }
    }

    public int getClientPageType() {
        return mClientPageType;
    }

    public DatasetModel getDatasetModel() {
        return mDatasetModel;
    }
}
