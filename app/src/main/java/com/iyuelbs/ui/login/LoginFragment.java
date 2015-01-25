package com.iyuelbs.ui.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuelbs.R;
import com.iyuelbs.app.Keys;

public class LoginFragment extends Fragment {

    public static LoginFragment getInstance(Bundle data) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(data);
        return fragment;
    }

    private boolean mIsLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mIsLogin = getArguments().getString(Keys.OPEN_TYPE).equals(Keys.OPEN_LOGIN);

        if (!mIsLogin) {

        }
        return view;
    }


}
