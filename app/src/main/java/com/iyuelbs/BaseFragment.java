package com.iyuelbs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import de.greenrobot.event.EventBus;

/**
 * Created by Bob Peng on 2015/1/26.
 */
public class BaseFragment extends Fragment {
    protected Context mContext;
    private boolean mIsEventBusEnabled = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        setHasOptionsMenu(true);
    }

    public void setEventBusEnabled() {
        mIsEventBusEnabled = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsEventBusEnabled) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mIsEventBusEnabled) {
            EventBus.getDefault().unregister(this);
        }
    }
}
