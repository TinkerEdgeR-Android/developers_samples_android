/*
 * Copyright (C) 2017 Google Inc. All Rights Reserved.
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
package com.example.android.wearable.wear.wearaccessibilityapp;

import android.support.v4.app.NotificationCompat.MessagingStyle;

import java.util.ArrayList;

/** Mock data for each of the Notification Style Demos. */
public final class MockDatabase {

    public static MessagingStyleCommsAppData getMessagingStyleData() {
        return MessagingStyleCommsAppData.getInstance();
    }

    /** Represents data needed for MessagingStyle Notification. */
    public static class MessagingStyleCommsAppData {

        private static MessagingStyleCommsAppData sInstance = null;

        // Standard notification values
        private String mContentTitle;
        private String mContentText;

        // Style notification values
        private ArrayList<MessagingStyle.Message> mMessages;
        // Basically, String of all mMessages
        private String mFullConversation;
        // Name preferred when replying to chat
        private String mReplayName;
        private int mNumberOfNewMessages;
        private ArrayList<String> mParticipants;

        public static MessagingStyleCommsAppData getInstance() {
            if (sInstance == null) {
                sInstance = getSync();
            }
            return sInstance;
        }

        private static synchronized MessagingStyleCommsAppData getSync() {
            if (sInstance == null) {
                sInstance = new MessagingStyleCommsAppData();
            }

            return sInstance;
        }

        private MessagingStyleCommsAppData() {
            // Standard notification values
            // Content for API <24 (M and below) devices
            mContentTitle = "2 Messages w/ Famous McFamously";
            mContentText = "Dude! ... You know I am a Pesce-pescetarian. :P";

            // Style notification values

            // For each message, you need the timestamp, in this case, we are using arbitrary ones.
            long currentTime = System.currentTimeMillis();

            mMessages = new ArrayList<>();
            mMessages.add(
                    new MessagingStyle.Message(
                            "What are you doing tonight?", currentTime - 4000, "Famous"));
            mMessages.add(
                    new MessagingStyle.Message(
                            "I don't know, dinner maybe?", currentTime - 3000, null));
            mMessages.add(new MessagingStyle.Message("Sounds good.", currentTime - 2000, "Famous"));
            mMessages.add(new MessagingStyle.Message("How about BBQ?", currentTime - 1000, null));
            // Last two are the newest message (2) from friend
            mMessages.add(new MessagingStyle.Message("Dude!", currentTime, "Famous"));
            mMessages.add(
                    new MessagingStyle.Message(
                            "You know I am a Pesce-pescetarian. :P", currentTime, "Famous"));

            // String version of the mMessages above
            mFullConversation =
                    "Famous: What are you doing tonight?\n\n"
                            + "Me: I don't know, dinner maybe?\n\n"
                            + "Famous: Sounds good.\n\n"
                            + "Me: How about BBQ?\n\n"
                            + "Famous: Dude!\n\n"
                            + "Famous: You know I am a Pesce-pescetarian. :P\n\n";

            mNumberOfNewMessages = 2;

            // Name preferred when replying to chat
            mReplayName = "Me";

            // If the phone is in "Do not disturb mode, the user will still be notified if
            // the user(s) is starred as a favorite.
            mParticipants = new ArrayList<>();
            mParticipants.add("Famous McFamously");
        }

        public String getContentTitle() {
            return mContentTitle;
        }

        public String getContentText() {
            return mContentText;
        }

        public ArrayList<MessagingStyle.Message> getMessages() {
            return mMessages;
        }

        public String getFullConversation() {
            return mFullConversation;
        }

        public String getReplayName() {
            return mReplayName;
        }

        public int getNumberOfNewMessages() {
            return mNumberOfNewMessages;
        }

        public ArrayList<String> getParticipants() {
            return mParticipants;
        }

        @Override
        public String toString() {
            return getFullConversation();
        }
    }
}
