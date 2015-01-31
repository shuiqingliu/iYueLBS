package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;

/**
 * Created by qingliu on 1/31/15.
 */
public class LoginDefault extends BaseFragment implements View.OnClickListener{
    private EditText mUserName, mPassword;
    private Button mConfirmBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_default, container, false);
        mUserName = (EditText) view.findViewById(R.id.username);
        mPassword = (EditText) view.findViewById(R.id.password);
        mConfirmBtn = (Button) view.findViewById(R.id.confirm_btn);

        mConfirmBtn.setOnClickListener(this);
        return view;
    }
    public void onClick(View v){

    }
}
