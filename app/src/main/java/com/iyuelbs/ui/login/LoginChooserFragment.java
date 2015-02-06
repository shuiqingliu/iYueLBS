package com.iyuelbs.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;
import com.iyuelbs.ui.CommonActivity;

@Deprecated
public class LoginChooserFragment extends BaseFragment implements View.OnClickListener {

    private boolean mIsLogin;
    private Button mWeiboBtn, mQQBtn, mDefaultBtn;

    public static LoginChooserFragment getInstance(Bundle data) {
        LoginChooserFragment fragment = new LoginChooserFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_chooser_fragment, container, false);
        mWeiboBtn = (Button) view.findViewById(R.id.login_weibo_auth_btn);
        mQQBtn = (Button) view.findViewById(R.id.login_qq_auth_btn);
        mDefaultBtn = (Button) view.findViewById(R.id.login_default_auth_btn);
        mIsLogin = getArguments().getInt(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_LOGIN) == Keys.OPEN_LOGIN;
        mDefaultBtn.setOnClickListener(this);
        mWeiboBtn.setOnClickListener(this);
        mQQBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == mWeiboBtn) {
            //接入weibo api 实现
        } else if (v == mQQBtn) {
            //接入QQ api 实现
        } else if (v == mDefaultBtn) {
            // 调用直接登录acitivity实现
            Intent intent = new Intent(mContext, CommonActivity.class);
            intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_LOGIN);
            startActivity(intent);
        }
    }
}
