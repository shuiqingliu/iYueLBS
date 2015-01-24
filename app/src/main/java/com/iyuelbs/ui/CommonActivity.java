package com.iyuelbs.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.iyuelbs.R;
import com.iyuelbs.app.Keys;

public class CommonActivity extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        initFragment();
    }

    private void initFragment() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            String type = bundle.getString(Keys.OPEN_TYPE);
            if (type.equals(Keys.OPEN_LOGIN)) {
//                transaction.replace(R.id.common_container,new )
            }
        }
    }

}
