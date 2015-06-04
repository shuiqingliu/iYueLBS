package com.iyuelbs.ui.chat.ui.conversation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avoscloud.leanchatlib.controller.ConversationHelper;
import com.avoscloud.leanchatlib.view.ViewHolder;
import com.iyuelbs.R;
import com.iyuelbs.ui.chat.service.CacheService;
import com.iyuelbs.ui.chat.service.ConversationChangeEvent;
import com.iyuelbs.ui.chat.service.ConversationManager;
import com.iyuelbs.ui.chat.service.event.FinishEvent;
import com.iyuelbs.ui.chat.ui.chat.ChatRoomActivity;
import com.iyuelbs.ui.chat.ui.view.BaseListAdapter;
import com.iyuelbs.ui.chat.ui.view.BaseListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lzw on 14-10-7.
 */
public class ConversationGroupListActivity extends ConversationEventBaseActivity {
  @InjectView(R.id.groupList)
  BaseListView<AVIMConversation> groupListView;

  private List<AVIMConversation> convs = new ArrayList<AVIMConversation>();
  private ConversationGroupListAdapter conversationGroupListAdapter;
  private ConversationManager conversationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_list_activity);
    ButterKnife.inject(this);
    conversationManager = ConversationManager.getInstance();
    initList();
    initActionBar(getApplicationContext().getString(R.string.conversation_group));
    groupListView.onRefresh();
  }

  @Override
  public void onEvent(ConversationChangeEvent conversationChangeEvent) {
    groupListView.refreshWithoutAnim();
  }

  @Override
  public void onEvent(FinishEvent finishEvent) {

  }

  private void initList() {
    conversationGroupListAdapter = new ConversationGroupListAdapter(ctx, convs);
    groupListView.init(new BaseListView.DataFactory<AVIMConversation>() {
      AVException exception;
      List<AVIMConversation> convs;

      @Override
      public List<AVIMConversation> getDatasInBackground(int skip, int limit, List<AVIMConversation> currentDatas) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        conversationManager.findGroupConversationsIncludeMe(new AVIMConversationQueryCallback() {
          @Override
          public void done(List<AVIMConversation> conversations, AVException e) {
            convs = conversations;
            exception = e;
            latch.countDown();
          }
        });
        latch.await();
        if (exception != null) {
          throw exception;
        }
        CacheService.registerConvs(convs);
        return convs;
      }
    }, conversationGroupListAdapter);
    groupListView.setPullLoadEnable(false);
    groupListView.setItemListener(new BaseListView.ItemListener<AVIMConversation>() {
      @Override
      public void onItemSelected(AVIMConversation item) {
        ChatRoomActivity.chatByConversation(ConversationGroupListActivity.this, item);
      }
    });
  }

  /**
   * Created by lzw on 14-10-8.
   */
  public static class ConversationGroupListAdapter extends BaseListAdapter<AVIMConversation> {
    public ConversationGroupListAdapter(Context ctx, List<AVIMConversation> datas) {
      super(ctx, datas);
    }

    @Override
    public View getView(int position, View conView, ViewGroup parent) {
      if (conView == null) {
        //conView = View.inflate(ctx, R.layout.conversation_list_item,null);
        conView = inflater.inflate(R.layout.conversation_list_item, null);
      }
      TextView nameView = ViewHolder.findViewById(conView, R.id.name);
      ImageView iconView = ViewHolder.findViewById(conView, R.id.contact_group_icon);
      AVIMConversation conv = datas.get(position);
      nameView.setText(ConversationHelper.titleOfConversation(conv));
      iconView.setImageBitmap(ConversationManager.getConversationIcon(conv));
      return conView;
    }
  }
}
