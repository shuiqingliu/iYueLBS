package com.iyuelbs.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.iyuelbs.entity.Banner;
import com.iyuelbs.entity.Place;
import com.iyuelbs.entity.Tag;
import com.iyuelbs.entity.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class AppApplication extends Application {

    private static AppApplication mAppApplication;
    private User mUser;

    public static AppApplication getApplication() {
        return mAppApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;

        // 初始化
        initSdk();
        initImageLoader();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(AppHelper.getDefaultOption())
                .build();

        ImageLoader.getInstance().init(config);
    }

    private void initSdk() {
        AVObject.registerSubclass(Tag.class);
        AVObject.registerSubclass(Place.class);
        AVObject.registerSubclass(Banner.class);
        AVOSCloud.initialize(this, AppConfig.AVOS_APP_ID, AppConfig.AVOS_APP_KEY);
    }

    public User getCurrentUser() {
        if (mUser == null)
            mUser = User.getCurrentUser(User.class);
        return mUser;
    }

    public User updateReferUser() {
        mUser = null;
        return getCurrentUser();
    }

    public String getCurUserName() {
        return isLogin() ? mUser.getUsername() : null;
    }

    public boolean isLogin() {
        return mUser != null;
    }
}
