package com.iyuelbs.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.ui.login.AuthLoginFragment;
import com.iyuelbs.ui.login.LoginChooserFragment;
import com.iyuelbs.ui.login.LoginFragment;
import com.iyuelbs.ui.user.AvatarFragment;

public class CommonActivity extends BaseActivity {

    @Override
    protected void initView() {
        setContentView(R.layout.common_activity);
    }

    @Override
    protected void initFragments(Bundle bundle) {
        if (bundle != null) {
            final Fragment fragment;
            int type = bundle.getInt(Keys.EXTRA_OPEN_TYPE);
            switch (type) {
                case Keys.OPEN_LOGIN:
                    fragment = new LoginFragment();
                    break;
                case Keys.OPEN_REGISTER:
                    fragment = LoginChooserFragment.getInstance(bundle);
                    break;
                case Keys.OPEN_AVATAR:
                    fragment = new AvatarFragment();
                    break;
                case Keys.OPEN_WEIBO_AUTH:
                    fragment = new AuthLoginFragment();
                    break;
                default:
                    fragment = null;
                    break;
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.common_container,
                        fragment).commit();
            }
        }
    }

}
