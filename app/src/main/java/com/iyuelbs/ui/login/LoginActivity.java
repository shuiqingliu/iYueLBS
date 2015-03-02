package com.iyuelbs.ui.login;

import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.support.event.LoginEvent;
import com.iyuelbs.support.utils.AVUtils;
import com.iyuelbs.support.utils.NavUtils;
import com.iyuelbs.support.utils.ViewUtils;
import com.iyuelbs.ui.main.MainActivity;

import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkUserLogin();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setupWindowStyle() {
        AppHelper.setSystemBarSolidColor(this, getResources().getColor(R.color.login_background));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.common_activity);
    }

    @Override
    protected void initFragments(Bundle data) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.common_container, new LoginFragment())
                .commit();
    }

    @Override
    public void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(LoginEvent event) {
        checkUserLogin();
    }

    private boolean checkUserLogin() {
        if (!AppHelper.checkLogin()) {
            return false;
        }
        if (!AVUtils.isUserInfoComplete(AppHelper.getUpdatedUser())) {
            ViewUtils.showSimpleDialog(this, null, getString(R.string.msg_register_cancel_confirm),
                    new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            // TODO UserInfo
                        }
                    });
            return false;
        }

        NavUtils.go(this, MainActivity.class);
        finish();
        return true;
    }

}
