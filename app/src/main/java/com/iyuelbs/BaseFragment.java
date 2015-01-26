package com.iyuelbs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Bob Peng on 2015/1/26.
 */
public class BaseFragment extends Fragment {
    protected Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
}
