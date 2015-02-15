package com.iyuelbs.ui.login;

import android.content.Intent;
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
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.entity.User;
import com.iyuelbs.event.DialogEvent;
import com.iyuelbs.ui.main.MainActivity;
import com.iyuelbs.utils.AVUtils;
import com.iyuelbs.utils.ViewUtils;

import de.greenrobot.event.EventBus;

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
                    return true;
                }
                return false;
            }
        });

        registerBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        AppHelper.postEvent(new DialogEvent(null));
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
        EventBus.getDefault().post(new DialogEvent(getString(R.string.msg_login_in)));
        User.multiLogin(mLoginText.getText().toString(), mPasswordText.getText().toString(),
                new LogInCallback<User>() {
                    public void done(User user, AVException e) {
                        if (user != null) {
                            AppHelper.postEvent(new DialogEvent(null));
                            onLoginSuccess();
                        } else {
                            AppHelper.postEvent(new DialogEvent(null));
                            AVUtils.onFailure(mContext, e);
                        }
                    }
                });
    }

    protected void onLoginSuccess() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
