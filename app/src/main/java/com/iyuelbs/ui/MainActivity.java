package com.iyuelbs.ui;

import android.os.Bundle;
import android.view.View;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createUserTable();
    }

    private void createUserTable() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login) {

        }
    }
}
