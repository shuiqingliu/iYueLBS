package com.iyuelbs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.iyuelbs.app.AppConfig;
import com.iyuelbs.external.SystemBarTintManager;

import java.util.List;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class BaseActivity extends ActionBarActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowStyle();
        super.onCreate(savedInstanceState);
        mContext = this;
        setupActionBar(getResources().getColor(R.color.teal));
    }

    /**
     * Before super.onCreate().
     */
    protected void setupWindowStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


    /**
     * config actionBar or systemBar style.
     */
    protected void setupActionBar(int themeColor) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                SystemBarTintManager tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(AppConfig.TRANSLUCENT_BAR_ENABLED);
                tintManager.setTintColor(themeColor);
            }
        }
    }

    protected void initFragments() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment fragment : fragmentList) {
                if (fragment.isVisible())
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
