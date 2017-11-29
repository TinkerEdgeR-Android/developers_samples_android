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
import android.util.MutableInt;
import android.view.autofill.AutofillId;

import com.example.android.autofill.service.AutofillHints;
import com.example.android.autofill.service.StructureParser;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.autofill.service.util.Util.logd;

public class ClientViewMetadataBuilder {
    private StructureParser mStructureParser;

    public ClientViewMetadataBuilder(StructureParser parser) {
        mStructureParser = parser;
    }

    public ClientViewMetadata buildClientViewMetadata() {
        List<String> allHints = new ArrayList<>();
        MutableInt saveType = new MutableInt(0);
        List<AutofillId> autofillIds = new ArrayList<>();
        StringBuilder webDomainBuilder = new StringBuilder();
        mStructureParser.parse((node) -> parseNode(node, allHints, saveType, autofillIds));
        mStructureParser.parse((node) -> parseWebDomain(node, webDomainBuilder));
        String webDomain = webDomainBuilder.toString();
        AutofillId[] autofillIdsArray = autofillIds.toArray(new AutofillId[autofillIds.size()]);
        return new ClientViewMetadata(allHints, saveType.value, autofillIdsArray, webDomain);
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

    private void parseNode(AssistStructure.ViewNode root, List<String> allHints,
            MutableInt autofillSaveType, List<AutofillId> autofillIds) {
        String[] hints = root.getAutofillHints();
        if (hints != null) {
            for (String hint : hints) {
                if (AutofillHints.isValidHint(hint)) {
                    allHints.add(hint);
                    autofillSaveType.value |= AutofillHints.getSaveTypeForHint(hint);
                    autofillIds.add(root.getAutofillId());
                }
            }
        }
    }
}
