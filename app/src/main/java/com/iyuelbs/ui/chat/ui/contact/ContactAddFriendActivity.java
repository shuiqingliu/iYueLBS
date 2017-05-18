package com.iyuelbs.ui.chat.ui.contact;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avoscloud.leanchatlib.view.ViewHolder;
import com.iyuelbs.R;
import com.iyuelbs.app.AppApplication;
import com.iyuelbs.ui.chat.service.AddRequestService;
import com.iyuelbs.ui.chat.service.UserService;
import com.iyuelbs.ui.chat.ui.base_activity.BaseActivity;
import com.iyuelbs.ui.chat.ui.view.BaseListAdapter;
import com.iyuelbs.ui.chat.ui.view.BaseListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactAddFriendActivity extends BaseActivity {
  @BindView(R.id.searchNameEdit)
  EditText searchNameEdit;

  @BindView(R.id.searchList)
  BaseListView<AVUser> listView;
  private String searchName = "";
  private AddFriendListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_add_friend_activity);
    ButterKnife.bind(this);
    init();
  }

  private void init() {
    initActionBar(AppApplication.getApplication().getString(R.string.contact_findFriends));
    adapter = new AddFriendListAdapter(this, new ArrayList<AVUser>());
    listView.init(new BaseListView.DataFactory<AVUser>() {
      @Override
      public List<AVUser> getDatasInBackground(int skip, int limit, List<AVUser> currentDatas) throws Exception {
        return UserService.searchUser(searchName, adapter.getCount());
      }
    }, adapter);
    adapter.setClickListener(new AddFriendListAdapter.AddButtonClickListener() {
      @Override
      public void onAddButtonClick(AVUser user) {
        AddRequestService.createAddRequestInBackground(ContactAddFriendActivity.this, user);
      }
    });
    listView.onRefresh();
  }

  @OnClick(R.id.searchBtn)
  public void search(View view) {
    searchName = searchNameEdit.getText().toString();
    listView.onRefresh();
  }

  public static class AddFriendListAdapter extends BaseListAdapter<AVUser> {

    private AddButtonClickListener addButtonClickListener;

    public AddFriendListAdapter(Context context, List<AVUser> list) {
      super(context, list);
    }

    public void setClickListener(AddButtonClickListener addButtonClickListener) {
      this.addButtonClickListener = addButtonClickListener;
    }

    @Override
    public View getView(int position, View conView, ViewGroup parent) {
      // TODO Auto-generated method stub
      if (conView == null) {
        conView = inflater.inflate(R.layout.contact_add_friend_item, null);
      }
      final AVUser contact = datas.get(position);
      TextView nameView = ViewHolder.findViewById(conView, R.id.name);
      ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatar);
      Button addBtn = ViewHolder.findViewById(conView, R.id.add);
      UserService.displayAvatar(contact, avatarView);
      nameView.setText(contact.getUsername());
      addBtn.setText(R.string.contact_add);
      addBtn.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          if (addButtonClickListener != null) {
            addButtonClickListener.onAddButtonClick(contact);
          }
        }
      });
      return conView;
    }

    public interface AddButtonClickListener {
      void onAddButtonClick(AVUser user);
    }

  }
}
