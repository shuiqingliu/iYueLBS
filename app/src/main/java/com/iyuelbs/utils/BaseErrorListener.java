package com.iyuelbs.utils;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Bob Peng on 2015/1/31.
 */
public class BaseErrorListener implements Response.ErrorListener {
    private Context mContext;

    public BaseErrorListener(Context context) {
        mContext = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ViewUtils.showToast(mContext, error.getMessage());
    }
}
