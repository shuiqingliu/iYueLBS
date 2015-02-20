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
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.event.DialogEvent;
import com.iyuelbs.utils.AVUtils;
import com.iyuelbs.utils.ViewUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Bob Peng on 2015/2/15.
 */
public class RegPhoneVerify extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "RegPhoneVerify";

    private MaterialEditText mCodeText;
    private Button mResentBtn;

    private String mPhoneNumber;
    private String mPassword;
    private String mResentStr;
    private int mResentCounter = 60;

    private Timer mTimer;
    private ResentTask mTask;

    public static RegPhoneVerify newInstance(Bundle data) {
        RegPhoneVerify fragment = new RegPhoneVerify();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mPhoneNumber = data.getString(Keys.EXTRA_PHONE_NUMBER);
            mPassword = data.getString(Keys.EXTRA_PASSWORD);
            if (TextUtils.isEmpty(mPhoneNumber) || TextUtils.isEmpty(mPassword)) {
                throw new IllegalArgumentException();
            }
        }
        mResentStr = getString(R.string.btn_resent_verify_code);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_phone_confirm, container, false);
        mCodeText = (MaterialEditText) view.findViewById(R.id.phone_confirm_code);
        mResentBtn = (Button) view.findViewById(R.id.phone_confirm_resent);

        mResentBtn.setOnClickListener(this);
        mCodeText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    verifyCode();
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
        initTimer();
        mResentBtn.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_verify_phone);
        mCodeText.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            verifyCode();
        }
        return super.onOptionsItemSelected(item);
    }

    private void verifyCode() {
        if (TextUtils.isEmpty(mCodeText.getText())) {
            ViewUtils.showToast(mContext, getString(R.string.msg_verify_code_required));
        } else {
            AppHelper.postEvent(new DialogEvent());
            ViewUtils.closeKeyboard(mCodeText);
            User.verifyMobilePhoneInBackground(mCodeText.getText().toString(), new VerifyCallBack());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.phone_confirm_resent) {
            mResentBtn.setEnabled(false);
            mResentBtn.setText(R.string.msg_sending);
            User.requestMobilePhoneVerifyInBackground(mPhoneNumber, new RequestMobileCodeCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        initTimer();
                    } else {
                        mResentBtn.setEnabled(true);
                        mResentBtn.setText(mResentStr);
                        AVUtils.onFailure(mContext, e);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void initTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            mTask = new ResentTask();
        }
        mTimer.schedule(mTask, 0, 1000);
    }

    private class VerifyCallBack extends AVMobilePhoneVerifyCallback {
        @Override
        public void done(AVException e) {
            if (e == null) {
                User.loginByMobilePhoneNumberInBackground(mPhoneNumber, mPassword, new LogInCallback<User>() {
                    @Override
                    public void done(User user, AVException e) {
                        AppHelper.postEvent(new DialogEvent(null));
                        if (user != null) {
                            List<Fragment> fragments = getFragmentManager().getFragments();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.common_container, new RegUserDetailFragment());
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                            String className = RegUserDetailFragment.class.getSimpleName();
                            for (Fragment fragment : fragments) {
                                if (!fragment.getClass().getSimpleName().equals(className)) {
                                    transaction.remove(fragment);
                                }
                            }
                            transaction.commit();
                        } else {
                            AVUtils.onFailure(mContext, e);
                        }
                    }
                }, User.class);
            } else {
                AppHelper.postEvent(new DialogEvent(null));
                AVUtils.onFailure(mContext, e);
            }
        }
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
                                mResentBtn.setEnabled(true);
                                mResentBtn.setText(mResentStr);
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
                                mResentBtn.setText(mResentStr + " (" + mResentCounter + "秒)");
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
