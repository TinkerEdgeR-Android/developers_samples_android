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

package com.example.android.wearable.watchface.config;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.complications.ComplicationHelperActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.wearable.watchface.R;
import com.example.android.wearable.watchface.models.ComplicationsSimpleWatchFaceSettingsConfigData.AppearanceConfigItem;
import com.example.android.wearable.watchface.models.ComplicationsSimpleWatchFaceSettingsConfigData.ComplicationsConfigItem;
import com.example.android.wearable.watchface.models.ComplicationsSimpleWatchFaceSettingsConfigData.MoreOptionsConfigItem;

import com.example.android.wearable.watchface.watchfaces.ComplicationSimpleWatchFaceService;

import java.util.ArrayList;

/**
 * Displays different layouts for configuring watch face's complications and appearance settings.
 * Class extends {@RecyclerView.Adapter} and updates {@ComplicationSimpleWatchFaceService}.
 * <p>
 * Layouts provided by this adapter are split into 2 main view types.
 * <p>
 * A layout representing the watch face along with complication locations. This allows the user
 * to tap on the area of the watch face where they want to change the complication data.
 * <p>
 * A layout representing other appearance configuration outside of the main complications on the
 * watch face. These could include marker color, background color, layout, unread notifications,
 * and many others. This sample is simplified, so it only includes marker color,
 * unread notifications, and background complication.
 */
public class ComplicationSimpleRecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CompConfigAdapter";

    private static final int TYPE_COMPLICATIONS_CONFIG = 0;
    private static final int TYPE_MORE_OPTIONS_CONFIG = 1;
    private static final int TYPE_APPEARANCE_CONFIG = 2;

    private ArrayList<Object> mSettingsDataSet;

    public ComplicationSimpleRecyclerViewAdapter(ArrayList<Object> settingsDataSet) {
        mSettingsDataSet = settingsDataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder(): viewType: " + viewType);

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case TYPE_COMPLICATIONS_CONFIG:
                viewHolder = new ComplicationsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.config_list_complication_item, parent, false));
                break;

            case TYPE_MORE_OPTIONS_CONFIG:
                viewHolder = new MoreOptionsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.config_list_more_options_item, parent, false));
                break;

            case TYPE_APPEARANCE_CONFIG:
                viewHolder = new AppearanceViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.config_list_appearance_item, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Pulls all data for settings options.
        Object configItem = mSettingsDataSet.get(position);

        switch (viewHolder.getItemViewType()) {

            case TYPE_COMPLICATIONS_CONFIG:
                ComplicationsViewHolder complicationsViewHolder =
                        (ComplicationsViewHolder) viewHolder;

                ComplicationsConfigItem complicationsConfigItem =
                        (ComplicationsConfigItem) configItem;

                int watchFaceInt = complicationsConfigItem.getWatchFaceDrawable();

                complicationsViewHolder.setWatchFacePreviewImageView(watchFaceInt);
                break;

            case TYPE_MORE_OPTIONS_CONFIG:
                MoreOptionsViewHolder moreOptionsViewHolder = (MoreOptionsViewHolder) viewHolder;

                MoreOptionsConfigItem moreOptionsConfigItem = (MoreOptionsConfigItem) configItem;

                moreOptionsViewHolder.setIcon(moreOptionsConfigItem.getDrawable());
                break;


            case TYPE_APPEARANCE_CONFIG:
                AppearanceViewHolder appearanceViewHolder = (AppearanceViewHolder) viewHolder;

                AppearanceConfigItem appearanceConfigItem = (AppearanceConfigItem) configItem;

                int iconInt = appearanceConfigItem.getIcon();
                String name = appearanceConfigItem.getName();

                appearanceViewHolder.setName(name);
                appearanceViewHolder.setIcon(iconInt);
                break;
        }
    }



    @Override
    public int getItemViewType(int position) {

        if (mSettingsDataSet.get(position)
                instanceof ComplicationsConfigItem) {
            return TYPE_COMPLICATIONS_CONFIG;

        } else if (mSettingsDataSet.get(position)
                instanceof MoreOptionsConfigItem) {
            return TYPE_MORE_OPTIONS_CONFIG;

        } else if (mSettingsDataSet.get(position)
                instanceof AppearanceConfigItem) {
            return TYPE_APPEARANCE_CONFIG;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mSettingsDataSet.size();
    }

    /**
     * Layout displays watch face preview along with complication locations allowing the user to
     * tap on the complication they want to change.
     */
    public class ComplicationsViewHolder extends RecyclerView.ViewHolder
            implements View.OnTouchListener {

        private static final int SMALL_OFFSET = 10;
        private static final int LARGE_OFFSET = 100;
        private static final int TAPPABLE_SPACE = 100;

        private Rect mLeftComplicationRect;
        private Rect mRightComplicationRect;
        private Rect mTopComplicationRect;
        private Rect mBottomComplicationRect;

        private ImageView mWatchFacePreviewImageView;

        public ComplicationsViewHolder(final View view) {
            super(view);
            mWatchFacePreviewImageView = (ImageView) view.findViewById(R.id.watch_face_preview);

            view.setOnTouchListener(this);

            // Used to get dimensions of view once it is inflated to calculate locations of
            // complications.
            view.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override public void onGlobalLayout() {
                            // Calculates tappable space for each complication location.
                            // Constructor: Rect(int left, int top, int right, int bottom).
                            mLeftComplicationRect =
                                    new Rect(
                                            SMALL_OFFSET,
                                            LARGE_OFFSET,
                                            SMALL_OFFSET + TAPPABLE_SPACE,
                                            LARGE_OFFSET + TAPPABLE_SPACE);

                            mRightComplicationRect =
                                    new Rect(
                                            view.getWidth() - SMALL_OFFSET - TAPPABLE_SPACE,
                                            LARGE_OFFSET,
                                            view.getWidth() - SMALL_OFFSET,
                                            LARGE_OFFSET + TAPPABLE_SPACE);

                            mTopComplicationRect =
                                    new Rect(
                                            LARGE_OFFSET,
                                            SMALL_OFFSET,
                                            LARGE_OFFSET + TAPPABLE_SPACE,
                                            SMALL_OFFSET + TAPPABLE_SPACE);

                            mBottomComplicationRect =
                                    new Rect(
                                            LARGE_OFFSET,
                                            view.getHeight() - SMALL_OFFSET - TAPPABLE_SPACE,
                                            LARGE_OFFSET + TAPPABLE_SPACE,
                                            view.getHeight() - SMALL_OFFSET);
                }
            });
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_UP) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                if (mLeftComplicationRect.contains(x, y)) {
                    Log.d(TAG, "LEFT Complication: x, y: " + x + ", " + y);

                    Activity currentActivity = (Activity) view.getContext();
                    launchComplicationHelperActivity(
                            currentActivity,
                            ComplicationSimpleWatchFaceService.ComplicationLocation.LEFT);

                } else if (mRightComplicationRect.contains(x, y)) {
                    Log.d(TAG, "RIGHT Complication: x, y: " + x + ", " + y);

                    Activity currentActivity = (Activity) view.getContext();
                    launchComplicationHelperActivity(
                            currentActivity,
                            ComplicationSimpleWatchFaceService.ComplicationLocation.RIGHT);

                } else if (mTopComplicationRect.contains(x, y)) {
                    Log.d(TAG, "TOP Complication: x, y: " + x + ", " + y);

                    Activity currentActivity = (Activity) view.getContext();
                    launchComplicationHelperActivity(
                            currentActivity,
                            ComplicationSimpleWatchFaceService.ComplicationLocation.TOP);

                } else if (mBottomComplicationRect.contains(x, y)) {
                    Log.d(TAG, "BOTTOM Complication: x, y: " + x + ", " + y);

                    Activity currentActivity = (Activity) view.getContext();
                    launchComplicationHelperActivity(
                            currentActivity,
                            ComplicationSimpleWatchFaceService.ComplicationLocation.BOTTOM);
                }
            }
            return true;
        }

        // Verifies the watch face supports the complication location, then launches the helper
        // class, so user can choose their complication data provider.
        private void launchComplicationHelperActivity(
                Activity currentActivity,
                ComplicationSimpleWatchFaceService.ComplicationLocation complicationLocation) {

            int complicationId =
                    ComplicationSimpleWatchFaceService.getComplicationId(complicationLocation);

            if (complicationId >= 0) {

                int[] supportedTypes = ComplicationSimpleWatchFaceService
                        .getSupportedComplicationTypes(complicationLocation);

                if (supportedTypes.length > 0) {

                    ComponentName watchFace = new ComponentName(
                            currentActivity,
                            ComplicationSimpleWatchFaceService.class);

                    currentActivity.startActivityForResult(
                            ComplicationHelperActivity.createProviderChooserHelperIntent(
                                    currentActivity,
                                    watchFace,
                                    complicationId,
                                    supportedTypes),
                            ComplicationSimpleConfigActivity.COMPLICATION_CONFIG_REQUEST_CODE);
                } else {
                    Log.d(TAG, "Complication has no supported types.");
                }

            } else {
                Log.d(TAG, "Complication not supported by watch face.");
            }
        }

        public void setWatchFacePreviewImageView(int resourceId) {
            Context context = mWatchFacePreviewImageView.getContext();
            mWatchFacePreviewImageView.setImageDrawable(context.getDrawable(resourceId));
        }
    }

    /**
     * Layout displays all other non-complication appearance configuration options for the
     * watch face. These could include marker color, background color, layout, unread notifications,
     * and many others.
     */
    public class MoreOptionsViewHolder extends RecyclerView.ViewHolder {

        private ImageView mMoreOptionsImageView;

        public MoreOptionsViewHolder(View view) {
            super(view);
            mMoreOptionsImageView = (ImageView) view.findViewById(R.id.more_options_image_view);
        }

        public void setIcon(int resourceId) {
            Context context = mMoreOptionsImageView.getContext();
            mMoreOptionsImageView.setImageDrawable(context.getDrawable(resourceId));
        }
    }

    /**
     * Layout displays all other non-complication appearance configuration options for the
     * watch face. These could include marker color, background color, layout, unread notifications,
     * and many others.
     */
    public class AppearanceViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Button mAppearanceButton;

        public AppearanceViewHolder(View view) {
            super(view);

            mAppearanceButton = (Button) view.findViewById(R.id.appearance_button);
            view.setOnClickListener(this);
        }

        public void setName(String name) {
            mAppearanceButton.setText(name);
        }

        public void setIcon(int resourceId) {
            Context context = mAppearanceButton.getContext();
            mAppearanceButton.setCompoundDrawablesWithIntrinsicBounds(
                    context.getDrawable(resourceId),
                    null,
                    null,
                    null);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "Complication onClick() position: " + position);

            // TODO (jewalker): Launch Activity to select appearance.
        }
    }

    // TODO(jewalker): Add custom RecyclerView.ViewHolder for unread Notification toggle.
    // TODO(jewalker): Add custom RecyclerView.ViewHolder for background image complication.
}