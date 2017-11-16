package com.example.android.wearable.wear.wearaccessibilityapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.wearable.activity.WearableActivity;

public class ControlsActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ControlsPrefFragment())
                .commit();
    }

    public static class ControlsPrefFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.prefs_controls);
        }
    }
}