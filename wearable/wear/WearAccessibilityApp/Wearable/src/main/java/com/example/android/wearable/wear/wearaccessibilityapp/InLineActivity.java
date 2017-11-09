package com.example.android.wearable.wear.wearaccessibilityapp;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.wearable.activity.WearableActivity;

public class InLineActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new InLinePrefFragment())
                .commit();
    }

    public static class InLinePrefFragment extends PreferenceFragment {

        private SwitchPreference mDeterminantSwitchPref;
        private CircledImageViewPreference mCircledImageViewPref;
        private ProgressBarPreference mProgressBarPreference;
        private PreferenceScreen mPreferenceScreen;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.prefs_in_line_progress);

            mDeterminantSwitchPref = (SwitchPreference) findPreference(
                    getString(R.string.key_pref_determinant_switch));
            mDeterminantSwitchPref.setChecked(true);

            mCircledImageViewPref = (CircledImageViewPreference)
                    findPreference(getString(R.string.key_pref_circled_image_view));

            mPreferenceScreen = (PreferenceScreen) findPreference(
                    getString(R.string.key_pref_progress_screen));

            mProgressBarPreference = new ProgressBarPreference(getContext());
            mProgressBarPreference.setTitle("@string/indeterminant_progress");

            mDeterminantSwitchPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    mDeterminantSwitchPref.setChecked(!mDeterminantSwitchPref.isChecked());
                    if (mDeterminantSwitchPref.isChecked()) {
                        mCircledImageViewPref.cancelCountDownTimer();
                        mCircledImageViewPref.setStartCircledImageView();
                        mPreferenceScreen.removePreference(mProgressBarPreference);
                        mPreferenceScreen.addPreference(mCircledImageViewPref);
                    } else {
                        mPreferenceScreen.removePreference(mCircledImageViewPref);
                        mPreferenceScreen.addPreference(mProgressBarPreference);
                    }
                    return true;
                }
            });
        }
    }
}