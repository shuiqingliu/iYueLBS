package com.iyuelbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.Keys;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login:
                Intent intent = new Intent(mContext,CommonActivity.class);
                intent.putExtra(Keys.OPEN_TYPE,Keys.OPEN_LOGIN);
                startActivity(intent);
                break;
            case R.id.register:
                intent = new Intent(mContext,CommonActivity.class);
                intent.putExtra(Keys.OPEN_TYPE,Keys.OPEN_REGISTER);
                startActivity(intent);
                break;
        }
    }
}
