package com.iyuelbs.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.iyuelbs.R;

/**
 * Created by Bob Peng on 2015/2/10.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

}
