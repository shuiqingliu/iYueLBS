package com.iyuelbs.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.iyuelbs.R;
import com.iyuelbs.entity.User;
import com.iyuelbs.external.SystemBarTintManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

import de.greenrobot.event.EventBus;

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

    /**
     * Update cached user.
     */
    public static User getUpdatedUser() {
        return getApplication().updateReferUser();
    }

    public static ImageLoader getImageLoader() {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getDefaultOption() {
        return getDefaultOptsBuilder().build();
    }

    public static DisplayImageOptions.Builder getDefaultOptsBuilder() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(600))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.color.hint_dark)
                .showImageForEmptyUri(R.drawable.ic_default_avatar)
                .showImageOnFail(R.drawable.ic_default_avatar);
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

//    public static String signAvatar(Context context, String filename, String url) {
//        return BmobProFile.getInstance(context).signURL(filename, url, AppConfig.BMOB_AK, 0, null);
//    }

    /**
     * set status bar color, better use it in setupWindowStyle() method
     */
    public static void setSystemBarSolidColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(AppConfig.TRANSLUCENT_BAR_ENABLED);
            tintManager.setTintColor(color);
        }
    }

    /**
     * Helper method for {@code EventBus.getDefault()}
     */
    public static void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }
}
