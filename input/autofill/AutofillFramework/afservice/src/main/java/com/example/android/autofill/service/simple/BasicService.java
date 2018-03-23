/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.example.android.autofill.service.simple;

import android.app.assist.AssistStructure;
import android.app.assist.AssistStructure.ViewNode;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveInfo;
import android.service.autofill.SaveRequest;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.autofill.service.R;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A very basic {@link AutofillService} implementation that only shows dynamic-generated datasets
 * and don't persist the saved data.
 *
 * <p>This class has 2 goals:
 * <ul>
 *   <li>Provide a simple service that app developers can use to see how their apps behave with
 *   autofill.
 *   <li>Explain the basic autofill workflow / provide a service that can be easily modified.
 * </ul>
 */
/*
 * TODO list:
 * - improve documentation above, explaining
 *   - no-goals, security limitations, etc..
 *   - toast usage
 *   - how dynamic datasets are created
 *   - how save works
 * - use strings instead of hardcoded values
 * - use different icons for different services
 */
public class BasicService extends AutofillService {

    private static final String TAG = "BasicService";

    private static final int NUMBER_DATASETS = 4;

    @Override
    public void onFillRequest(FillRequest request, CancellationSignal cancellationSignal,
            FillCallback callback) {
        Log.d(TAG, "onFillRequest()");

        // Find autofillable fields
        AssistStructure structure = getLatestAssistStructure(request);
        Map<String, AutofillId> fields = getAutofillableFields(structure);
        Log.d(TAG, "autofillable fields:" + fields);

        if (fields.isEmpty()) {
            toast("No autofill hints found");
            callback.onSuccess(null);
            return;
        }
        Log.d(TAG, "autofill hints: " + fields);

        // Create the base response
        FillResponse.Builder response = new FillResponse.Builder();

        // 1.Add the dynamic datasets
        String packageName = getApplicationContext().getPackageName();
        for (int i = 1; i <= NUMBER_DATASETS; i++) {
            Dataset.Builder dataset = new Dataset.Builder();
            for (Entry<String, AutofillId> field : fields.entrySet()) {
                String hint = field.getKey();
                AutofillId id = field.getValue();
                String value = hint + i;
                String displayValue = hint.contains("password") ? "password for #" + i : value;
                RemoteViews presentation = newDatasetPresentation(packageName, displayValue);
                dataset.setValue(id, AutofillValue.forText(value), presentation);
            }
            response.addDataset(dataset.build());
        }

        // 2.Add save info
        Collection<AutofillId> ids = fields.values();
        AutofillId[] requiredIds = new AutofillId[ids.size()];
        ids.toArray(requiredIds);
        response.setSaveInfo(
                // We're simple, so we're generic
                new SaveInfo.Builder(SaveInfo.SAVE_DATA_TYPE_GENERIC, requiredIds).build());

        // 3.Profit!
        callback.onSuccess(response.build());
    }

    @Override
    public void onSaveRequest(SaveRequest request, SaveCallback callback) {
        Log.d(TAG, "onSaveRequest()");
        toast("Save not supported");
        callback.onSuccess();
    }

    // TODO: document
    @NonNull
    private Map<String, AutofillId> getAutofillableFields(@NonNull AssistStructure structure) {
        Map<String, AutofillId> fields = new ArrayMap<>();
        int nodes = structure.getWindowNodeCount();
        for (int i = 0; i < nodes; i++) {
            ViewNode node = structure.getWindowNodeAt(i).getRootViewNode();
            addAutofillableFields(fields, node);
        }
        return fields;
    }

    // TODO: document
    private void addAutofillableFields(@NonNull Map<String, AutofillId> fields,
            @NonNull ViewNode node) {
        String[] hints = node.getAutofillHints();
        if (hints != null) {
            AutofillId id = node.getAutofillId();
            // We're simple, we only care about the first hint
            String hint = hints[0].toLowerCase();
            Log.v(TAG, "Found hint " + hint + " on " + id);
            fields.put(hint, id);
        }
        int childrenSize = node.getChildCount();
        for (int i = 0; i < childrenSize; i++) {
            addAutofillableFields(fields, node.getChildAt(i));
        }
    }

    // TODO: move to common code
    @NonNull
    private static AssistStructure getLatestAssistStructure(@NonNull FillRequest request) {
        List<FillContext> fillContexts = request.getFillContexts();
        return fillContexts.get(fillContexts.size() - 1).getStructure();
    }

    // TODO: move to common code
    @NonNull
    private static RemoteViews newDatasetPresentation(@NonNull String packageName,
            @NonNull CharSequence text) {
        RemoteViews presentation =
                new RemoteViews(packageName, R.layout.multidataset_service_list_item);
        presentation.setTextViewText(R.id.text, text);
        presentation.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        return presentation;
    }

    // TODO: move to common code
    private void toast(@NonNull CharSequence message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
