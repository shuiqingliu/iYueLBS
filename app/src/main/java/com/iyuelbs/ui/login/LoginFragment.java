package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.utils.ViewUtils;

/**
 * Created by qingliu on 1/31/15.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private EditText mUserText, mPwdText;

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

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_confirm_btn) {
            if (checkField()) {
                login();
            }
        } else if (id == R.id.login_register_btn) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            Bundle data = new Bundle();
            data.putInt(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_REGISTER);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.common_container, LoginFragment.newInstance(data));
            transaction.addToBackStack(null);
            transaction.commit();
        }
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

    private void login() {

    }
}
