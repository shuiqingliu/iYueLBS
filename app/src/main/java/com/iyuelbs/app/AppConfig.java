package com.iyuelbs.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Bob Peng on 2015/1/20.
 */
public class AppConfig {
    public static final String AVOS_APP_ID = "nbu2pmnfk3heomm0naqkrvaji848rd0xwfgs7vb55is5jkz1";
    public static final String AVOS_APP_KEY = "gwjh7evm1cwee3d1rf2j71l741li3f8dwel2n5q55ms3vf7r";
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
