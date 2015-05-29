package com.iyuelbs.app;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ChatManagerAdapter;
import com.avoscloud.leanchatlib.model.UserInfo;
import com.baidu.mapapi.SDKInitializer;
import com.iyuelbs.entity.Banner;
import com.iyuelbs.entity.Place;
import com.iyuelbs.entity.Tag;
import com.iyuelbs.entity.User;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class AppApplication extends Application {

    private static AppApplication mAppApplication;
    private AsyncHttpClient mHttpClient;
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
        initChat();
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
        AVInstallation.getCurrentInstallation().saveInBackground();
        updateCurUser();

        SDKInitializer.initialize(this);
    }

    private void initChat(){
        ChatManager.setDebugEnabled(true);// tag leanchatlib
        AVOSCloud.setDebugLogEnabled(true);  // set false when release
        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        chatManager.setChatManagerAdapter(new ChatManagerAdapter() {
            @Override
            public UserInfo getUserInfoById(String userId) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(userId);
                userInfo.setAvatarUrl("http://ac-x3o016bx.clouddn.com/86O7RAPx2BtTW5zgZTPGNwH9RZD5vNDtPm1YbIcu");
                return userInfo;
            }

            @Override
            public void cacheUserInfoByIdsInBackground(List<String> userIds) throws Exception {

            }

            @Override
            public void shouldShowNotification(Context context, String selfId, AVIMConversation conversation, AVIMTypedMessage message) {
                Toast.makeText(context, "收到了一条消息但并未打开相应的对话。可以触发系统通知。", Toast.LENGTH_LONG).show();
            }
        });
    }

    public User getCurUser() {
        if (mUser == null)
            mUser = User.getCurrentUser(User.class);
        return mUser;
    }

    public User updateCurUser() {
        return mUser = User.getCurrentUser(User.class);
    }

    public String getCurUserName() {
        return isLogin() ? mUser.getUsername() : null;
    }

    public boolean isLogin() {
        return mUser != null;
    }

    public void logoff() {
        if (updateCurUser() != null) {
            User.logOut();
            mUser = null;
        }
    }

    public AsyncHttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new AsyncHttpClient();
        }
        return mHttpClient;
    }

}
