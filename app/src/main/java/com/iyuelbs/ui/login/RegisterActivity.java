package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.event.RegisterEvent;
import com.iyuelbs.utils.Utils;
import com.iyuelbs.utils.ViewUtils;

public class RegisterActivity extends BaseActivity {

    private boolean mHasRegistered = false;
    private boolean mCallFromCallback = false;

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
            case Keys.REG_STEP_PHONE_VERIFY:
                fragment = RegPhoneVerify.newInstance(bundle);
                break;
            case Keys.REG_STEP_USER_CONFIG:
                getFragmentManager().beginTransaction().replace(R.id.common_container,
                        new RegQuickSettings()).commit();
                return;
            default:
                fragment = new RegAccountFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.common_container,
                fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Utils.onUpKeySelected(item.getItemId())) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        MaterialDialog.ButtonCallback callback = new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                mCallFromCallback = true;
                onBackPressed();
            }
        };

        if (!mCallFromCallback) {
            if (AppHelper.checkLogin()) {
                ViewUtils.showSimpleDialog(this, null,
                        getString(R.string.msg_register_login_cancel_confirm), callback);
            } else if (mHasRegistered) {
                ViewUtils.showSimpleDialog(this, null,
                        getString(R.string.msg_register_cancel_confirm), callback);
            }
        } else {
            super.onBackPressed();
        }
        mCallFromCallback = false;
    }

    public void onEvent(RegisterEvent event) {
        mHasRegistered = true;
    }
}
