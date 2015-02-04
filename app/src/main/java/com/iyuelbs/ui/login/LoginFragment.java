package com.iyuelbs.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.entity.User;
import com.iyuelbs.ui.main.MainActivity;
import com.iyuelbs.utils.ViewUtils;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by qingliu on 1/31/15.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private EditText mUserText, mPwdText;
    private AlertDialog mDialog;

    public static LoginFragment newInstance(Bundle data) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mUserText = (EditText) view.findViewById(R.id.login_username);
        mPwdText = (EditText) view.findViewById(R.id.login_password);
        Button confirmBtn = (Button) view.findViewById(R.id.login_confirm_btn);
        Button registerBtn = (Button) view.findViewById(R.id.login_register_btn);

        registerBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        return view;
    }

    private boolean checkField() {
        if (TextUtils.isEmpty(mUserText.getText())) {
            mUserText.requestFocus();
            ViewUtils.showToast(mContext, R.string.msg_please_input_account);
            return false;
        } else if (TextUtils.isEmpty(mPwdText.getText())) {
            mPwdText.requestFocus();
            ViewUtils.showToast(mContext, R.string.msg_please_input_account);
            return false;
        }

        return true;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_confirm_btn) {
            if (checkField()) {
                login();
            }
        } else if (id == R.id.login_register_btn) {
            Intent intent = new Intent(mContext, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void login() {
        User user = new User();
        user.setPassword(mPwdText.getText().toString());
        user.multiLogin(mContext, mUserText.getText().toString(), new SaveListener() {
            @Override
            public void onSuccess() {
                if (mDialog != null)
                    mDialog.dismiss();
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(int i, String s) {
                if (mDialog != null)
                    mDialog.dismiss();
                ViewUtils.showToast(mContext, s);
            }
        });
        mDialog = ViewUtils.createLoadingDialog(mContext, getString(R.string.msg_login_in));
        mDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
