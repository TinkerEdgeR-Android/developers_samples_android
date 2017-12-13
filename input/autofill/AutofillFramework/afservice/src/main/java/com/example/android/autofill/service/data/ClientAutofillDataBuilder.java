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
import android.support.annotation.NonNull;
import android.view.View;
import android.view.autofill.AutofillValue;

import com.example.android.autofill.service.AutofillHints;
import com.example.android.autofill.service.StructureParser;
import com.example.android.autofill.service.model.AutofillDataset;
import com.example.android.autofill.service.model.DatasetWithFilledAutofillFields;
import com.example.android.autofill.service.model.FilledAutofillField;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import static com.example.android.autofill.service.AutofillHints.convertToStoredHintNames;
import static com.example.android.autofill.service.util.Util.loge;

public class ClientAutofillDataBuilder implements AutofillDataBuilder {
    private final StructureParser mStructureParser;

    public ClientAutofillDataBuilder(StructureParser structureParser) {
        mStructureParser = structureParser;
    }

    @Override
    public List<DatasetWithFilledAutofillFields> buildDatasetsByPartition(int datasetNumber) {
        ImmutableList.Builder<DatasetWithFilledAutofillFields> listBuilder =
                new ImmutableList.Builder<>();
        for (int partition : AutofillHints.PARTITIONS) {
            AutofillDataset autofillDataset = new AutofillDataset(UUID.randomUUID().toString(),
                    "dataset-" + datasetNumber + "." + partition);
            DatasetWithFilledAutofillFields datasetWithFilledAutofillFields =
                    buildDatasetForPartition(autofillDataset, partition);
            if (datasetWithFilledAutofillFields != null) {
                listBuilder.add(datasetWithFilledAutofillFields);
            }
        }
        return listBuilder.build();
    }

    /**
     * Parses a client view structure and build a dataset (in the form of a
     * {@link DatasetWithFilledAutofillFields}) from the view metadata found.
     */
    private DatasetWithFilledAutofillFields buildDatasetForPartition(AutofillDataset dataset,
            int partition) {
        DatasetWithFilledAutofillFields datasetWithFilledAutofillFields =
                new DatasetWithFilledAutofillFields();
        datasetWithFilledAutofillFields.autofillDataset = dataset;
        mStructureParser.parse((node) ->
                parseAutofillFields(node, datasetWithFilledAutofillFields, partition)
        );
        if (datasetWithFilledAutofillFields.filledAutofillFields == null) {
            return null;
        } else {
            return datasetWithFilledAutofillFields;
        }
    }

    private void parseAutofillFields(AssistStructure.ViewNode viewNode,
            DatasetWithFilledAutofillFields datasetWithFilledAutofillFields, int partition) {
        String[] hints = viewNode.getAutofillHints();
        if (hints == null) {
            return;
        }
        List<String> filteredHints = convertToStoredHintNames(Arrays.asList(hints), partition);
        if (filteredHints == null || filteredHints.size() == 0) {
            return;
        }
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
        appendViewMetadata(datasetWithFilledAutofillFields,
                filteredHints, textValue, dateValue, toggleValue,
                autofillOptions, listIndex);
    }

    private void appendViewMetadata(@NonNull DatasetWithFilledAutofillFields
            datasetWithFilledAutofillFields, @NonNull List<String> hints,
            @Nullable String textValue, @Nullable Long dateValue, @Nullable Boolean toggleValue,
            @Nullable CharSequence[] autofillOptions, @Nullable Integer listIndex) {
        for (int i = 0; i < hints.size(); i++) {
            String hint = hints.get(i);
            // Then check if the "actual" hint is supported.
            if (AutofillHints.isValidHint(hint)) {
                // Only add the field if the hint is supported by the type.
                if (textValue != null) {
                    Preconditions.checkArgument(AutofillHints.isValidTypeForHints(hint,
                            View.AUTOFILL_TYPE_TEXT),
                            "Text is invalid type for hint '%s'", hint);
                }
                if (autofillOptions != null && listIndex != null &&
                        autofillOptions.length > listIndex) {
                    Preconditions.checkArgument(AutofillHints.isValidTypeForHints(hint,
                            View.AUTOFILL_TYPE_LIST),
                            "List is invalid type for hint '%s'", hint);
                    textValue = autofillOptions[listIndex].toString();
                }
                if (dateValue != null) {
                    Preconditions.checkArgument(AutofillHints.isValidTypeForHints(hint,
                            View.AUTOFILL_TYPE_DATE),
                            "Date is invalid type for hint '%s'", hint);
                }
                if (toggleValue != null) {
                    Preconditions.checkArgument(AutofillHints.isValidTypeForHints(hint,
                            View.AUTOFILL_TYPE_TOGGLE),
                            "Toggle is invalid type for hint '%s'", hint);
                }
                String datasetId = datasetWithFilledAutofillFields.autofillDataset.getId();
                datasetWithFilledAutofillFields.add(new FilledAutofillField(datasetId,
                        hint, textValue, dateValue, toggleValue));
            } else {
                loge("Invalid hint: %s", hint);
            }
        }
    }
}
