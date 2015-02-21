package com.iyuelbs.ui.login;

import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.event.LoginEvent;
import com.iyuelbs.ui.main.MainActivity;
import com.iyuelbs.utils.AVUtils;
import com.iyuelbs.utils.ViewUtils;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (checkUserState()) {
            MainActivity.go(this);
            finish();
        }

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

    public void onEventMainThread(LoginEvent event) {
        if (checkUserState()) {
            MainActivity.go(this);
            finish();
        }
    }

    private boolean checkUserState() {
        if (!AppHelper.checkLogin()) {
            return false;
        }
        if (!AVUtils.isUserInfoComplete(AppHelper.getUpdatedUser())) {
            ViewUtils.showSimpleDialog(this, null, getString(R.string.msg_user_info_not_complete),
                    new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            // TODO UserInfo
                        }
                    });
            return false;
        }
        return true;
    }
}
