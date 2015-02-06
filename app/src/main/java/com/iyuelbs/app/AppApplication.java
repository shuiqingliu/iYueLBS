package com.iyuelbs.app;

import android.app.Application;

import com.iyuelbs.entity.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

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
        initBmobSdk();
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

    private void initBmobSdk() {
        Bmob.initialize(this, AppConfig.BMOB_APP_KEY);
        BmobInstallation.getCurrentInstallation(this).save();

        // 开启推送消息
        if (AppConfig.getBoolean(AppConfig.KEY_BMOB_PUSH_ENABLED, true)) {
            BmobPush.startWork(this, AppConfig.BMOB_APP_KEY);
        }

        // 初始化用户信息
        mUser = BmobUser.getCurrentUser(this, User.class);
    }

    public User getCurrentUser() {
        if (mUser == null)
            mUser = BmobUser.getCurrentUser(this, User.class);
        return mUser;
    }

    public void updateUser() {
        mUser = BmobUser.getCurrentUser(this, User.class);
    }

    public String getCurUserName() {
        return isLogin() ? mUser.getUsername() : null;
    }

    public boolean isLogin() {
        return mUser != null;
    }
}
