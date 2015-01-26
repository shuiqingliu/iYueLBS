package com.iyuelbs.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.entity.User;
import com.iyuelbs.entity.UserInfo;
import com.iyuelbs.utils.ViewUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Bob Peng on 2015/1/26.
 */
public class UserManager extends BaseFragment implements View.OnClickListener {

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mAvatar;
    private EditText mNickName;
    private CheckBox mSex;
    private Button mSaveBtn;

    private User mUser;
    private UserInfo mUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_manager, container, false);
        mSex = (CheckBox) view.findViewById(R.id.sex);
        mAvatar = (EditText) view.findViewById(R.id.avatar);
        mNickName = (EditText) view.findViewById(R.id.nickname);
        mPhone = (EditText) view.findViewById(R.id.phone);
        mEmail = (EditText) view.findViewById(R.id.email);
        mUsername = (EditText) view.findViewById(R.id.username);
        mSaveBtn = (Button) view.findViewById(R.id.save_btn);

        mSaveBtn.setOnClickListener(this);
        mUser = BmobUser.getCurrentUser(mContext, User.class);
        if (mUser != null) {
            mUsername.setText(mUser.getUsername());
            mEmail.setText(mUser.getEmail());
            mPhone.setText(mUser.getPhoneNumber());
            loadUserInfo();
        } else {
            Toast.makeText(mContext, "用户未登录！", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void loadUserInfo() {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.getObject(mContext, mUser.getUserInfo().getObjectId(), new GetListener<UserInfo>() {

            @Override
            public void onSuccess(UserInfo userInfo) {
                if (userInfo == null)
                    return;
                mUserInfo = userInfo;
                mNickName.setText(userInfo.getNickName());
                mAvatar.setText(userInfo.getAvatar());
                mSex.setChecked(userInfo.isMale());
            }

            @Override
            public void onFailure(int i, String s) {
                ViewUtils.showToast(mContext, "error:" + i + " " + s);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mSaveBtn) {
            final UserInfo userInfo = new UserInfo();
            userInfo.setAvatar(mAvatar.getText().toString());
            userInfo.setNickName(mNickName.getText().toString());
            userInfo.setMale(mSex.isChecked());
            userInfo.setUser(mUser);
            userInfo.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    BmobPointer userInfoPointer = new BmobPointer(userInfo);
                    mUser.setUserInfo(userInfoPointer);
                    mUser.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            ViewUtils.showToast(mContext, "已保存UserInfo！");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                        }
                    });
                    ViewUtils.showToast(mContext, "保存成功！");
                }

                @Override
                public void onFailure(int i, String s) {
                    ViewUtils.showToast(mContext, s);
                }
            });
        }
    }
}
