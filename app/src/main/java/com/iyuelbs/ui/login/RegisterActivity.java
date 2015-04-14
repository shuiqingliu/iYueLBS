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
import com.iyuelbs.support.event.RegisterEvent;
import com.iyuelbs.support.utils.Utils;
import com.iyuelbs.support.utils.ViewUtils;

import de.greenrobot.event.EventBus;

public class RegisterActivity extends BaseActivity {

    private boolean mHasRegistered = false;
    private boolean mIsCallback = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_activity, false);
        initFragments(savedInstanceState);
    }

    @Override
    protected void initView() {
    }

    protected void initFragments(Bundle savedInstanceState) {
        Fragment fragment;
        int step = getIntent() == null ? 0 : getIntent().getIntExtra(Keys.EXTRA_REGISTER_STEP, -1);
        switch (step) {
            case Keys.REG_STEP_USER_DETAIL:
                fragment = new RegisterUserDetail();
                break;
            case Keys.REG_STEP_PHONE_VERIFY:
                fragment = RegisterPhoneVerify.newInstance(getIntent().getExtras());
                break;
            case Keys.REG_STEP_USER_CONFIG:
                getFragmentManager().beginTransaction().replace(R.id.common_container,
                        new RegisterQuickSettings()).commit();
                return;
            default:
                fragment = new RegisterAccount();
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
        if (Utils.onUpKeyClick(item.getItemId())) {
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
                mIsCallback = true;
                onBackPressed();
            }
        };

        if (!mIsCallback) {
            if (AppHelper.checkLogin()) {
                ViewUtils.showSimpleDialog(this, null,
                        getString(R.string.msg_register_login_cancel_confirm), callback);
                return;
            } else if (mHasRegistered) {
                ViewUtils.showSimpleDialog(this, null,
                        getString(R.string.msg_register_cancel_confirm), callback);
                return;
            }
        }

        super.onBackPressed();
        mIsCallback = false;
    }

    @Override
    public void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(RegisterEvent event) {
        mHasRegistered = true;
    }
}
