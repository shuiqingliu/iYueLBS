package com.iyuelbs.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Bob Peng on 2015/1/20.
 */
public class AppConfig {
    public static final String BMOB_APP_KEY = "b03b3ba6c4c585e57496bd3a1dd00a23";
    public static final String BMOB_REST_KEY = "6046cbcd9eb678cc99e298bed5a53c08";
    public static final String BMOB_AK = "e906767109cb49cc6400a4ec510cba36";
    public static final String WEIBO_KEY = "2729685708";
    public static final String WEIBO_SECRET = "79f1d519042f2b1cf609970a876add5a";
    public static final String REDIRECT_URL = "http://dating.bmob.cn/";
    public static final boolean TRANSLUCENT_BAR_ENABLED = true;

    public static final String KEY_BMOB_PUSH_ENABLED = "bmob_push_enabled";

    private static final String PREF_NAME = "preferences";

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
