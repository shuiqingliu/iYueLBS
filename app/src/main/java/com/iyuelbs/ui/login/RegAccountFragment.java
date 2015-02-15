package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.entity.User;
import com.iyuelbs.utils.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Bob Peng on 2015/2/3.
 */
public class RegAccountFragment extends BaseFragment {

    private static final int MIN_PWD_LENGTH = 8;

    private MaterialEditText mUserNameText, mPhoneText, mPasswordText, mConfirmPwdText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_account, container, false);
        mUserNameText = (MaterialEditText) view.findViewById(R.id.reg_account_username);
        mPhoneText = (MaterialEditText) view.findViewById(R.id.reg_account_phone);
        mPasswordText = (MaterialEditText) view.findViewById(R.id.reg_account_password);
        mConfirmPwdText = (MaterialEditText) view.findViewById(R.id.reg_account_pwd_confirm);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            if (checkField()) {
                // TODO dialog
                User user = new User();
                user.setUsername(mUserNameText.getText().toString());
                user.setMobilePhoneNumber(mPhoneText.getText().toString());
                user.setPassword(mPasswordText.getText().toString());
                user.signUpInBackground(new SignUpListener());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkField() {
        boolean valid = true;
        if (TextUtils.isEmpty(mUserNameText.getText())) {
            mUserNameText.setError(getString(R.string.msg_username_required));
            valid = false;
        }
        if (TextUtils.isEmpty(mPhoneText.getText())) {
            mPhoneText.setError(getString(R.string.msg_phone_required));
            valid = false;
        }
        if (TextUtils.isEmpty(mPasswordText.getText())) {
            mPasswordText.setError(getString(R.string.msg_password_required));
            valid = false;
        }
        if (TextUtils.isEmpty(mConfirmPwdText.getText())) {
            mConfirmPwdText.setError(getString(R.string.msg_pwd_confirm_required));
            valid = false;
        }

        if (valid) {
            if (!Utils.isPhoneString(mPhoneText.getText().toString())) {
                mPhoneText.setError(getString(R.string.msg_phone_invalid));
                valid = false;
            }
            if (mPhoneText.getText().toString().equals(mUserNameText.getText().toString())) {
                mUserNameText.setError(getString(R.string.msg_username_cant_be_phone));
                valid = false;
            }
            if (!mPasswordText.getText().toString().equals(mConfirmPwdText.getText().toString())) {
                mConfirmPwdText.setError(getString(R.string.msg_pwd_confirm_failed));
                valid = false;
            }
            if (mPasswordText.getText().length() < MIN_PWD_LENGTH) {
                mPasswordText.setError(getString(R.string.msg_pwd_too_short));
                valid = false;
            }
        }
        return valid;
    }

    private class SignUpListener extends SignUpCallback {
        public void done(AVException e) {
            if (e == null) {
                // TODO dialog
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.common_container, new RegUserDetailFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            } else {
                // TODO dialog
            }
        }
    }
}
