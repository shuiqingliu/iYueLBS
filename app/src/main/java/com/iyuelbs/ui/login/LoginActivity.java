package com.iyuelbs.ui.login;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);
        initFragments();
    }

    @Override
    protected void setupWindowStyle(int themeColor) {
        AppHelper.setSystemBarSolidColor(this, getResources().getColor(R.color.login_background));
    }

    @Override
    protected void initFragments() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.common_container, new LoginFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
