package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.entity.User;
import com.iyuelbs.utils.BmobUtils;
import com.iyuelbs.utils.Utils;
import com.iyuelbs.utils.ViewUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Bob Peng on 2015/2/3.
 */
public class RegAccountFragment extends BaseFragment {
    private MaterialEditText mUserNameText, mEmailText, mPasswordText, mConfirmPwdText;
    private MaterialDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_account, container, false);
        mUserNameText = (MaterialEditText) view.findViewById(R.id.reg_account_username);
        mEmailText = (MaterialEditText) view.findViewById(R.id.reg_account_email);
        mPasswordText = (MaterialEditText) view.findViewById(R.id.reg_account_password);
        mConfirmPwdText = (MaterialEditText) view.findViewById(R.id.reg_account_pwd_confirm);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            if (checkField()) {
                User user = new User();
                user.setUsername(mUserNameText.getText().toString());
                user.setEmail(mEmailText.getText().toString());
                user.setPassword(Utils.md5(mPasswordText.getText().toString()));
                user.signUp(mContext, new SignUpListener());
                mDialog = ViewUtils.createLoadingDialog(mContext, null);
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
        if (TextUtils.isEmpty(mEmailText.getText())) {
            mEmailText.setError(getString(R.string.msg_email_required));
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
        if (valid && !mPasswordText.getText().toString().equals(mConfirmPwdText.getText().toString())) {
            mConfirmPwdText.setError(getString(R.string.msg_pwd_confirm_failed));
            valid = false;
        }
        return valid;
    }

    private class SignUpListener extends SaveListener {
        public void onSuccess() {
            User user = new User();
            user.setUsername(mUserNameText.getText().toString());
            user.setPassword(Utils.md5(mPasswordText.getText().toString()));
            user.login(mContext, new SaveListener() {

                public void onSuccess() {
                    if (mDialog != null)
                        mDialog.dismiss();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.common_container, new RegUserDetailFragment());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                }

                public void onFailure(int i, String s) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    BmobUtils.onFailure(mContext, i, s);
                }
            });
        }

        public void onFailure(int i, String s) {
            if (mDialog != null)
                mDialog.dismiss();
            BmobUtils.onFailure(mContext, i, s);
        }
    }
}
