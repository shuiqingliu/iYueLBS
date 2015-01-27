package com.iyuelbs.app;

import android.app.Application;

import com.iyuelbs.entity.User;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
        // 初始化BmobSDK
        initBmobSdk();
    }

    private void initBmobSdk() {
        Bmob.initialize(this, AppConfig.BMOB_AK);
        BmobInstallation.getCurrentInstallation(this).save();
        BmobPush.startWork(this, AppConfig.BMOB_AK); // 监听推送消息

        // 初始化用户信息
        mUser = BmobUser.getCurrentUser(this, User.class);
    }

    public static AppApplication getApplication() {
        return mAppApplication;
    }

    public User getCurrentUser() {
        return mUser;
    }

    public boolean isLogin() {
        return mUser != null;
    }
}
