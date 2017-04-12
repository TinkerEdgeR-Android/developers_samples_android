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
package com.example.android.wearable.watchface.models;

import com.example.android.wearable.watchface.R;

import java.util.ArrayList;

/**
 * Data represents different views for configuring the
 * {@link com.example.android.wearable.watchface.watchfaces.ComplicationSimpleWatchFaceService}
 * watch face's appearance and complications.
 */
public class ComplicationsSimpleWatchFaceSettingsConfigData {

    public static ArrayList<Object> getSettingsData() {

        ArrayList<Object> settingsConfigData = new ArrayList<>();

        // Data for complications UX in settings Activity (ComplicationSimpleConfigActivity).
        Object complicationConfigItem =
                new ComplicationsConfigItem(R.drawable.preview_settings_complication_240);
        settingsConfigData.add(complicationConfigItem);

        // Data for drawable indicating there are more options in the settings
        // Activity (ComplicationSimpleConfigActivity).
        Object moreOptionsConfigItem =
                new MoreOptionsConfigItem(R.drawable.ic_expand_more_white_18dp);
        settingsConfigData.add(moreOptionsConfigItem);

        // Data for Markers (watch face arms/hands/ticks) UX in settings
        // Activity (ComplicationSimpleConfigActivity).
        Object markersConfigItem =
                new AppearanceConfigItem("Markers", R.drawable.icn_styles);
        settingsConfigData.add(markersConfigItem);

        // Data for 'Unread Notifications' UX (toggle) in settings
        // Activity (ComplicationSimpleConfigActivity).
        Object unreadNotificationsConfigItem =
                new AppearanceConfigItem(
                        "Unread Notifications",
                        R.drawable.ic_notifications_white_24dp);
        settingsConfigData.add(unreadNotificationsConfigItem);

        // Data for background complications UX in settings
        // Activity (ComplicationSimpleConfigActivity).
        Object backgroundImageComplicationConfigItem =
                new AppearanceConfigItem("Background Image", R.drawable.ic_landscape_white);
        settingsConfigData.add(backgroundImageComplicationConfigItem);

        return settingsConfigData;
    }

    /**
     * Represents Complication data for config settings in a RecyclerView.
     */
    public static class ComplicationsConfigItem {
        private int watchFaceDrawable;

        ComplicationsConfigItem(int drawable) {
            watchFaceDrawable = drawable;
        }

        public int getWatchFaceDrawable() {
            return watchFaceDrawable;
        }
    }

    /**
     * Represents simple "more options" icon for config settings in a RecyclerView.
     */
    public static class MoreOptionsConfigItem {
        private int drawable;

        MoreOptionsConfigItem(int drawable) {
            this.drawable = drawable;
        }

        public int getDrawable() {
            return drawable;
        }
    }

    /**
     * Represents Appearance type data for config settings in a RecyclerView.
     */
    public static class AppearanceConfigItem {
        private String name;
        private int icon;

        AppearanceConfigItem(String name, int icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }
    }
}