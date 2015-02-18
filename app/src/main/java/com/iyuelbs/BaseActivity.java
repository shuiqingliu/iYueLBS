package com.iyuelbs;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iyuelbs.app.AppConfig;
import com.iyuelbs.event.DialogEvent;
import com.iyuelbs.external.SystemBarTintManager;
import com.iyuelbs.utils.ViewUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public abstract class BaseActivity extends ActionBarActivity {
    protected Context mContext;
    protected MaterialDialog mLoadingDialog;

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
                if (fragment != null && fragment.isVisible())
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    protected void showDialog(String msg) {
        // TODO handle if dialog is showing.
        if (mLoadingDialog != null) {
            if (TextUtils.isEmpty(msg)) {
                msg = getString(R.string.msg_loading);
            }
            mLoadingDialog.setContent(msg);
            mLoadingDialog.show();
        } else {
            mLoadingDialog = ViewUtils.createLoadingDialog(this, msg);
        }
    }

    protected void dismissDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void onEventMainThread(DialogEvent event) {
        if (event.msg == null) {
            dismissDialog();
            Log.e("xifan", "dismissDialog");
        } else {
            showDialog(event.msg);
            Log.e("xifan", "showDialog");
        }
    }
}
