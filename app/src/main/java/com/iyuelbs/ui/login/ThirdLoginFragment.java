package com.iyuelbs.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.support.utils.AVUtils;
import com.iyuelbs.support.utils.Utils;
import com.iyuelbs.support.utils.ViewUtils;
import com.iyuelbs.ui.CommonActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Bob Peng on 2015/2/15.
 */
public class ThirdLoginFragment extends LoginFragment {

    private boolean mLoginCallback = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_login_fragment, container, false);
        mLoginText = (MaterialEditText) view.findViewById(R.id.third_login_phone);
        mPasswordText = (MaterialEditText) view.findViewById(R.id.third_login_pwd);
        final Button loginBtn = (Button) view.findViewById(R.id.third_login_btn);
        TextView weiboLogin = (TextView) view.findViewById(R.id.third_login_weibo);
        TextView qqLogin = (TextView) view.findViewById(R.id.third_login_qq);
        View backArrow = view.findViewById(R.id.third_login_arrow_back);
        View registerBtn = view.findViewById(R.id.third_login_register);

        ViewUtils.setDrawableTop(weiboLogin, R.drawable.ic_weibo, 60, 60);
        ViewUtils.setDrawableTop(qqLogin, R.drawable.ic_qq, 60, 60);
        mPasswordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    loginBtn.performClick();
                    return true;
                }
                return false;
            }
        });

        loginBtn.setOnClickListener(this);
        weiboLogin.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == Keys.FOR_COMMON_RESULT) {
            mLoginCallback = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // EventBus is registered after onStart, so delay it to be registered.
        if (mLoginCallback) {
            onLoginSuccess();
            mLoginCallback = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.third_login_btn:
                if (checkField()) {
                    doLogin();
                }
                break;
            case R.id.third_login_weibo:
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_WEIBO_AUTH);
                startActivityForResult(intent, Keys.FOR_COMMON_RESULT);
                break;
            case R.id.third_login_qq:
                //TODO Auth
                break;
            case R.id.third_login_arrow_back:
                getActivity().onBackPressed();
                break;
            case R.id.third_login_register:
                intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected boolean checkField() {
        boolean valid = true;
        String loginText = mLoginText.getText().toString();
        String pwdText = mPasswordText.getText().toString();
        if (TextUtils.isEmpty(loginText)) {
            mLoginText.requestFocus();
            ViewUtils.showToast(mContext, R.string.msg_phone_required);
            valid = false;
        }
        if (TextUtils.isEmpty(pwdText)) {
            if (valid) {
                mPasswordText.requestFocus();
                ViewUtils.showToast(mContext, R.string.msg_password_required);
            }
            valid = false;
        }

        if (valid) {
            if (!Utils.isPhoneString(loginText)) {
                ViewUtils.showToast(mContext, R.string.msg_phone_invalid);
                valid = false;
            }
        }

        return valid;
    }

    @Override
    protected void doLogin() {
        showDialog(getString(R.string.msg_loging_in));
        User.loginByMobilePhoneNumberInBackground(mLoginText.getText().toString(), mPasswordText.getText().toString(),
                new LogInCallback<User>() {
                    public void done(User user, AVException e) {
                        dismissDialog();

                        if (user != null) {
                            onLoginSuccess();
                        } else {
                            AVUtils.onFailure(mContext, e);
                        }
                    }
                }, User.class);
    }
}
