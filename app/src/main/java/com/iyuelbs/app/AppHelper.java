package com.iyuelbs.app;

import android.content.Context;

import com.iyuelbs.entity.User;

import java.io.File;

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

    public static User getCurrentUser() {
        return getApplication().getCurrentUser();
    }

    public static String getCacheDirPath() {
        return getCacheDir().getPath();
    }

    public static File getCacheDir() {
        return getAppContext().getExternalCacheDir();
    }

    public static String getCacheDirPath(String filename) {
        return getCacheDirPath() + File.separator + filename;
    }
}
