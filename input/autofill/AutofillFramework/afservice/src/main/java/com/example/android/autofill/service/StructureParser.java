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
package com.example.android.autofill.service;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.app.assist.AssistStructure.WindowNode;
import android.content.Context;
import android.view.autofill.AutofillValue;

import com.example.android.autofill.service.datasource.local.SharedPrefsDigitalAssetLinksRepository;
import com.example.android.autofill.service.datasource.local.LocalAutofillDataSource;
import com.example.android.autofill.service.model.AutofillDataset;
import com.example.android.autofill.service.model.FilledAutofillField;
import com.example.android.autofill.service.model.FilledAutofillFieldCollection;

import java.util.List;
import java.util.UUID;

import static com.example.android.autofill.service.util.Util.logd;

/**
 * Parser for an AssistStructure object. This is invoked when the Autofill Service receives an
 * AssistStructure from the client Activity, representing its View hierarchy. In this sample, it
 * parses the hierarchy and collects autofill metadata from {@link ViewNode}s along the way.
 */
final class StructureParser {
    private final AutofillFieldMetadataCollection mAutofillFields =
            new AutofillFieldMetadataCollection();
    private final LocalAutofillDataSource mLocalAutofillDataSource;
    private final SharedPrefsDigitalAssetLinksRepository mDalRepository;
    private final AssistStructure mStructure;
    private FilledAutofillFieldCollection mFilledAutofillFieldCollection;
    private final Context mContext;

    StructureParser(Context context, AssistStructure structure,
            LocalAutofillDataSource localAutofillDataSource,
            SharedPrefsDigitalAssetLinksRepository dalRepository) {
        mContext = context;
        mLocalAutofillDataSource = localAutofillDataSource;
        mDalRepository = dalRepository;
        mStructure = structure;
    }

    public void parseForFill() {
        parse(true);
    }

    public void parseForSave() {
        parse(false);
    }

    /**
     * Traverse AssistStructure and add ViewNode metadata to a flat list.
     */
    private void parse(boolean forFill) {
        logd("Parsing structure for %s", mStructure.getActivityComponent());
        int nodes = mStructure.getWindowNodeCount();
        String datasetName = "dataset-" + mLocalAutofillDataSource.getDatasetNumber();
        String datasetId = UUID.randomUUID().toString();
        AutofillDataset dataset = new AutofillDataset(datasetId, datasetName);
        mFilledAutofillFieldCollection = new FilledAutofillFieldCollection(dataset);
        StringBuilder webDomain = new StringBuilder();
        for (int i = 0; i < nodes; i++) {
            WindowNode node = mStructure.getWindowNodeAt(i);
            ViewNode view = node.getRootViewNode();
            parseLocked(forFill, view, webDomain);
        }
        if (webDomain.length() > 0) {
            String packageName = mStructure.getActivityComponent().getPackageName();
            boolean valid = mDalRepository.isValid(mContext, webDomain.toString(), packageName);
            if (!valid) {
                throw new SecurityException(String.format(
                        "Could not associate web domain %s with app %s", webDomain, packageName));
            }
            logd("Domain %s is valid for %s", webDomain, packageName);
        } else {
            logd("no web domain");
        }
    }

    private void parseLocked(boolean forFill, ViewNode viewNode, StringBuilder validWebDomain) {
        String webDomain = viewNode.getWebDomain();
        if (webDomain != null) {
            logd("child web domain: %s", webDomain);
            if (validWebDomain.length() > 0) {
                if (!webDomain.equals(validWebDomain.toString())) {
                    throw new SecurityException("Found multiple web domains: valid= "
                            + validWebDomain + ", child=" + webDomain);
                }
            } else {
                validWebDomain.append(webDomain);
            }
        }

        if (viewNode.getAutofillHints() != null) {
            String[] filteredHints = AutofillHints.filterForSupportedHints(
                    viewNode.getAutofillHints());
            if (filteredHints != null && filteredHints.length > 0) {
                if (forFill) {
                    mAutofillFields.add(new AutofillFieldMetadata(viewNode));
                } else {
                    AutofillValue autofillValue = viewNode.getAutofillValue();
                    String textValue = null;
                    Long dateValue = null;
                    Boolean toggleValue = null;
                    CharSequence[] autofillOptions = null;
                    Integer listIndex = null;
                    if (autofillValue != null) {
                        if (autofillValue.isText()) {
                            // Using toString of AutofillValue.getTextValue in order to save it to
                            // SharedPreferences.
                            textValue = autofillValue.getTextValue().toString();
                        } else if (autofillValue.isDate()) {
                            dateValue = autofillValue.getDateValue();
                        } else if (autofillValue.isList()) {
                            autofillOptions = viewNode.getAutofillOptions();
                            listIndex = autofillValue.getListValue();
                        } else if (autofillValue.isToggle()) {
                            toggleValue = autofillValue.getToggleValue();
                        }
                    }
                    List<FilledAutofillField> filledAutofillFields =
                            FilledAutofillField.build(
                                    mFilledAutofillFieldCollection.getDataset().getId(),
                                    viewNode.getAutofillHints(),
                                    textValue, dateValue, toggleValue, autofillOptions, listIndex);
                    if (filledAutofillFields != null) {
                        mFilledAutofillFieldCollection.add(filledAutofillFields);
                    }
                }
            }
        }
        int childrenSize = viewNode.getChildCount();
        if (childrenSize > 0) {
            for (int i = 0; i < childrenSize; i++) {
                parseLocked(forFill, viewNode.getChildAt(i), validWebDomain);
            }
        }
    }

    public AutofillFieldMetadataCollection getAutofillFields() {
        return mAutofillFields;
    }

    public FilledAutofillFieldCollection getClientFormData() {
        return mFilledAutofillFieldCollection;
    }
}
