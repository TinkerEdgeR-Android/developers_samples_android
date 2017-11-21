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
import android.support.annotation.NonNull;
import android.view.View;

import com.example.android.autofill.service.model.FilledAutofillField;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.example.android.autofill.service.util.Util.logd;
import static com.example.android.autofill.service.util.Util.logw;
import static java.util.stream.Collectors.toList;

public final class AutofillHints {
    public static final int PARTITION_ALL = -1;
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
                                return new FilledAutofillField(datasetId,
                                        View.AUTOFILL_HINT_EMAIL_ADDRESS, textValue);

                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_NAME, new AutofillHintProperties(
                            View.AUTOFILL_HINT_NAME, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "name" + seed;
                                return new FilledAutofillField(datasetId, View.AUTOFILL_HINT_NAME,
                                        textValue);

                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_USERNAME, new AutofillHintProperties(
                            View.AUTOFILL_HINT_USERNAME, SaveInfo.SAVE_DATA_TYPE_USERNAME,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "login" + seed;
                                return new FilledAutofillField(datasetId,
                                        View.AUTOFILL_HINT_USERNAME, textValue);

                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_PASSWORD, new AutofillHintProperties(
                            View.AUTOFILL_HINT_PASSWORD, SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "login" + seed;
                                return new FilledAutofillField(datasetId,
                                        View.AUTOFILL_HINT_PASSWORD, textValue);

                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(View.AUTOFILL_HINT_PHONE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_PHONE, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + "2345678910";
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_PHONE, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_POSTAL_ADDRESS, new AutofillHintProperties(
                            View.AUTOFILL_HINT_POSTAL_ADDRESS, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue =
                                        "" + seed + " Fake Ln, Fake, FA, FAA 10001";
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_POSTAL_ADDRESS, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_POSTAL_CODE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_POSTAL_CODE, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "1000" + seed;
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_POSTAL_CODE, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_NUMBER,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + "234567";
                                return new FilledAutofillField(
                                        datasetId,
                                        View.AUTOFILL_HINT_CREDIT_CARD_NUMBER, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + seed + seed;
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE,
                                        textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + seed);
                                Long dateValue = calendar.getTimeInMillis();
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE,
                                        dateValue);
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
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH,
                                        textValue, dateValue);
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
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR,
                                        textValue, dateValue);
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
                                return new FilledAutofillField(datasetId,
                                        View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY,
                                        textValue, dateValue);
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
                                return new FilledAutofillField(
                                        datasetId, W3cHints.HONORIFIC_PREFIX, textValueFromList);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.GIVEN_NAME, new AutofillHintProperties(W3cHints.GIVEN_NAME,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "name" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.GIVEN_NAME, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDITIONAL_NAME, new AutofillHintProperties(
                            W3cHints.ADDITIONAL_NAME, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "addtlname" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ADDITIONAL_NAME, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.FAMILY_NAME, new AutofillHintProperties(
                            W3cHints.FAMILY_NAME, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "famname" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.FAMILY_NAME, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.HONORIFIC_SUFFIX, new AutofillHintProperties(
                            W3cHints.HONORIFIC_SUFFIX, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                CharSequence[] exampleSuffixes = {"san", "kun", "chan", "sama"};
                                String textValueFromListValue =
                                        exampleSuffixes[seed % exampleSuffixes.length].toString();
                                return new FilledAutofillField(
                                        datasetId, W3cHints.HONORIFIC_SUFFIX,
                                        textValueFromListValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.NEW_PASSWORD, new AutofillHintProperties(
                            W3cHints.NEW_PASSWORD, SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "login" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.NEW_PASSWORD, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CURRENT_PASSWORD, new AutofillHintProperties(
                            View.AUTOFILL_HINT_PASSWORD, SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "login" + seed;
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_PASSWORD, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ORGANIZATION_TITLE, new AutofillHintProperties(
                            W3cHints.ORGANIZATION_TITLE, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "org" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ORGANIZATION_TITLE, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.ORGANIZATION, new AutofillHintProperties(W3cHints.ORGANIZATION,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "org" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ORGANIZATION, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.STREET_ADDRESS, new AutofillHintProperties(
                            W3cHints.STREET_ADDRESS, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue =
                                        "" + seed + " Fake Ln, Fake, FA, FAA 10001";
                                return new FilledAutofillField(
                                        datasetId, W3cHints.STREET_ADDRESS, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LINE1, new AutofillHintProperties(W3cHints.ADDRESS_LINE1,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + " Fake Ln";
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LINE1, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LINE2, new AutofillHintProperties(W3cHints.ADDRESS_LINE2,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "Bldg. " + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LINE2, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LINE3, new AutofillHintProperties(W3cHints.ADDRESS_LINE3,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "Suite " + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LINE3, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LEVEL4, new AutofillHintProperties(
                            W3cHints.ADDRESS_LEVEL4, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "city " + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LEVEL4, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LEVEL3, new AutofillHintProperties(
                            W3cHints.ADDRESS_LEVEL3, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "county " + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LEVEL3, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LEVEL2, new AutofillHintProperties(
                            W3cHints.ADDRESS_LEVEL2, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "state " + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LEVEL2, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.ADDRESS_LEVEL1, new AutofillHintProperties(
                            W3cHints.ADDRESS_LEVEL1, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "country " + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.ADDRESS_LEVEL1, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.COUNTRY, new AutofillHintProperties(W3cHints.COUNTRY,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "country " + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.COUNTRY, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.COUNTRY_NAME, new AutofillHintProperties(W3cHints.COUNTRY_NAME,
                            SaveInfo.SAVE_DATA_TYPE_ADDRESS, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                CharSequence[] exampleCountries = {"USA", "Mexico", "Canada"};
                                String textValue = exampleCountries[seed % exampleCountries.length]
                                        .toString();
                                return new FilledAutofillField(
                                        datasetId, W3cHints.COUNTRY_NAME, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.POSTAL_CODE, new AutofillHintProperties(
                            View.AUTOFILL_HINT_POSTAL_CODE, SaveInfo.SAVE_DATA_TYPE_ADDRESS,
                            PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + seed + seed + seed + seed;
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_POSTAL_CODE, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_NAME, new AutofillHintProperties(W3cHints.CC_NAME,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "firstname" + seed + "lastname" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.CC_NAME, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_GIVEN_NAME, new AutofillHintProperties(W3cHints.CC_GIVEN_NAME,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "givenname" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.CC_GIVEN_NAME, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_ADDITIONAL_NAME, new AutofillHintProperties(
                            W3cHints.CC_ADDITIONAL_NAME, SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "addtlname" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.CC_ADDITIONAL_NAME, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_FAMILY_NAME, new AutofillHintProperties(
                            W3cHints.CC_FAMILY_NAME, SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD,
                            PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "familyname" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.CC_FAMILY_NAME, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_NUMBER, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_NUMBER,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + "234567";
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_NUMBER,
                                        textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_EXPIRATION, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + seed);
                                Long dateValue = calendar.getTimeInMillis();
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE,
                                        dateValue);
                            }, View.AUTOFILL_TYPE_DATE))
                    .put(W3cHints.CC_EXPIRATION_MONTH, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                CharSequence[] months = monthRange();
                                String textValueFromListValue = months[seed % months.length]
                                        .toString();
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH,
                                        textValueFromListValue);
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
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR,
                                        textValue, dateValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.CC_CSC, new AutofillHintProperties(
                            View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "" + seed + seed + seed;
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE,
                                        textValue);

                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.CC_TYPE, new AutofillHintProperties(W3cHints.CC_TYPE,
                            SaveInfo.SAVE_DATA_TYPE_CREDIT_CARD, PARTITION_CREDIT_CARD,
                            (seed, datasetId) -> {
                                String textValue = "type" + seed;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.CC_TYPE, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TRANSACTION_CURRENCY, new AutofillHintProperties(
                            W3cHints.TRANSACTION_CURRENCY, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                CharSequence[] exampleCurrencies = {"USD", "CAD", "KYD", "CRC"};
                                String textValueFromListValue =
                                        exampleCurrencies[seed % exampleCurrencies.length]
                                                .toString();
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TRANSACTION_CURRENCY,
                                        textValueFromListValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TRANSACTION_AMOUNT, new AutofillHintProperties(
                            W3cHints.TRANSACTION_AMOUNT, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "" + seed * 100;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TRANSACTION_AMOUNT, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.LANGUAGE, new AutofillHintProperties(W3cHints.LANGUAGE,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                CharSequence[] exampleLanguages = {"Bulgarian", "Croatian", "Czech",
                                        "Danish", "Dutch", "English", "Estonian"};
                                String textValueFromListValue =
                                        exampleLanguages[seed % exampleLanguages.length].toString();
                                return new FilledAutofillField(
                                        datasetId, W3cHints.LANGUAGE, textValueFromListValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.BDAY, new AutofillHintProperties(W3cHints.BDAY,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - seed * 10);
                                calendar.set(Calendar.MONTH, seed % 12);
                                calendar.set(Calendar.DATE, seed % 27);
                                Long dateValue = calendar.getTimeInMillis();
                                return new FilledAutofillField(datasetId, W3cHints.BDAY, dateValue);
                            }, View.AUTOFILL_TYPE_DATE))
                    .put(W3cHints.BDAY_DAY, new AutofillHintProperties(W3cHints.BDAY_DAY,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "" + seed % 27;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.BDAY_DAY, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.BDAY_MONTH, new AutofillHintProperties(W3cHints.BDAY_MONTH,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "" + seed % 12;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.BDAY_MONTH, textValue);

                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.BDAY_YEAR, new AutofillHintProperties(W3cHints.BDAY_YEAR,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                int year = Calendar.getInstance().get(Calendar.YEAR) - seed * 10;
                                String textValue = "" + year;
                                return new FilledAutofillField(
                                        datasetId, W3cHints.BDAY_YEAR, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.SEX, new AutofillHintProperties(W3cHints.SEX,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "Other";
                                return new FilledAutofillField(
                                        datasetId, W3cHints.SEX, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.URL, new AutofillHintProperties(W3cHints.URL,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "http://google.com";
                                return new FilledAutofillField(
                                        datasetId, W3cHints.URL, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.PHOTO, new AutofillHintProperties(W3cHints.PHOTO,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                String textValue = "photo" + seed + ".jpg";
                                return new FilledAutofillField(
                                        datasetId, W3cHints.PHOTO, textValue);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_SECTION, new AutofillHintProperties(
                            W3cHints.PREFIX_SECTION, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_SECTION);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.SHIPPING, new AutofillHintProperties(W3cHints.SHIPPING,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.SHIPPING);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.BILLING, new AutofillHintProperties(W3cHints.BILLING,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_ADDRESS,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.BILLING);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_HOME, new AutofillHintProperties(W3cHints.PREFIX_HOME,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_HOME);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_WORK, new AutofillHintProperties(W3cHints.PREFIX_WORK,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_WORK);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_FAX, new AutofillHintProperties(W3cHints.PREFIX_FAX,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_FAX);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.PREFIX_PAGER, new AutofillHintProperties(W3cHints.PREFIX_PAGER,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.PREFIX_PAGER);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL, new AutofillHintProperties(W3cHints.TEL,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TEL);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.TEL_COUNTRY_CODE, new AutofillHintProperties(
                            W3cHints.TEL_COUNTRY_CODE, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TEL_COUNTRY_CODE);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_NATIONAL, new AutofillHintProperties(W3cHints.TEL_NATIONAL,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TEL_NATIONAL);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_AREA_CODE, new AutofillHintProperties(
                            W3cHints.TEL_AREA_CODE, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TEL_AREA_CODE);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_LOCAL, new AutofillHintProperties(
                            W3cHints.TEL_LOCAL, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TEL_LOCAL);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_LOCAL_PREFIX, new AutofillHintProperties(
                            W3cHints.TEL_LOCAL_PREFIX, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TEL_LOCAL_PREFIX);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_LOCAL_SUFFIX, new AutofillHintProperties(
                            W3cHints.TEL_LOCAL_SUFFIX, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TEL_LOCAL_SUFFIX);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.TEL_EXTENSION, new AutofillHintProperties(W3cHints.TEL_EXTENSION,
                            SaveInfo.SAVE_DATA_TYPE_GENERIC, PARTITION_OTHER,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.TEL_EXTENSION);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .put(W3cHints.EMAIL, new AutofillHintProperties(
                            View.AUTOFILL_HINT_EMAIL_ADDRESS, SaveInfo.SAVE_DATA_TYPE_GENERIC,
                            PARTITION_EMAIL,
                            (seed, datasetId) -> {
                                String textValue = "email" + seed;
                                return new FilledAutofillField(
                                        datasetId, View.AUTOFILL_HINT_EMAIL_ADDRESS, textValue);
                            }, View.AUTOFILL_TYPE_TEXT))
                    .put(W3cHints.IMPP, new AutofillHintProperties(W3cHints.IMPP,
                            SaveInfo.SAVE_DATA_TYPE_EMAIL_ADDRESS, PARTITION_EMAIL,
                            (seed, datasetId) -> {
                                return new FilledAutofillField(
                                        datasetId, W3cHints.IMPP);
                            }, View.AUTOFILL_TYPE_TEXT, View.AUTOFILL_TYPE_LIST))
                    .build();

    private AutofillHints() {
    }

    public static boolean isValidTypeForHints(@NonNull String hint, int type) {
        if (sValidHints.containsKey(hint)) {
            boolean valid = sValidHints.get(hint).isValidType(type);
            if (valid) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidHint(@NonNull String hint) {
        return sValidHints.containsKey(hint);
    }

    public static int getSaveTypeForHint(@NonNull String hint) {
        if (sValidHints.containsKey(hint)) {
            return sValidHints.get(hint).getSaveType();
        } else {
            return 0;
        }
    }

    public static FilledAutofillField generateFakeField(@NonNull String hint, int seed,
            String datasetId) {
        if (isValidHint(hint)) {
            return sValidHints.get(hint).generateFakeField(seed, datasetId);
        } else {
            return null;
        }
    }

    public static ImmutableSet<String> getHints() {
        return sValidHints.keySet();
    }

    public static List<String> convertToStoredHintNames(@NonNull List<String> hints) {
        return convertToStoredHintNames(hints, PARTITION_ALL);
    }

    public static List<String> convertToStoredHintNames(@NonNull List<String> hints, int partition) {
        return removePrefixes(hints)
                .stream()
                .filter(sValidHints::containsKey)
                .map(sValidHints::get)
                .filter(Objects::nonNull)
                .filter((properties) -> matchesPartition(properties, partition))
                .map(AutofillHintProperties::getAutofillHint)
                .collect(toList());
    }

    public static boolean matchesPartition(@NonNull String hint, int partition) {
        return isValidHint(hint) && matchesPartition(sValidHints.get(hint), partition);
    }

    private static boolean matchesPartition(@NonNull AutofillHintProperties properties,
            int partition) {
        return partition == PARTITION_ALL || properties.getPartition() == partition;
    }

    private static List<String> removePrefixes(@NonNull List<String> hints) {
        List<String> hintsWithoutPrefixes = new ArrayList<>();
        String nextHint = null;
        for (int i = 0; i < hints.size(); i++) {
            String hint = hints.get(i);
            if (i < hints.size() - 1) {
                nextHint = hints.get(i + 1);
            }
            // First convert the compound W3C autofill hints
            if (isW3cSectionPrefix(hint) && i < hints.size() - 1) {
                i++;
                hint = hints.get(i);
                logd("Hint is a W3C section prefix; using %s instead", hint);
                if (i < hints.size() - 1) {
                    nextHint = hints.get(i + 1);
                }
            }
            if (isW3cTypePrefix(hint) && nextHint != null && isW3cTypeHint(nextHint)) {
                hint = nextHint;
                i++;
                logd("Hint is a W3C type prefix; using %s instead", hint);
            }
            if (isW3cAddressType(hint) && nextHint != null) {
                hint = nextHint;
                i++;
                logd("Hint is a W3C address prefix; using %s instead", hint);
            }
            hintsWithoutPrefixes.add(hint);
        }
        return hintsWithoutPrefixes;
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

    private static boolean isW3cSectionPrefix(@NonNull String hint) {
        return hint.startsWith(W3cHints.PREFIX_SECTION);
    }

    private static boolean isW3cAddressType(@NonNull String hint) {
        switch (hint) {
            case W3cHints.SHIPPING:
            case W3cHints.BILLING:
                return true;
        }
        return false;
    }

    private static boolean isW3cTypePrefix(@NonNull String hint) {
        switch (hint) {
            case W3cHints.PREFIX_WORK:
            case W3cHints.PREFIX_FAX:
            case W3cHints.PREFIX_HOME:
            case W3cHints.PREFIX_PAGER:
                return true;
        }
        return false;
    }

    private static boolean isW3cTypeHint(@NonNull String hint) {
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
