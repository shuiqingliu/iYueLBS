package com.iyuelbs.app;

import android.content.Context;
import android.graphics.Bitmap;

import com.bmob.BmobProFile;
import com.iyuelbs.R;
import com.iyuelbs.entity.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

/**
 * Created by Bob Peng on 2015/1/25.
 */
public class AppHelper {

    private static final String MAGIC = "xxxifan";

    public static Context getAppContext() {
        return getApplication();
    }

    public static AppApplication getApplication() {
        return AppApplication.getApplication();
    }

    public static User getCurrentUser() {
        return getApplication().getCurrentUser();
    }

    public static boolean checkLogin() {
        return getApplication().isLogin();
    }

    public static ImageLoader getImageLoader() {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getDefaultOpts() {
        return getDefaultOptsBuilder().build();
    }
    public static DisplayImageOptions.Builder getDefaultOptsBuilder() {
        //.showImageForEmptyUri(R.drawable.ic_empty)  resource or drawable
        //.showImageOnFail(R.drawable.ic_error)  resource or drawable
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(600))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.color.hint_dark);
    }

    public static String getCacheDirPath() {
        return getCacheDir().getPath();
    }

    public static File getCacheDir() {
        return getAppContext().getCacheDir();
    }

    public static String getCacheDirPath(String filename) {
        return getCacheDirPath() + File.separator + filename;
    }

    public static String signAvatar(Context context, String filename, String url) {
//        return BmobProFile.getInstance(context).signURL(filename, url, AppConfig.BMOB_AK, 100, MAGIC);
        return BmobProFile.getInstance(context).signURL(filename, url, AppConfig.BMOB_AK, 0, null);
    }
}
