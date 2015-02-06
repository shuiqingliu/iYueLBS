package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity);
        initFragment();
    }

    private void initFragment() {
        Fragment fragment;
        Bundle bundle = getIntent().getExtras();
        int step = bundle == null ? 0 : bundle.getInt(Keys.EXTRA_REGISTER_STEP);
        switch (step) {
            case Keys.REG_STEP_USER_INTERFACE:
                fragment = null;
                break;
            case Keys.REG_STEP_USER_BRIEF:
                fragment = null;
                break;
            case Keys.REG_STEP_USER_CONFIG:
                fragment = null;
                break;
            default:
                fragment = new RegAccountFragment();
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.common_container,
                    fragment).commit();
        }
    }
}
