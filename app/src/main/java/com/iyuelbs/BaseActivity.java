package com.iyuelbs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewStub;
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
    }

    /**
     * @param useToolbar true to use base toolbar layout
     */
    protected void setContentView(int resId, boolean useToolbar) {
        if (useToolbar) {
            setContentView(R.layout.base_activity);
            ViewStub stub = (ViewStub) findViewById(R.id.base_stub);
            stub.setLayoutResource(resId);
            stub.inflate();
        } else {
            setContentView(resId);
        }

        setupActionBar(getResources().getColor(R.color.teal));
        initView();
    }

    protected void setupWindowStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * config actionBar or systemBar style.
     * only effect if actionBar exists.
     */
    protected void setupActionBar(int themeColor) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundColor(themeColor);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(AppConfig.TRANSLUCENT_BAR_ENABLED);
            tintManager.setTintColor(themeColor);
        }
    }

    protected abstract void initView();

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

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Utils.onUpKeyClick(item.getItemId())) {
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
