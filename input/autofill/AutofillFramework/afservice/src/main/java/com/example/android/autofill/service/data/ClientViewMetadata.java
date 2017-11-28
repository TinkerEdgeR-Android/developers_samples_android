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

package com.example.android.autofill.service.data;

import android.app.assist.AssistStructure;
import android.service.autofill.SaveInfo;
import android.view.autofill.AutofillId;

import com.example.android.autofill.service.AutofillHints;
import com.example.android.autofill.service.StructureParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.autofill.service.util.Util.logd;

/**
 * In this simple implementation, the only view data we parse from the client are autofill hints
 * of the views in the view hierarchy, the hints of views that are focused, the corresponding
 * autofill IDs, and the {@link SaveInfo} based on the hints.
 * <p>
 * Note: this class is not thread safe.
 */
public class ClientViewMetadata {
    private final StructureParser mStructureParser;

    private List<String> mCachedAllHints;
    private Integer mCachedSaveType;
    private SaveInfo mCachedSaveInfo;
    private List<AutofillId> mCachedAutofillIds;

    public ClientViewMetadata(StructureParser parser) {
        mStructureParser = parser;
    }

    public List<String> getAllHints() {
        if (mCachedAllHints == null) {
            parseHints();
        }
        return mCachedAllHints;
    }

    private List<AutofillId> getAutofillIds() {
        if (mCachedAutofillIds == null) {
            AutofillSaveType autofillSaveType = new AutofillSaveType();
            List<AutofillId> autofillIds = new ArrayList<>();
            mStructureParser.parse((node) -> parseSaveTypeAndIds(node, autofillSaveType, autofillIds));
            mCachedSaveType = autofillSaveType.saveType;
            mCachedAutofillIds = autofillIds;
        }
        return mCachedAutofillIds;
    }

    public AutofillId[] getAutofillIdsArray() {
        List<AutofillId> autofillIds = getAutofillIds();
        if (autofillIds == null || autofillIds.isEmpty()) {
            return null;
        }
        return autofillIds.toArray(new AutofillId[autofillIds.size()]);
    }

    public SaveInfo getSaveInfo() {
        if (mCachedSaveInfo == null) {
            int saveType = getSaveType();
            AutofillId[] autofillIdsArray = getAutofillIdsArray();
            if (autofillIdsArray == null || autofillIdsArray.length == 0) {
                return null;
            }
            // TODO: on MR1, creates a new SaveType without required ids
            mCachedSaveInfo = new SaveInfo.Builder(saveType, autofillIdsArray).build();
        }
        return mCachedSaveInfo;
    }

    public int getSaveType() {
        if (mCachedSaveType == null) {
            AutofillSaveType autofillSaveType = new AutofillSaveType();
            List<AutofillId> autofillIds = new ArrayList<>();
            mStructureParser.parse((node) -> parseSaveTypeAndIds(node, autofillSaveType, autofillIds));
            mCachedSaveType = autofillSaveType.saveType;
            mCachedAutofillIds = autofillIds;
        }
        return mCachedSaveType;
    }

    public String buildWebDomain() {
        StringBuilder webDomainBuilder = new StringBuilder();
        mStructureParser.parse((node) -> parseWebDomain(node, webDomainBuilder));
        return webDomainBuilder.toString();
    }

    private void parseWebDomain(AssistStructure.ViewNode viewNode, StringBuilder validWebDomain) {
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
    }

    private void parseSaveTypeAndIds(AssistStructure.ViewNode root,
            AutofillSaveType autofillSaveType, List<AutofillId> autofillIds) {
        String[] hints = root.getAutofillHints();
        if (hints != null) {
            for (String hint : hints) {
                if (AutofillHints.isValidHint(hint)) {
                    autofillSaveType.saveType |= AutofillHints.getSaveTypeForHint(hint);
                    autofillIds.add(root.getAutofillId());
                }
            }
        }
    }

    private void parseHints() {
        List<String> allHints = new ArrayList<>();
        mStructureParser.parse((node) -> getHints(node, allHints));
        mCachedAllHints = allHints;
    }

    private void getHints(AssistStructure.ViewNode node, List<String> allHints) {
        if (node.getAutofillHints() != null) {
            String[] hints = node.getAutofillHints();
            Collections.addAll(allHints, hints);
        }
    }

    public void clearCache() {
        mCachedAllHints = null;
        mCachedSaveType = null;
        mCachedSaveInfo = null;
        mCachedAutofillIds = null;
    }

    private class AutofillSaveType {
        int saveType;
    }
}
