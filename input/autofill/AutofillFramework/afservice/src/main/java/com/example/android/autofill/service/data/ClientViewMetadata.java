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

import android.os.Parcel;
import android.os.Parcelable;
import android.service.autofill.SaveInfo;
import android.view.autofill.AutofillId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * In this simple implementation, the only view data we collect from the client are autofill hints
 * of the views in the view hierarchy, the corresponding autofill IDs, and the {@link SaveInfo}
 * based on the hints.
 */
public class ClientViewMetadata implements Parcelable {

    public static final Creator<ClientViewMetadata> CREATOR = new Creator<ClientViewMetadata>() {
        @Override
        public ClientViewMetadata createFromParcel(Parcel parcel) {
            return new ClientViewMetadata(parcel);
        }

        @Override
        public ClientViewMetadata[] newArray(int size) {
            return new ClientViewMetadata[size];
        }
    };

    private final List<String> mAllHints;
    private final int mSaveType;
    private final AutofillId[] mAutofillIds;
    private final String mWebDomain;

    public ClientViewMetadata(List<String> allHints, int saveType, AutofillId[] autofillIds,
            String webDomain) {
        mAllHints = allHints;
        mSaveType = saveType;
        mAutofillIds = autofillIds;
        mWebDomain = webDomain;
    }

    private ClientViewMetadata(Parcel parcel) {
        mAllHints = new ArrayList<>();
        parcel.readList(mAllHints, String.class.getClassLoader());
        mSaveType = parcel.readInt();
        Parcelable[] ids = parcel.readParcelableArray(AutofillId.class.getClassLoader());
        if (ids != null && ids.length > 0) {
            mAutofillIds = Arrays.copyOf(ids, ids.length, AutofillId[].class);
        } else {
            mAutofillIds = null;
        }
        mWebDomain = parcel.readString();
    }

    public List<String> getAllHints() {
        return mAllHints;
    }

    public AutofillId[] getAutofillIds() {
        return mAutofillIds;
    }

    public int getSaveType() {
        return mSaveType;
    }

    public String getWebDomain() {
        return mWebDomain;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(mAllHints);
        parcel.writeInt(mSaveType);
        parcel.writeParcelableArray(mAutofillIds, 0);
        parcel.writeString(mWebDomain);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "ClientViewMetadata{" +
                "mAllHints=" + mAllHints +
                ", mSaveType=" + mSaveType +
                ", mAutofillIds=" + Arrays.toString(mAutofillIds) +
                ", mWebDomain='" + mWebDomain + '\'' +
                '}';
    }
}
