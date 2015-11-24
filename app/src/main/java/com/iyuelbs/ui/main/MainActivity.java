package com.iyuelbs.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.support.utils.ViewUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private MapFragment fragment;

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
            fragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment, "map")
                    .commit();
        }
    }

    public void closeDrawLayout(){
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDrawerToggle.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.action_settings:
                // TODO 设置界面进行设置
                DrawerFragment.goMsgActivityFromActivity(this,3);
                break;
            case R.id.action_logout:
                //退出当前登录
                finish();
                break;
            case R.id.my_location:
                fragment.mLocationClient.requestLocation();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else if (fragment != null) {
            fragment.slidingClose();
            if (fragment.mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
                super.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }
}
