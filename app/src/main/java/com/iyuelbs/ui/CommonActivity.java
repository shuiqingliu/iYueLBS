package com.iyuelbs.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.ui.login.LoginFragment;
import com.iyuelbs.ui.login.OpenAuthFragment;
import com.iyuelbs.ui.user.AvatarFragment;
import com.iyuelbs.ui.user.UserManager;

public class CommonActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        initFragment();
    }

    private void initFragment() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            int type = bundle.getInt(Keys.EXTRA_OPEN_TYPE);
            if (type == Keys.OPEN_LOGIN) {
                transaction.replace(R.id.common_container, LoginFragment.getInstance(bundle));
            } else if (type == Keys.OPEN_REGISTER) {
                transaction.replace(R.id.common_container, LoginFragment.getInstance(bundle));
            } else if (type == Keys.OPEN_FILL_INFO) {
                transaction.replace(R.id.common_container, new UserManager());
            } else if (type == Keys.OPEN_AVATAR) {
                transaction.replace(R.id.common_container, new AvatarFragment());
            } else if (type == Keys.OPEN_WEIBO_AUTH || type == Keys.OPEN_QQ_AUTH) {
                transaction.replace(R.id.common_container, OpenAuthFragment.getInstance(bundle));
            }

            transaction.commit();
        }
    }

}
