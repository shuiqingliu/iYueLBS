package com.iyuelbs.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
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
    public BDLocation location;
    private MyLocationData mLocData;
    //定位相关
    public MyLocationListener myListener = new MyLocationListener();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;

    //UI相关
    RadioGroup.OnCheckedChangeListener radioButtonListener;
    Button requestLocButton;
    boolean isFirstLoc = true;// 是否首次定位


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidumap);
        requestLocButton = (Button) findViewById(R.id.request);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        requestLocButton.setText("普通");
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);
        RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
        radioButtonListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.defaulticon) {
                    // 传入null则，恢复默认图标
                    mCurrentMarker = null;
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, null));
                }
                if (checkedId == R.id.customicon) {
                    // 修改为自定义marker
                    mCurrentMarker = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_geo);
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, mCurrentMarker));
                }
            }
        };
        group.setOnCheckedChangeListener(radioButtonListener);
        //地图初始化
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DrawerFragment) {
            mDrawerController = (DrawerController) fragment;
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
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
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
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
