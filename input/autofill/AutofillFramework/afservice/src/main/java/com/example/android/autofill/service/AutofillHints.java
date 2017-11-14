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

import android.service.autofill.SaveInfo;
import android.view.View;

import com.example.android.autofill.service.model.AutofillDataset;
import com.example.android.autofill.service.model.FilledAutofillField;
import com.example.android.autofill.service.model.FilledAutofillFieldCollection;
import com.google.common.collect.ImmutableMap;

import java.util.Calendar;
import java.util.UUID;

import static com.example.android.autofill.service.util.Util.logw;


public final class AutofillHints {
    public static final int PARTITION_OTHER = 0;
    public static final int PARTITION_ADDRESS = 1;
    public static final int PARTITION_EMAIL = 2;
    public static final int PARTITION_CREDIT_CARD = 3;
    public static final int[] PARTITIONS = {
            PARTITION_OTHER, PARTITION_ADDRESS, PARTITION_EMAIL, PARTITION_CREDIT_CARD
    };
    /* TODO: finish building fake data for all hints. */
    private static final ImmutableMap<String, AutofillHintProperties> sValidHints =
            new ImmutableMap.Builder<String, AutofillHintProperties>()
                    .put(View.AUTOFILL_HINT_EMAIL_ADDRESS, new AutofillHintProperties(
                            View.AUTOFILL_HINT_EMAIL_ADDRESS, SaveInfo.SAVE_DATA_TYPE_EMAIL_ADDRESS,
                            PARTITION_EMAIL,
                            (seed, datasetId) -> {
                                String textValue = "email" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_EMAIL_ADDRESS, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_NAME, new AutofillHintProperties(
                            View.AUTOFILL_HINT_NAME, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "name" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_USERNAME, new AutofillHintProperties(
                            View.AUTOFILL_HINT_USERNAME, SaveInfo.SAVE_DATA_TYPE_USERNAME,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "login" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_USERNAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_PASSWORD, new AutofillHintProperties(
                            View.AUTOFILL_HINT_PASSWORD, SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "login" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_PASSWORD, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(View.AUTOFILL_HINT_PHONE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_PHONE, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + "2345678910";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_PHONE, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_POSTAL_ADDRESS, new AutofillHintProperties(
                            View.AUTOFILL_HINT_POSTAL_ADDRESS, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue =
                                        "" + seed + " Fake Ln, Fake, FA, FAA 10001";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_POSTAL_ADDRESS, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_POSTAL_CODE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_POSTAL_CODE, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "1000" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_POSTAL_CODE, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_NUMBER,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + "234567";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId,
                                        View.AUTOFILL_HINT_CREDIT_CARD_NUMBER, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + seed + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(datasetId,
                                        View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + seed);
                                Long dateValue = calendar.getTimeInMillis();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE,
                                        dateValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_DATE))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                CharSequence[] months = monthRange();
                                int month = seed % months.length;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.MONTH, month);
                                String textValue = Integer.toString(month);
                                Long dateValue = calendar.getTimeInMillis();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH,
                                        textValue, dateValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST,
                            View.AUTOFILL_TYPE_DATE))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                Calendar calendar = Calendar.getInstance();
                                int expYear = calendar.get(Calendar.YEAR) + seed;
                                calendar.set(Calendar.YEAR, expYear);
                                Long dateValue = calendar.getTimeInMillis();
                                String textValue = Integer.toString(expYear);
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR,
                                        textValue, dateValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST,
                            View.AUTOFILL_TYPE_DATE))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                CharSequence[] days = dayRange();
                                int day = seed % days.length;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.DATE, day);
                                String textValue = Integer.toString(day);
                                Long dateValue = calendar.getTimeInMillis();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(datasetId,
                                                View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY,
                                                textValue, dateValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST,
                            View.AUTOFILL_TYPE_DATE))
                    .put(W3cHints.HONORIFIC_PREFIX, new AutofillHintProperties(
                            W3cHints.HONORIFIC_PREFIX, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                CharSequence[] examplePrefixes = {"Miss", "Ms.", "Mr.", "Mx.",
                                        "Sr.", "Dr.", "Lady", "Lord"};
                                String textValueFromList =
                                        examplePrefixes[seed % examplePrefixes.length].toString();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(datasetId,
                                                W3cHints.HONORIFIC_PREFIX, textValueFromList);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.GIVEN_NAME, new AutofillHintProperties(W3cHints.GIVEN_NAME,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "name" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.GIVEN_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDITIONAL_NAME, new AutofillHintProperties(
                            W3cHints.ADDITIONAL_NAME, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "addtlname" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ADDITIONAL_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.FAMILY_NAME, new AutofillHintProperties(
                            W3cHints.FAMILY_NAME, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "famname" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.FAMILY_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.HONORIFIC_SUFFIX, new AutofillHintProperties(
                            W3cHints.HONORIFIC_SUFFIX, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                CharSequence[] exampleSuffixes = {"san", "kun", "chan", "sama"};
                                String textValueFromListValue =
                                        exampleSuffixes[seed % exampleSuffixes.length].toString();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.HONORIFIC_SUFFIX,
                                        textValueFromListValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.NEW_PASSWORD, new AutofillHintProperties(
                            W3cHints.NEW_PASSWORD, SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "login" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.NEW_PASSWORD, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CURRENT_PASSWORD, new AutofillHintProperties(
                            View.AUTOFILL_HINT_PASSWORD, SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "login" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_PASSWORD, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ORGANIZATION_TITLE, new AutofillHintProperties(
                            W3cHints.ORGANIZATION_TITLE, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "org" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ORGANIZATION_TITLE, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.ORGANIZATION, new AutofillHintProperties(W3cHints.ORGANIZATION,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "org" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ORGANIZATION, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.STREET_ADDRESS, new AutofillHintProperties(
                            W3cHints.STREET_ADDRESS, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue =
                                        "" + seed + " Fake Ln, Fake, FA, FAA 10001";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.STREET_ADDRESS, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LINE1, new AutofillHintProperties(W3cHints.ADDRESS_LINE1,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + " Fake Ln";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LINE1, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LINE2, new AutofillHintProperties(W3cHints.ADDRESS_LINE2,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "Bldg. " + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LINE2, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LINE3, new AutofillHintProperties(W3cHints.ADDRESS_LINE3,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "Suite " + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LINE3, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LEVEL4, new AutofillHintProperties(
                            W3cHints.ADDRESS_LEVEL4, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "city " + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LEVEL4, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LEVEL3, new AutofillHintProperties(
                            W3cHints.ADDRESS_LEVEL3, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "county " + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LEVEL3, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LEVEL2, new AutofillHintProperties(
                            W3cHints.ADDRESS_LEVEL2, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "state " + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LEVEL2, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LEVEL1, new AutofillHintProperties(
                            W3cHints.ADDRESS_LEVEL1, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "country " + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LEVEL1, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.COUNTRY, new AutofillHintProperties(W3cHints.COUNTRY,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "country " + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.COUNTRY, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.COUNTRY_NAME, new AutofillHintProperties(W3cHints.COUNTRY_NAME,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                CharSequence[] exampleCountries = {"USA", "Mexico", "Canada"};
                                String textValue = exampleCountries[seed % exampleCountries.length]
                                        .toString();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.COUNTRY_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.POSTAL_CODE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_POSTAL_CODE, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + seed + seed + seed + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_POSTAL_CODE, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_NAME, new AutofillHintProperties(W3cHints.CC_NAME,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "firstname" + seed + "lastname" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.CC_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_GIVEN_NAME, new AutofillHintProperties(W3cHints.CC_GIVEN_NAME,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "givenname" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.CC_GIVEN_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_ADDITIONAL_NAME, new AutofillHintProperties(
                            W3cHints.CC_ADDITIONAL_NAME, SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "addtlname" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.CC_ADDITIONAL_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_FAMILY_NAME, new AutofillHintProperties(
                            W3cHints.CC_FAMILY_NAME, SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "familyname" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.CC_FAMILY_NAME, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_NUMBER, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_NUMBER,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + "234567";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_NUMBER,
                                        textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_EXPIRATION, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + seed);
                                Long dateValue = calendar.getTimeInMillis();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE,
                                        dateValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_DATE))
                    .put(W3cHints.CC_EXPIRATION_MONTH, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                CharSequence[] months = monthRange();
                                String textValueFromListValue = months[seed % months.length]
                                        .toString();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH,
                                        textValueFromListValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.CC_EXPIRATION_YEAR, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                Calendar calendar = Calendar.getInstance();
                                int expYear = calendar.get(Calendar.YEAR) + seed;
                                calendar.set(Calendar.YEAR, expYear);
                                Long dateValue = calendar.getTimeInMillis();
                                String textValue = "" + expYear;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR,
                                        textValue, dateValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.CC_CSC, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + seed + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE,
                                        textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_TYPE, new AutofillHintProperties(W3cHints.CC_TYPE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "type" + seed;
                                FilledAutofillField filledAutofillField =
                                        new FilledAutofillField(datasetId, W3cHints.CC_TYPE,
                                                textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TRANSACTION_CURRENCY, new AutofillHintProperties(
                            W3cHints.TRANSACTION_CURRENCY, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                CharSequence[] exampleCurrencies = {"USD", "CAD", "KYD", "CRC"};
                                String textValueFromListValue =
                                        exampleCurrencies[seed % exampleCurrencies.length]
                                                .toString();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TRANSACTION_CURRENCY,
                                        textValueFromListValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TRANSACTION_AMOUNT, new AutofillHintProperties(
                            W3cHints.TRANSACTION_AMOUNT, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "" + seed * 100;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TRANSACTION_AMOUNT, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.LANGUAGE, new AutofillHintProperties(W3cHints.LANGUAGE,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                CharSequence[] exampleLanguages = {"Bulgarian", "Croatian", "Czech",
                                        "Danish", "Dutch", "English", "Estonian"};
                                String textValueFromListValue =
                                        exampleLanguages[seed % exampleLanguages.length].toString();
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.LANGUAGE, textValueFromListValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.BDAY, new AutofillHintProperties(W3cHints.BDAY,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - seed * 10);
                                calendar.set(Calendar.MONTH, seed % 12);
                                calendar.set(Calendar.DATE, seed % 27);
                                Long dateValue = calendar.getTimeInMillis();
                                FilledAutofillField filledAutofillField =
                                        new FilledAutofillField(datasetId, W3cHints.BDAY, dateValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_DATE))
                    .put(W3cHints.BDAY_DAY, new AutofillHintProperties(W3cHints.BDAY_DAY,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "" + seed % 27;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.BDAY_DAY, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.BDAY_MONTH, new AutofillHintProperties(W3cHints.BDAY_MONTH,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "" + seed % 12;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.BDAY_MONTH, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.BDAY_YEAR, new AutofillHintProperties(W3cHints.BDAY_YEAR,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                int year = Calendar.getInstance().get(Calendar.YEAR) - seed * 10;
                                String textValue = "" + year;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.BDAY_YEAR, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.SEX, new AutofillHintProperties(W3cHints.SEX,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "Other";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.SEX, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.URL, new AutofillHintProperties(W3cHints.URL,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "http://google.com";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.URL, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.PHOTO, new AutofillHintProperties(W3cHints.PHOTO,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "photo" + seed + ".jpg";
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.PHOTO, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_SECTION, new AutofillHintProperties(
                            W3cHints.PREFIX_SECTION, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_SECTION);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.SHIPPING, new AutofillHintProperties(W3cHints.SHIPPING,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.SHIPPING);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.BILLING, new AutofillHintProperties(W3cHints.BILLING,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.BILLING);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_HOME, new AutofillHintProperties(W3cHints.PREFIX_HOME,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_HOME);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_WORK, new AutofillHintProperties(W3cHints.PREFIX_WORK,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_WORK);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_FAX, new AutofillHintProperties(W3cHints.PREFIX_FAX,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_FAX);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_PAGER, new AutofillHintProperties(W3cHints.PREFIX_PAGER,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_PAGER);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL, new AutofillHintProperties(W3cHints.TEL,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TEL);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.TEL_COUNTRY_CODE, new AutofillHintProperties(
                            W3cHints.TEL_COUNTRY_CODE, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TEL_COUNTRY_CODE);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_NATIONAL, new AutofillHintProperties(W3cHints.TEL_NATIONAL,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TEL_NATIONAL);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_AREA_CODE, new AutofillHintProperties(
                            W3cHints.TEL_AREA_CODE, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TEL_AREA_CODE);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_LOCAL, new AutofillHintProperties(
                            W3cHints.TEL_LOCAL, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TEL_LOCAL);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_LOCAL_PREFIX, new AutofillHintProperties(
                            W3cHints.TEL_LOCAL_PREFIX, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TEL_LOCAL_PREFIX);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_LOCAL_SUFFIX, new AutofillHintProperties(
                            W3cHints.TEL_LOCAL_SUFFIX, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TEL_LOCAL_SUFFIX);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_EXTENSION, new AutofillHintProperties(W3cHints.TEL_EXTENSION,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.TEL_EXTENSION);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.EMAIL, new AutofillHintProperties(
                            View.AUTOFILL_HINT_EMAIL_ADDRESS, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_EMAIL,
                            (seed, datasetId) -> {
                                String textValue = "email" + seed;
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_EMAIL_ADDRESS, textValue);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.IMPP, new AutofillHintProperties(W3cHints.IMPP,
                            SaveInfo.SAVE_DATA_TYPE_EMAIL_ADDRESS, PARTITION_EMAIL,
                            (seed, datasetId) -> {
                                FilledAutofillField filledAutofillField = new FilledAutofillField(
                                        datasetId, W3cHints.IMPP);
                                return filledAutofillField;
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .build();

    private AutofillHints() {
    }

    public static boolean isValidTypeForHints(String hint, int type) {
        if (hint != null && sValidHints.containsKey(hint)) {
            boolean valid = sValidHints.get(hint).isValidType(type);
            if (valid) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidHint(String hint) {
        return sValidHints.containsKey(hint);
    }

    public static int getSaveTypeForHints(String[] hints) {
        int saveType = 0;
        if (hints != null) {
            for (String hint : hints) {
                if (hint != null && sValidHints.containsKey(hint)) {
                    saveType |= sValidHints.get(hint).getSaveType();
                }
            }
        }
        return saveType;
    }

    public static FilledAutofillField getFakeField(String hint, int seed, String datasetId) {
        return sValidHints.get(hint).generateFakeField(seed, datasetId);
    }

    public static FilledAutofillFieldCollection getFakeFieldCollection(int partition, int seed) {
        String datasetName = "dataset-" + seed;
        String datasetId = UUID.randomUUID().toString();
        AutofillDataset autofillDataset = new AutofillDataset(datasetId, datasetName);
        FilledAutofillFieldCollection filledAutofillFieldCollection =
                new FilledAutofillFieldCollection(autofillDataset);
        for (String hint : sValidHints.keySet()) {
            if (hint != null && sValidHints.get(hint).getPartition() == partition) {
                FilledAutofillField fakeField = getFakeField(hint, seed, datasetId);
                filledAutofillFieldCollection.add(fakeField);
            }
        }
        return filledAutofillFieldCollection;
    }

    private static String getStoredHintName(String hint) {
        return sValidHints.get(hint).getAutofillHint();
    }

    public static void convertToStoredHintNames(String[] hints) {
        for (int i = 0; i < hints.length; i++) {
            hints[i] = getStoredHintName(hints[i]);
        }
    }

    private static CharSequence[] dayRange() {
        CharSequence[] days = new CharSequence[27];
        for (int i = 0; i < days.length; i++) {
            days[i] = Integer.toString(i);
        }
        return days;
    }

    private static CharSequence[] monthRange() {
        CharSequence[] months = new CharSequence[12];
        for (int i = 0; i < months.length; i++) {
            months[i] = Integer.toString(i);
        }
        return months;
    }

    public static String[] filterForSupportedHints(String[] hints) {
        String[] filteredHints = new String[hints.length];
        int i = 0;
        for (String hint : hints) {
            if (AutofillHints.isValidHint(hint)) {
                filteredHints[i++] = hint;
            } else {
                logw("Invalid autofill hint: %s", hint);
            }
        }
        if (i == 0) {
            return null;
        }
        String[] finalFilteredHints = new String[i];
        System.arraycopy(filteredHints, 0, finalFilteredHints, 0, i);
        return finalFilteredHints;
    }

    public static boolean isW3cSectionPrefix(String hint) {
        return hint.startsWith(W3cHints.PREFIX_SECTION);
    }

    public static boolean isW3cAddressType(String hint) {
        switch (hint) {
            case W3cHints.SHIPPING:
            case W3cHints.BILLING:
                return true;
        }
        return false;
    }

    public static boolean isW3cTypePrefix(String hint) {
        switch (hint) {
            case W3cHints.PREFIX_WORK:
            case W3cHints.PREFIX_FAX:
            case W3cHints.PREFIX_HOME:
            case W3cHints.PREFIX_PAGER:
                return true;
        }
        return false;
    }

    public static boolean isW3cTypeHint(String hint) {
        switch (hint) {
            case W3cHints.TEL:
            case W3cHints.TEL_COUNTRY_CODE:
            case W3cHints.TEL_NATIONAL:
            case W3cHints.TEL_AREA_CODE:
            case W3cHints.TEL_LOCAL:
            case W3cHints.TEL_LOCAL_PREFIX:
            case W3cHints.TEL_LOCAL_SUFFIX:
            case W3cHints.TEL_EXTENSION:
            case W3cHints.EMAIL:
            case W3cHints.IMPP:
                return true;
        }
        logw("Invalid W3C type hint: %s", hint);
        return false;
    }
}
