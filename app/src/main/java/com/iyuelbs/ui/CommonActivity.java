package com.iyuelbs.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.ui.login.LoginFragment;
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

            String type = bundle.getString(Keys.OPEN_TYPE);
            if (type.equals(Keys.OPEN_LOGIN)) {
                transaction.replace(R.id.common_container, LoginFragment.getInstance(bundle));
            } else if (type.equals(Keys.OPEN_REGISTER)) {
                transaction.replace(R.id.common_container, LoginFragment.getInstance(bundle));
            } else if (type.equals(Keys.OPEN_FILL_INFO)) {
                transaction.replace(R.id.common_container, new UserManager());
            } else if (type.equals(Keys.OPEN_AVATAR)) {
                transaction.replace(R.id.common_container, new AvatarFragment());
            }

            transaction.commit();
        }
    }

}
