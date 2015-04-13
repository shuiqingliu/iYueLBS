package com.iyuelbs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iyuelbs.support.event.EventBusBinder;
import com.iyuelbs.support.utils.ViewUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by Bob Peng on 2015/1/26.
 */
public class BaseFragment extends Fragment implements EventBusBinder {
    protected Context mContext;
    protected MaterialDialog mDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus();
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

    protected void showDialog() {
        showDialog(null);
    }

    /**
     * show a loading dialog with msg
     */
    protected void showDialog(String msg) {
        // TODO handle if dialog is showing.
        if (mDialog != null) {
            if (TextUtils.isEmpty(msg)) {
                msg = getString(R.string.msg_loading);
            }
            mDialog.setContent(msg);
            mDialog.show();
        } else {
            mDialog = ViewUtils.showLoadingDialog(mContext, msg);
        }
    }

    /**
     * dismiss current loading dialog
     */
    protected void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
