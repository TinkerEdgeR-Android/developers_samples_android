/*
 * Copyright (c) 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.example.android.wearable.wear.messaging.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.android.wearable.wear.messaging.model.Chat;
import com.example.android.wearable.wear.messaging.model.Message;
import com.example.android.wearable.wear.messaging.model.Profile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * SharedPreferencesHelper provides static methods to set, get, delete these objects.
 *
 * <p>The user's profile details and chat details are persisted in SharedPreferences to access
 * across the app.
 */
public class SharedPreferencesHelper {

    private static final String TAG = "SharedPreferencesHelper";

    private static ObjectMapper mMapper = new ObjectMapper();

    /**
     * Returns logged in user or null if no user is logged in.
     *
     * @param context shared preferences context
     * @return user profile
     * @throws IOException
     */
    public static Profile readUserFromJsonPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        String profileString = sharedPreferences.getString(Constants.PREFS_USER_KEY, null);
        return profileString != null ? mMapper.readValue(profileString, Profile.class) : null;
    }

    /**
     * Writes a {@link Profile} to json and stores it in preferences.
     *
     * @param context used to access {@link SharedPreferences}
     * @param user to be stored in preferences
     * @throws JsonProcessingException if object cannot be written to preferences
     */
    public static void writeUserToJsonPref(Context context, Profile user)
            throws JsonProcessingException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREFS_USER_KEY, mMapper.writeValueAsString(user));
        editor.apply();
    }

    /**
     * Reads contacts from preferences.
     *
     * @param context used to access {@link SharedPreferences}
     * @return contacts from preferences
     * @throws IOException if there is an error parsing the json from preferences
     */
    public static List<Profile> readContactsFromJsonPref(Context context) throws IOException {
        return getList(context, Profile.class, Constants.PREFS_CONTACTS_KEY);
    }

    /**
     * Writes a {@link List<Profile>} to json and stores it in preferences.
     *
     * @param context used to access {@link SharedPreferences}
     * @param contacts to be stored in preferences
     * @throws JsonProcessingException if objects cannot be marshalled to json
     */
    public static void writeContactsToJsonPref(Context context, List<Profile> contacts)
            throws JsonProcessingException {
        setList(context, contacts, Constants.PREFS_CONTACTS_KEY);
    }

    /**
     * Reads chats from preferences
     *
     * @param context used to access {@link SharedPreferences}
     * @return chats from preferences
     * @throws IOException if there is an error parsing the json from preferences
     */
    public static List<Chat> readChatsFromJsonPref(Context context) throws IOException {
        return getList(context, Chat.class, Constants.PREFS_CHATS_KEY);
    }

    /**
     * Writes a {@link List<Chat>} to json and stores it in preferences.
     *
     * @param context used to access {@link SharedPreferences}
     * @param chats to be stores in preferences
     * @throws JsonProcessingException if objects cannot be marshalled to json
     */
    public static void writeChatsToJsonPref(Context context, List<Chat> chats)
            throws JsonProcessingException {
        Log.d(TAG, String.format("Saving %d chat(s)", chats.size()));
        setList(context, chats, Constants.PREFS_CHATS_KEY);
    }

    /**
     * Reads messages for a chat from preferences.
     *
     * @param context used to access {@link SharedPreferences}
     * @param chatId for the chat the messages are from
     * @return messages from preferences
     * @throws IOException if there is an error parsing the json from preferences
     */
    public static List<Message> readMessagesForChat(Context context, String chatId)
            throws IOException {
        return getList(context, Message.class, Constants.PREFS_MESSAGE_PREFIX + chatId);
    }

    /**
     * Writes a {@link List<Message>} to json and stores it in preferences.
     *
     * @param context used to access {@link SharedPreferences}
     * @param chat that the messages are from
     * @param messages to be stored into preferences
     * @throws JsonProcessingException if objects cannot be marshalled to json
     */
    public static void writeMessagesForChatToJsonPref(
            Context context, Chat chat, List<Message> messages) throws JsonProcessingException {
        setList(context, messages, Constants.PREFS_MESSAGE_PREFIX + chat.getId());
    }

    /**
     * Returns List of specified class from SharedPreferences (converts from string in
     * SharedPreferences to class)
     *
     * @param context used for getting an instance of shared preferences
     * @param clazz the class that the strings will be unmarshalled into
     * @param key the key in shared preferences to access the string set
     * @param <T> the type of object that will be in the returned list, should be the same as the
     *     clazz that was supplied
     * @return a list of <T> objects that were stored in shared preferences. Returns an empty list
     *     if no data is available.
     * @throws IOException if the string cannot unmarshall into the object <T>
     */
    private static <T> List<T> getList(Context context, Class<T> clazz, String key)
            throws IOException {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> contactsSet = sharedPreferences.getStringSet(key, new HashSet<String>());
        if (contactsSet.isEmpty()) {
            // Favoring mutability of the list over Collections.emptyList().
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(contactsSet.size());
        for (String contactString : contactsSet) {
            list.add(mMapper.readValue(contactString, clazz));
        }
        return list;
    }

    /**
     * Sets a List of specified class in SharedPreferences (converts from List of class to string
     * for SharedPreferences)
     *
     * @param context used for getting an instance of shared preferences
     * @param list of <T> object that need to be persisted
     * @param key the key in shared preferences which the string set will be stored
     * @param <T> type the of object we will be marshalling and persisting
     * @throws JsonProcessingException if the object cannot be written to a string
     */
    private static <T> void setList(Context context, List<T> list, String key)
            throws JsonProcessingException {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> strings = new LinkedHashSet<>(list.size());
        for (T t : list) {
            strings.add(mMapper.writeValueAsString(t));
        }
        editor.putStringSet(key, strings);
        editor.apply();
    }
}
