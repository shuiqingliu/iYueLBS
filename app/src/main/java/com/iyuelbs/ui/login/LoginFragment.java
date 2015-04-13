package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.support.event.LoginEvent;
import com.iyuelbs.support.utils.AVUtils;
import com.iyuelbs.support.utils.NavUtils;
import com.iyuelbs.support.utils.ViewUtils;

/**
 * Created by qingliu on 1/31/15.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    protected EditText mLoginText, mPasswordText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mLoginText = (EditText) view.findViewById(R.id.login_username);
        mPasswordText = (EditText) view.findViewById(R.id.login_password);
        final Button confirmBtn = (Button) view.findViewById(R.id.login_confirm_btn);
        Button registerBtn = (Button) view.findViewById(R.id.login_other_btn);

        mPasswordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    confirmBtn.performClick();
                    ViewUtils.closeKeyboard(mPasswordText);
                    return true;
                }
                return false;
            }
        });

        registerBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_confirm_btn) {
            if (checkField()) {
                doLogin();
            }
        } else if (id == R.id.login_other_btn) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.common_container, new ThirdLoginFragment());
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

    protected boolean checkField() {
        boolean valid = true;
        if (TextUtils.isEmpty(mLoginText.getText())) {
            mLoginText.requestFocus();
            ViewUtils.showToast(mContext, R.string.msg_username_required);
            valid = false;
        }
        if (TextUtils.isEmpty(mPasswordText.getText())) {
            if (valid) {
                mPasswordText.requestFocus();
                ViewUtils.showToast(mContext, R.string.msg_password_required);
            }
            valid = false;
        }

        return valid;
    }

    protected void doLogin() {
        showDialog(getString(R.string.msg_loging_in));
        User.multiLogin(mLoginText.getText().toString(), mPasswordText.getText().toString(),
                new LogInCallback<User>() {
                    public void done(User user, AVException e) {
                        dismissDialog();

                        if (user != null) {
                            onLoginSuccess();
                        } else {
                            AVUtils.onFailure(mContext, e);

                            if (e.getCode() == AVException.USER_MOBILEPHONE_NOT_VERIFIED) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(Keys.EXTRA_REGISTER_STEP, Keys.REG_STEP_PHONE_VERIFY);
                                bundle.putString(Keys.EXTRA_PHONE_NUMBER,
                                        mLoginText.getText().toString());
                                bundle.putInt(Keys.EXTRA_ACTION_NEXT, Keys.ACTION_GO_HOME);
                                bundle.putString(Keys.EXTRA_PASSWORD, mPasswordText.getText().toString());
                                NavUtils.go(mContext, RegisterActivity.class, bundle);
                            }
                        }
                    }
                });
    }

    protected void onLoginSuccess() {
        postEvent(new LoginEvent());
    }
}
