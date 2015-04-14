package com.iyuelbs.ui.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.support.utils.ViewUtils;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity, false);

        initFragments(savedInstanceState);
    }

    @Override
    protected void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name);

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // set drawer width
        findViewById(R.id.left_drawer).getLayoutParams().width = ViewUtils.getScreenWidth()
                - ViewUtils.getPixels(56);
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            MapFragment fragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment, "map")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDrawerToggle.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

}
