package com.iyuelbs.ui.settings;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.ui.login.RegQuickSettings;

/**
 * Created by Bob Peng on 2015/2/10.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void initView() {
        setContentView(R.layout.common_activity);
    }

    @Override
    protected void initFragments(Bundle data) {
        boolean quickSettings = data.getInt(Keys.EXTRA_OPEN_TYPE, 0) > 0;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.common_container, quickSettings ? new RegQuickSettings() : new
                SettingsFragment());
        transaction.commit();
    }
}