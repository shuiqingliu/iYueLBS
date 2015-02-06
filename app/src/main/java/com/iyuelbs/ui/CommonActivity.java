package com.iyuelbs.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.ui.login.LoginChooserFragment;
import com.iyuelbs.ui.login.LoginFragment;
import com.iyuelbs.ui.user.AvatarFragment;
import com.iyuelbs.ui.user.UserManager;

public class CommonActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity);
        initFragment();
    }

    private void initFragment() {
        Bundle bundle = getIntent().getExtras();
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
                case Keys.OPEN_FILL_INFO:
                    fragment = new UserManager();
                    break;
                case Keys.OPEN_AVATAR:
                    fragment = new AvatarFragment();
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
