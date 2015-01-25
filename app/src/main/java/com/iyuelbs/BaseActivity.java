package com.iyuelbs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class BaseActivity extends ActionBarActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
}
