package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.event.DialogEvent;
import com.iyuelbs.event.RegisterEvent;
import com.iyuelbs.utils.AVUtils;
import com.iyuelbs.utils.Utils;
import com.iyuelbs.utils.ViewUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Bob Peng on 2015/2/3.
 */
public class RegisterAccount extends BaseFragment {

    private static final int MIN_PWD_LENGTH = 8;

    private MaterialEditText mUserNameText, mPhoneText, mPasswordText, mConfirmPwdText;
    private User mUser;
    private boolean mHasSignedUp = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_account, container, false);
        mUserNameText = (MaterialEditText) view.findViewById(R.id.reg_account_username);
        mPhoneText = (MaterialEditText) view.findViewById(R.id.reg_account_phone);
        mPasswordText = (MaterialEditText) view.findViewById(R.id.reg_account_password);
        mConfirmPwdText = (MaterialEditText) view.findViewById(R.id.reg_account_pwd_confirm);

        mConfirmPwdText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    doRegister();
                    return true;
                }
                return false;
            }
        });
        mPhoneText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (mHasSignedUp) {
                    doRegister();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserNameText.requestFocus();
        AppHelper.clearLoginState();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_register);

        if (mHasSignedUp) {
            if (isAdded() && mUserNameText.isEnabled()) {
                //mUserNameText.isEnabled() to ensure onetime show up
                ViewUtils.showToast(mContext, R.string.msg_register_already_done);
            }
            mUserNameText.setEnabled(false);
            mPasswordText.setEnabled(false);
            mConfirmPwdText.setEnabled(false);
            mPhoneText.requestFocus();
            AppHelper.postEvent(new RegisterEvent());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            if (checkField()) {
                doRegister();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void doRegister() {
        ViewUtils.closeKeyboard(mConfirmPwdText);

        if (mUser == null) {
            mUser = new User();
            mUser.setUsername(mUserNameText.getText().toString());
            mUser.setMobilePhoneNumber(mPhoneText.getText().toString());
            mUser.setPassword(mPasswordText.getText().toString());
        }

        if (!mHasSignedUp) {
            AppHelper.postEvent(new DialogEvent(getString(R.string.msg_registering)));
            mUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    AppHelper.postEvent(new DialogEvent(null));
                    if (e == null) {
                        onSignUpSuccess();
                    } else {
                        AVUtils.onFailure(mContext, e);
                    }
                }
            });
        } else {
            AppHelper.postEvent(new DialogEvent(""));
            String phone = mPhoneText.getText().toString();

            if (mUser.getMobilePhoneNumber().equals(phone)) {
                User.requestMobilePhoneVerifyInBackground(phone, new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        AppHelper.postEvent(new DialogEvent(null));
                        if (e == null) {
                            onSignUpSuccess();
                        } else {
                            AVUtils.onFailure(mContext, e);
                        }
                    }
                });
            } else {
                mUser.setMobilePhoneNumber(phone);
                mUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        AppHelper.postEvent(new DialogEvent(null));
                        if (e == null) {
                            onSignUpSuccess();
                        } else {
                            AVUtils.onFailure(mContext, e);
                        }
                    }
                });
            }
        }
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

    private void onSignUpSuccess() {
        mHasSignedUp = true;
        Bundle intent = new Bundle();
        intent.putString(Keys.EXTRA_PHONE_NUMBER, mPhoneText.getText().toString());
        intent.putString(Keys.EXTRA_PASSWORD, mPasswordText.getText().toString());

        Fragment fragment = getFragmentManager().findFragmentByTag(RegisterPhoneVerify.TAG);
        if (fragment == null) {
            fragment = RegisterPhoneVerify.newInstance(intent);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.common_container, fragment, RegisterPhoneVerify.TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
