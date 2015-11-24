package com.iyuelbs.ui.chat.ui.profile;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;

import com.iyuelbs.R;
import com.iyuelbs.ui.chat.ui.base_activity.BaseActivity;


/**
 * Created by lzw on 14-9-24.
 */
public class ProfileNotifySettingActivity extends BaseActivity {
  private ProfileNotifySettingFragment profileNotifySettingFragment;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_setting_notify_layout);
    initActionBar(R.string.profile_notifySetting);
    profileNotifySettingFragment = new ProfileNotifySettingFragment();
    replaceFragment(R.id.settings_container,profileNotifySettingFragment);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public void replaceFragment(int viewId, android.app.Fragment fragment) {
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(viewId, fragment).commit();
  }

}
