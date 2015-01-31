package com.iyuelbs.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.ui.CommonActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private boolean mIsLogin;
    private EditText mUsername, mPassword, mPasswordConfirm, mEmail;
    private Button mConfirmBtn;

    public static LoginFragment getInstance(Bundle data) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mUsername = (EditText) view.findViewById(R.id.username);
        mPassword = (EditText) view.findViewById(R.id.password);
        mConfirmBtn = (Button) view.findViewById(R.id.confirm_btn);
        Button weiboLogin = (Button) view.findViewById(R.id.login_weibo);
        Button qqLogin = (Button) view.findViewById(R.id.login_qq);
        Button logoff = (Button) view.findViewById(R.id.login_logoff);

        mIsLogin = getArguments().getInt(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_LOGIN) == Keys.OPEN_LOGIN;
        if (!mIsLogin) {
            mPasswordConfirm = (EditText) view.findViewById(R.id.confirm_pwd);
            mEmail = (EditText) view.findViewById(R.id.email);
            mPasswordConfirm.setVisibility(View.VISIBLE);
            mEmail.setVisibility(View.VISIBLE);
        }

        logoff.setOnClickListener(this);
        weiboLogin.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        mConfirmBtn.setText(getString(mIsLogin ? R.string.btn_login : R.string.btn_register));
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == mConfirmBtn) {
            if (mIsLogin) {
                onLoginRequest();
            } else {
                onRegisterRequest();
            }
        } else if (v.getId() == R.id.login_weibo) {
            Intent intent = new Intent(mContext, CommonActivity.class);
            intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_WEIBO_AUTH);
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.login_qq) {

        } else if (v.getId() == R.id.login_logoff) {
            BmobUser.logOut(mContext);
        }
    }

    private void onLoginRequest() {
        if (fieldCheck()) {
            mConfirmBtn.setEnabled(false);
            User user = new User();
            user.setUsername(mUsername.getText().toString());
            user.setPassword(mPassword.getText().toString());
            user.login(mContext, new UserSaveListener());
        }
    }

    private void onRegisterRequest() {
        if (fieldCheck()) {
            mConfirmBtn.setEnabled(false);
            User user = new User();
            user.setUsername(mUsername.getText().toString());
            user.setPassword(mPassword.getText().toString());
            user.setEmail(mEmail.getText().toString());
            user.signUp(mContext, new UserSaveListener());
        }
    }

    private boolean fieldCheck() {
        ViewGroup parent = (ViewGroup) getView();
        if (parent == null)
            return false;

        if (mIsLogin) {
            if (TextUtils.isEmpty(mUsername.getText()) || TextUtils.isEmpty(mPassword.getText())) {
                return false;
            }
        } else {
            if (TextUtils.isEmpty(mUsername.getText()) || TextUtils.isEmpty(mPassword.getText())
                    || TextUtils.isEmpty(mPasswordConfirm.getText()) || TextUtils.isEmpty(mEmail.getText())) {
                return false;
            }
            if (!mPassword.getText().toString().equals(mPasswordConfirm.getText().toString())) {
                return false;
            }
        }

        return true;
    }

    private class UserSaveListener extends SaveListener {

        @Override
        public void onSuccess() {
            mConfirmBtn.setEnabled(true);
            Toast.makeText(mContext, mIsLogin ? "登陆成功！" : "注册成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(int errorCode, String msg) {
            mConfirmBtn.setEnabled(true);
            Toast.makeText(mContext, "错误：" + msg, Toast.LENGTH_SHORT).show();
        }
    }
}
