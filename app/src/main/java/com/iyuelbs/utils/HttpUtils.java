package com.iyuelbs.utils;

import android.content.Context;

import com.bmob.BmobProFile;
import com.iyuelbs.app.AppConfig;

/**
 * Created by Bob Peng on 2015/1/30.
 */
public class HttpUtils {
    private static final String MAGIC = "xxxifan";

    public static String getSignedImageUrl(Context context, String filename, String url) {
        return BmobProFile.getInstance(context).signURL(filename, url, AppConfig.BMOB_AK, 100, MAGIC);
    }
}
