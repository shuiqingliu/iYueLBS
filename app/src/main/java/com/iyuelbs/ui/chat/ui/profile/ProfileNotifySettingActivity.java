package com.iyuelbs.ui.chat.ui.profile;

import android.os.Bundle;

import com.iyuelbs.R;
import com.iyuelbs.ui.chat.ui.base_activity.BaseActivity;


/**
 * Created by lzw on 14-9-24.
 */
public class ProfileNotifySettingActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_setting_notify_layout);
    initActionBar(R.string.profile_notifySetting);
  }

}
