package com.iyuelbs.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.ui.login.AuthLoginFragment;
import com.iyuelbs.ui.user.AvatarFragment;

public class CommonActivity extends BaseActivity {

    @Override
    protected void initView() {
        setContentView(R.layout.common_activity);
    }

    @Override
    protected void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null && getIntent() != null) {
            final Fragment fragment;
            int type = getIntent().getIntExtra(Keys.EXTRA_OPEN_TYPE, -1);
            switch (type) {
                case Keys.OPEN_AVATAR:
                    fragment = new AvatarFragment();
                    break;
                case Keys.OPEN_WEIBO_AUTH:
                    fragment = new AuthLoginFragment();
                    break;
                case Keys.OPEN_QQ_AUTH:
                    fragment = AuthLoginFragment.newInstance(getIntent().getExtras());
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
