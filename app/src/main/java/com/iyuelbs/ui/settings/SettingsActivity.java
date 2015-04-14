package com.iyuelbs.ui.settings;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.ui.login.RegisterQuickSettings;

/**
 * Created by Bob Peng on 2015/2/10.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity, true);
        initFragments(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    protected void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            boolean quickSettings = getIntent().getIntExtra(Keys.EXTRA_OPEN_TYPE, 0) > 0;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.common_container, quickSettings ? new RegisterQuickSettings() : new
                    SettingsFragment());
            transaction.commit();
        }
    }
}
