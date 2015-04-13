package com.iyuelbs.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Bob Peng on 2015/1/20.
 */
public class AppConfig {
    public static final String AVOS_APP_ID = "nbu2pmnfk3heomm0naqkrvaji848rd0xwfgs7vb55is5jkz1";
    public static final String AVOS_APP_KEY = "gwjh7evm1cwee3d1rf2j71l741li3f8dwel2n5q55ms3vf7r";
    public static final String WEIBO_ID = "2729685708";
    public static final String WEIBO_SECRET = "79f1d519042f2b1cf609970a876add5a";
    public static final String QQ_ID = "101196423";
    public static final String QQ_SECRET = "7acba16d8ef62ecd49ed20535c1d8de9";
    public static final String REDIRECT_URL = "dating.bmob.cn";
    public static final String REDIRECT_WEIBO_URL = "http://dating.bmob.cn/";
    public static final boolean TRANSLUCENT_BAR_ENABLED = true;

    public static SharedPreferences getPref() {
        return PreferenceManager.getDefaultSharedPreferences(AppHelper.getAppContext());
    }

    public static SharedPreferences.Editor getEditor() {
        return getPref().edit();
    }

    public static void putString(String key, String value) {
        getPref().edit().putString(key, value).apply();
    }

    public static void putInt(String key, int value) {
        getPref().edit().putInt(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        getPref().edit().putBoolean(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        return getPref().getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getPref().getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPref().getBoolean(key, defValue);
    }
}
