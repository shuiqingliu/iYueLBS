package com.iyuelbs.app;

/**
 * Created by Bob Peng on 2015/1/24.
 */
public class Keys {
    /**
     * keys for requestCode that don't care who's return.
     */
    public static final int FOR_COMMON_RESULT = 100;

    public static final String EXTRA_OPEN_TYPE = "openType";
    public static final int OPEN_LOGIN = 2;
    public static final int OPEN_REGISTER = 3;
    public static final int OPEN_FILL_INFO = 4;
    public static final int OPEN_WEIBO_AUTH = 5;
    public static final int OPEN_QQ_AUTH = 6;
    public static final int OPEN_QUICK_SETTINGS = 7;
    /**
     * temp
     */
    public static final int OPEN_AVATAR = 0;

    public static final String EXTRA_REGISTER_STEP = "regStep";
    public static final int REG_STEP_USER_INTERFACE = 1;
    public static final int REG_STEP_USER_DETAIL = 2;
    public static final int REG_STEP_USER_CONFIG = 3;

    public static final int STYLE_COLOR_PRIMARY = 1;
    public static final int STYLE_COLOR_PRIMARY_DARK = 2;
    public static final int STYLE_COLOR_ACCENT = 3;

    public static final String EXTRA_URL = "url";
    public static final String FILE_TMP_AVATAR = "tmp_avatar";

    public static final String EXTRA_PHONE_NUMBER = "phoneNumber";
    public static final String EXTRA_PASSWORD = "password";
}
