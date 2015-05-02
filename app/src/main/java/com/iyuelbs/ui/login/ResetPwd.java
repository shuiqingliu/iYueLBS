package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.entity.User;
import com.iyuelbs.support.utils.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by qingliu on 4/29/15.
 */
public class ResetPwd extends BaseFragment implements View.OnClickListener {
    private MaterialEditText mUserNameText, mPhoneText, mPasswordText, mConfirmPwdText,mPhoneConfirm;
    private Button mPhoneResent, mResetPwd;
    private static final int MIN_PWD_LENGTH = 8;
    private Timer mTimer;
    private ResentTask mTask;
    private int mResentCounter = 60;
    private int mNextAction = 0;
    private String mResentStr;
    private String smsCode;
    private String newPwd;
    private String secPwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_password, container,false);
        View backArrow = view.findViewById(R.id.find_pass_arrow_back);
        mPhoneText = (MaterialEditText) view.findViewById(R.id.find_pass_phone);
        mPasswordText = (MaterialEditText) view.findViewById(R.id.fist_pwd);
        mConfirmPwdText = (MaterialEditText) view.findViewById(R.id.verify_pwd);
        mPhoneConfirm = (MaterialEditText) view.findViewById(R.id.phone_confirm_code);
        mPhoneResent = (Button) view.findViewById(R.id.phone_confirm_resent);
        mResetPwd = (Button) view.findViewById(R.id.reset_pass_btn);
        mResentStr = getString(R.string.btn_resent_verify_code);

        //如果没有获取验证码就将输入密码禁用
        mPhoneConfirm.setEnabled(false);
        mPasswordText.setEnabled(false);
        mConfirmPwdText.setEnabled(false);

        //事件监听
        backArrow.setOnClickListener(this);
        mPhoneResent.setOnClickListener(this);
        mResetPwd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.find_pass_arrow_back:
                getActivity().onBackPressed();
                break;
            case R.id.phone_confirm_resent:
                if (mPhoneText.getText().length() != 11){
                    Toast.makeText(mContext,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }else {
                    mPhoneResent.setEnabled(false);
                    mPhoneResent.setText(R.string.msg_sending);
                    mPhoneConfirm.requestFocus();
                    User.requestPasswordResetBySmsCodeInBackground(mPhoneText.getText().toString()
                            , new RequestMobileCodeCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                mPhoneConfirm.setEnabled(true);
                                mPasswordText.setEnabled(true);
                                mConfirmPwdText.setEnabled(true);
                                initTimer();
                            } else {
                                mPhoneResent.setEnabled(true);
                                mPhoneResent.setText(mResentStr);
                                //AVUtils.onFailure(mContext, e);
                                if (e.getCode() == 213) {
                                    Toast.makeText(mContext, "手机号不存在", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.reset_pass_btn:
                if(checkField()){
                    smsCode = mPhoneConfirm.getText().toString();
                    newPwd = mPasswordText.getText().toString();
                    secPwd = mConfirmPwdText.getText().toString();
                    User.resetPasswordBySmsCodeInBackground(smsCode, newPwd, new UpdatePasswordCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null){
                                Toast.makeText(mContext,"密码重置成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(mContext,"失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        }
    }

    private void initTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            mTask = new ResentTask();
        }
        mTimer.schedule(mTask, 0, 1000);
    }

    private boolean checkField() {
        boolean valid = true;
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

    private class ResentTask extends TimerTask {

        private Runnable resetRunnable;
        private Runnable decrementRunnable;

        @Override
        public void run() {
            if (mResentCounter == 1) {
                mResentCounter = 60;
                mTimer.cancel();
                mTimer = null;
                mTask = null;
                if (isVisible()) {
                    if (resetRunnable == null) {
                        resetRunnable = new Runnable() {
                            public void run() {
                                mPhoneResent.setEnabled(true);
                                mPhoneResent.setText(mResentStr);
                            }
                        };
                    }
                    getActivity().runOnUiThread(resetRunnable);
                }
            } else {
                if (isVisible()) {
                    if (decrementRunnable == null) {
                        decrementRunnable = new Runnable() {
                            public void run() {
                                mPhoneResent.setText(mResentStr + " (" + mResentCounter + "秒)");
                            }
                        };
                    }
                    getActivity().runOnUiThread(decrementRunnable);
                }
                mResentCounter--;
            }
        }
    }
}
