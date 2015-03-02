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
import com.iyuelbs.support.event.EventBusBinder;
import com.iyuelbs.support.utils.Utils;
import com.iyuelbs.support.widget.SystemBarTintManager;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public abstract class BaseActivity extends ActionBarActivity implements EventBusBinder {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowStyle();
        super.onCreate(savedInstanceState);
        mContext = this;

        setupActionBar(getResources().getColor(R.color.teal));
        initView();
        initFragments(getIntent().getExtras());
    }

    protected void setupWindowStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


    /**
     * config actionBar or systemBar style.
     * only effect if actionBar exists.
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

    protected abstract void initView();

    protected abstract void initFragments(Bundle data);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null && fragment.isVisible()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Utils.onUpKeySelected(item.getItemId())) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void registerEventBus() {
    }

    @Override
    public void unregisterEventBus() {
    }

    @Override
    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

}
