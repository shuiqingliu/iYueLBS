package com.iyuelbs.ui.chat.ui.conversation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avoscloud.leanchatlib.activity.ChatActivity;
import com.avoscloud.leanchatlib.controller.ConversationHelper;
import com.avoscloud.leanchatlib.db.RoomsTable;
import com.avoscloud.leanchatlib.model.ConversationType;
import com.avoscloud.leanchatlib.view.ViewHolder;
import com.iyuelbs.R;
import com.iyuelbs.app.AppApplication;
import com.iyuelbs.ui.chat.service.CacheService;
import com.iyuelbs.ui.chat.service.ConversationManager;
import com.iyuelbs.ui.chat.service.UserService;
import com.iyuelbs.ui.chat.ui.base_activity.UpdateContentActivity;
import com.iyuelbs.ui.chat.ui.contact.ContactPersonInfoActivity;
import com.iyuelbs.ui.chat.ui.view.BaseListAdapter;
import com.iyuelbs.ui.chat.ui.view.ExpandGridView;
import com.iyuelbs.ui.chat.util.SimpleNetTask;
import com.iyuelbs.ui.chat.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lzw on 14-10-11.
 */
public class ConversationDetailActivity extends ConversationBaseActivity implements AdapterView.OnItemClickListener,
    AdapterView.OnItemLongClickListener {
  private static final int ADD_MEMBERS = 0;
  private static final int INTENT_NAME = 0;
  private static List<AVUser> members = new ArrayList<AVUser>();
  @BindView(R.id.usersGrid)
  ExpandGridView usersGrid;

  @BindView(R.id.name_layout)
  View nameLayout;

  @BindView(R.id.quit_layout)
  View quitLayout;

  private ConversationType conversationType;
  private ConversationManager conversationManager;
  private UserListAdapter usersAdapter;
  private boolean isOwner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_detail_activity);
    ButterKnife.bind(this);
    initData();
    initGrid();
    initActionBar(R.string.conversation_detail_title);
    setViewByConvType(conversationType);
    refresh();
  }

  private void setViewByConvType(ConversationType conversationType) {
    if (conversationType == ConversationType.Single) {
      nameLayout.setVisibility(View.GONE);
      quitLayout.setVisibility(View.GONE);
    } else {
      nameLayout.setVisibility(View.VISIBLE);
      quitLayout.setVisibility(View.VISIBLE);
    }
  }

  @Override
  protected void onConvChanged(AVIMConversation conv) {
    refresh();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem invite = menu.add(0, ADD_MEMBERS, 0, R.string.conversation_detail_invite);
    alwaysShowMenuItem(invite);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    int menuId = item.getItemId();
    if (menuId == ADD_MEMBERS) {
      Utils.goActivity(ctx, ConversationAddMembersActivity.class);
    }
    return super.onMenuItemSelected(featureId, item);
  }

  private void refresh() {
    new SimpleNetTask(ctx) {
      List<AVUser> subMembers = new ArrayList<AVUser>();

      @Override
      protected void doInBack() throws Exception {
        List<AVUser> users = CacheService.findUsers(conv().getMembers());
        CacheService.registerUsers(users);
        subMembers = users;
      }

      @Override
      protected void onSucceed() {
        usersAdapter.clear();
        usersAdapter.addAll(subMembers);
      }
    }.execute();
  }

  private void initGrid() {
    usersAdapter = new UserListAdapter(ctx, members);
    usersGrid.setAdapter(usersAdapter);
    usersGrid.setOnItemClickListener(this);
    usersGrid.setOnItemLongClickListener(this);
  }

  private void initData() {
    conversationManager = ConversationManager.getInstance();
    isOwner = conv().getCreator().equals(AVUser.getCurrentUser().getObjectId());
    conversationType = ConversationHelper.typeOfConversation(conv());
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    AVUser user = (AVUser) parent.getAdapter().getItem(position);
    ContactPersonInfoActivity.goPersonInfo(ctx, user.getObjectId());
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    if (conversationType == ConversationType.Single) {
      return true;
    }
    final AVUser user = (AVUser) parent.getAdapter().getItem(position);
    boolean isTheOwner = conv().getCreator().equals(user.getObjectId());
    if (!isTheOwner) {
      new AlertDialog.Builder(ctx).setMessage(R.string.conversation_kickTips)
          .setPositiveButton(R.string.common_sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              final ProgressDialog progress = showSpinnerDialog();
              conv().kickMembers(Arrays.asList(user.getObjectId()), new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                  progress.dismiss();
                  if (filterException(e)) {
                    Utils.toast(R.string.conversation_detail_kickSucceed);
                  }
                }
              });
            }
          }).setNegativeButton(R.string.chat_common_cancel, null).show();
    }
    return true;
  }

  @OnClick(R.id.name_layout)
  void changeName() {
    UpdateContentActivity.goActivityForResult(this, AppApplication.getApplication().getString(R.string.conversation_name), INTENT_NAME);
  }

  @OnClick(R.id.quit_layout)
  void quit() {
    final String convid = conv().getConversationId();
    conv().quit(new AVIMConversationCallback() {
      @Override
      public void done(AVIMException e) {
        if (filterException(e)) {
          RoomsTable roomsTable = RoomsTable.getCurrentUserInstance();
          roomsTable.deleteRoom(convid);
          Utils.toast(R.string.conversation_alreadyQuitConv);
          ConversationDetailActivity.this.finish();

          if (ChatActivity.getChatInstance() != null) {
            ChatActivity.getChatInstance().finish();
          }
        }
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      if (requestCode == INTENT_NAME) {
        String newName = UpdateContentActivity.getResultValue(data);
        conversationManager.updateName(conv(), newName, new AVIMConversationCallback() {
          @Override
          public void done(AVIMException e) {
            if (filterException(e)) {
              ConversationDetailActivity.this.refresh();
            }
          }
        });
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  public static class UserListAdapter extends BaseListAdapter<AVUser> {
    public UserListAdapter(Context ctx, List<AVUser> datas) {
      super(ctx, datas);
    }

    @Override
    public View getView(int position, View conView, ViewGroup parent) {
      if (conView == null) {
        conView = View.inflate(ctx, R.layout.conversation_member_item, null);
      }
      AVUser user = datas.get(position);
      ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatar);
      TextView nameView = ViewHolder.findViewById(conView, R.id.username);
      UserService.displayAvatar(user, avatarView);
      nameView.setText(user.getUsername());
      return conView;
    }
  }
}
