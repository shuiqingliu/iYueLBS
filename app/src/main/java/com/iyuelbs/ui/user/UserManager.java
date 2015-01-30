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
import com.iyuelbs.utils.ViewUtils;

import cn.bmob.v3.BmobUser;
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
            mAvatar.setText(mUser.getAvatarUrl());
            mNickName.setText(mUser.getNickName());
            mSex.setChecked(mUser.isMale());
        } else {
            Toast.makeText(mContext, "用户未登录！", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == mSaveBtn) {
            mUser.setEmail(mEmail.getText().toString());
            mUser.setNickName(mNickName.getText().toString());
            mUser.setIsMale(mSex.isChecked());
            mUser.setPhoneNumber(mPhone.getText().toString());
            mUser.setAvatarUrl(mAvatar.getText().toString());
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
    }
}
