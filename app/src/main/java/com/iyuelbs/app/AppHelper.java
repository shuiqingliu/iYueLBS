package com.iyuelbs.app;

import android.content.Context;

/**
 * Created by Bob Peng on 2015/1/25.
 */
public class AppHelper {

    public static Context getAppContext() {
        return getApplication();
    }

    public static AppApplication getApplication() {
        return AppApplication.getApplication();
    }
}
