package com.iyuelbs.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.ui.main.MainActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppHelper.checkLogin()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
}
