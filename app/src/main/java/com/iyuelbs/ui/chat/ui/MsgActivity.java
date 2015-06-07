package com.iyuelbs.ui.chat.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVUser;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.iyuelbs.R;
import com.iyuelbs.ui.chat.service.CacheService;
import com.iyuelbs.ui.chat.service.event.LoginFinishEvent;
import com.iyuelbs.ui.chat.ui.base_activity.BaseActivity;
import com.iyuelbs.ui.chat.ui.chat.ChatRoomActivity;
import com.iyuelbs.ui.chat.ui.contact.ContactFragment;
import com.iyuelbs.ui.chat.ui.conversation.ConversationRecentFragment;
import com.iyuelbs.ui.chat.ui.profile.ProfileFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by lzw on 14-9-17.
 */
public class MsgActivity extends BaseActivity {
  public static final int FRAGMENT_N = 3;
  public static final int[] tabsNormalBackIds = new int[]{R.drawable.tabbar_chat,
      R.drawable.tabbar_contacts,  R.drawable.tabbar_me};
  public static final int[] tabsActiveBackIds = new int[]{R.drawable.tabbar_chat_active,
      R.drawable.tabbar_contacts_active, R.drawable.tabbar_me_active};
  private static final String
          FRAGMENT_TAG_CONVERSATION = "conversation";
  private static final String FRAGMENT_TAG_CONTACT = "contact";
  private static final String FRAGMENT_TAG_PROFILE = "profile";
  private static final String[] fragmentTags = new String[]{FRAGMENT_TAG_CONVERSATION, FRAGMENT_TAG_CONTACT, FRAGMENT_TAG_PROFILE};


  Button conversationBtn, contactBtn,  mySpaceBtn;
  View fragmentContainer;
  ContactFragment contactFragment;
  ConversationRecentFragment conversationRecentFragment;
  ProfileFragment profileFragment;
  Button[] tabs;
  View recentTips, contactTips;

  public static void goMainActivityFromActivity(Activity fromActivity) {
    EventBus eventBus = EventBus.getDefault();
    eventBus.post(new LoginFinishEvent());

    ChatManager chatManager = ChatManager.getInstance();
    chatManager.setupDatabaseWithSelfId(AVUser.getCurrentUser().getObjectId());
    chatManager.openClientWithSelfId(AVUser.getCurrentUser().getObjectId(), null);
    Intent intent = new Intent(fromActivity, MsgActivity.class);
    fromActivity.startActivity(intent);
  }

  public static void goChatActivityFromActivity(Activity fromActivity,String userID) {
    ChatManager chatManager = ChatManager.getInstance();
    chatManager.setupDatabaseWithSelfId(AVUser.getCurrentUser().getObjectId());
    chatManager.openClientWithSelfId(AVUser.getCurrentUser().getObjectId(), null);
    ChatRoomActivity.chatByUserId(fromActivity, userID);
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.msg_activity);
    findView();
    init();

    conversationBtn.performClick();
    CacheService.registerUser(AVUser.getCurrentUser());
  }



  private void init() {
    tabs = new Button[]{conversationBtn, contactBtn, mySpaceBtn};
  }

  private void findView() {
    conversationBtn = (Button) findViewById(R.id.btn_message);
    contactBtn = (Button) findViewById(R.id.btn_contact);
    mySpaceBtn = (Button) findViewById(R.id.btn_my_space);
    fragmentContainer = findViewById(R.id.fragment_container);
    recentTips = findViewById(R.id.iv_recent_tips);
    contactTips = findViewById(R.id.iv_contact_tips);
  }

  public void onTabSelect(View v) {
    int id = v.getId();
    FragmentManager manager = getFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    hideFragments(manager, transaction);
    setNormalBackgrounds();
    if (id == R.id.btn_message) {
      if (conversationRecentFragment == null) {
        conversationRecentFragment = new ConversationRecentFragment();
        transaction.add(R.id.fragment_container, conversationRecentFragment, FRAGMENT_TAG_CONVERSATION);
      }
      transaction.show(conversationRecentFragment);
    } else if (id == R.id.btn_contact) {
      if (contactFragment == null) {
        contactFragment = new ContactFragment();
        transaction.add(R.id.fragment_container, contactFragment, FRAGMENT_TAG_CONTACT);
      }
      transaction.show(contactFragment);
    } else if (id == R.id.btn_my_space) {
      if (profileFragment == null) {
        profileFragment = new ProfileFragment();
        transaction.add(R.id.fragment_container, profileFragment, FRAGMENT_TAG_PROFILE);
      }
      transaction.show(profileFragment);
    }
    int pos;
    for (pos = 0; pos < FRAGMENT_N; pos++) {
      if (tabs[pos] == v) {
        break;
      }
    }
    transaction.commit();
    setTopDrawable(tabs[pos], tabsActiveBackIds[pos]);
  }

  private void setNormalBackgrounds() {
    for (int i = 0; i < tabs.length; i++) {
      Button v = tabs[i];
      setTopDrawable(v, tabsNormalBackIds[i]);
    }
  }

  private void setTopDrawable(Button v, int resId) {
    v.setCompoundDrawablesWithIntrinsicBounds(null, ctx.getResources().getDrawable(resId), null, null);
  }

  private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
    for (int i = 0; i < fragmentTags.length; i++) {
      Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags[i]);
      if (fragment != null && fragment.isVisible()) {
        transaction.hide(fragment);
      }
    }
  }

}
