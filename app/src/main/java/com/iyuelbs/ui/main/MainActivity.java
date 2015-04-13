package com.iyuelbs.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.support.utils.ViewUtils;

/**
 * Created by Bob Peng on 2015/1/21.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private DrawerController mDrawerController;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public BDLocation location;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DrawerFragment) {
            mDrawerController = (DrawerController) fragment;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidumap);
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mBaiduMap.setMyLocationEnabled(true);

    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            Log.e("fail",sb.toString());
        }
    }

    @Override
    protected void setupActionBar(int themeColor) {
        super.setupActionBar(themeColor);
        hackActionBar();

        // set drawer width
        findViewById(R.id.left_drawer).getLayoutParams().width = ViewUtils.getScreenWidth()
                - ViewUtils.getPixels(56);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    private void hackActionBar() {
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View actionBar = decor.getChildAt(0);

        DrawerLayout drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.main_activity, null);
        FrameLayout container = (FrameLayout) drawerLayout.findViewById(R.id.main_fake_content);
        container.setFitsSystemWindows(true);
        // correct system bar margin
        ViewGroup.MarginLayoutParams params = ((ViewGroup.MarginLayoutParams) drawerLayout.findViewById
                (R.id.main_content).getLayoutParams());
        params.topMargin = ViewUtils.getPixels(56);
        params.bottomMargin = ViewUtils.getPixels(48);

        decor.removeView(actionBar);
        container.addView(actionBar);
        decor.addView(drawerLayout);

        mDrawerLayout = drawerLayout;
        initView();
    }

    @Override
    protected void initView() {
        // hacked R.layout.main_activity
    }

    @Override
    protected void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
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
    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.login:
//                Intent intent = new Intent(mContext, CommonActivity.class);
//                intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_LOGIN);
//                startActivityForResult(intent, Keys.FOR_COMMON_RESULT);
//                break;
//            case R.id.register:
//                intent = new Intent(mContext, CommonActivity.class);
//                intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_REGISTER);
//                startActivityForResult(intent, Keys.FOR_COMMON_RESULT);
//                break;
//            case R.id.add_friend:
//                if (mDrawerController != null)
//                    mDrawerController.setItemCount(2, 5);
//                break;
//            case R.id.view_friend:
//                break;
//            case R.id.fill_info:
//                intent = new Intent(mContext, CommonActivity.class);
//                intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_FILL_INFO);
//                startActivity(intent);
//                break;
//            case R.id.new_tag:
//                break;
//            case R.id.upload_avatar:
//                intent = new Intent(mContext, CommonActivity.class);
//                intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_AVATAR);
//                startActivity(intent);
//                break;
//        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
}
