package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.utils.ViewUtils;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void initView() {
        setContentView(R.layout.common_activity);
    }

    @Override
    protected void initFragments(Bundle bundle) {
        Fragment fragment;
        int step = bundle == null ? 0 : bundle.getInt(Keys.EXTRA_REGISTER_STEP);
        switch (step) {
            case Keys.REG_STEP_USER_DETAIL:
                fragment = new RegUserDetailFragment();
                break;
            case Keys.REG_STEP_USER_CONFIG:
                fragment = null; // RegQuickSettings is using app.Fragment not v4.app.Fragment
                getFragmentManager().beginTransaction().replace(R.id.common_container,
                        new RegQuickSettings()).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (AppHelper.checkLogin()) {
            // TODO 已注册，确认取消
            ViewUtils.showToast(this, "已注册，确认取消");
        } else {
            super.onBackPressed();
        }
    }
}