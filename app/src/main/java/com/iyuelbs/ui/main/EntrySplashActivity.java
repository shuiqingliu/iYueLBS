package com.iyuelbs.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVUser;
import com.iyuelbs.R;
import com.iyuelbs.ui.chat.service.UserService;
import com.iyuelbs.ui.chat.ui.MsgActivity;
import com.iyuelbs.ui.chat.ui.base_activity.BaseActivity;
import com.iyuelbs.ui.chat.util.Utils;
import com.iyuelbs.ui.login.LoginActivity;

public class EntrySplashActivity extends BaseActivity {
  public static final int SPLASH_DURATION = 2000;
  private static final int GO_MAIN_MSG = 1;
  private static final int GO_LOGIN_MSG = 2;

  private  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case GO_MAIN_MSG:
          MsgActivity.goActivityFromActivity(EntrySplashActivity.this);
          finish();
          break;
        case GO_LOGIN_MSG:
          Utils.goActivity(ctx, LoginActivity.class);
          finish();
          break;
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.entry_splash_layout);
    if (AVUser.getCurrentUser() != null) {
      UserService.updateUserInfo();
      handler.sendEmptyMessageDelayed(GO_MAIN_MSG, SPLASH_DURATION);
    } else {
      handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
    }
  }
}
