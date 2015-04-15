package com.iyuelbs.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.iyuelbs.R;

/**
 * Created by xifan on 15-4-14.
 */
public class MapFragment extends Fragment implements View.OnClickListener {

    private Button mRequestLocButton;
    private Button mLocationReq;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient = null;
    private BitmapDescriptor mCurrentMarker;
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    private boolean isFirstLoc = true;// 是否首次定位

    private Context mContext;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.baidumap, viewGroup);

        mMapView = (MapView) view.findViewById(R.id.mapview);
        mRequestLocButton = (Button) view.findViewById(R.id.request);
        mLocationReq = (Button) view.findViewById(R.id.location_req);
        RadioGroup group = (RadioGroup) view.findViewById(R.id.radioGroup);

        mBaiduMap = mMapView.getMap();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocationClient = new LocationClient(mContext);
        mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(999);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        // common ui
        mRequestLocButton.setText("普通");
        mRequestLocButton.setOnClickListener(this);
        mLocationReq.setText("+");
        mLocationReq.setOnClickListener(this);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.defaulticon) {
                    // 传入null则，恢复默认图标
                    mCurrentMarker = null;
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, null));
                } else if (checkedId == R.id.customicon) {
                    // 修改为自定义marker
                    mCurrentMarker = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_geo);
                    mBaiduMap
                            .setMyLocationConfigeration(new MyLocationConfiguration(
                                    mCurrentMode, true, mCurrentMarker));
                }
            }
        });

        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    private void onRequestLocation() {
        switch (mCurrentMode) {
            case NORMAL:
                mRequestLocButton.setText("跟随");
                mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                break;
            case COMPASS:
                mRequestLocButton.setText("普通");
                mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                break;
            case FOLLOWING:
                mRequestLocButton.setText("罗盘");
                mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mRequestLocButton) {
            onRequestLocation();
        }else if (v == mLocationReq){
            mLocationClient.requestLocation();
        }
    }

    public void drawCircle(LatLng latLng){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.fillColor(0x204DB6AC);
        circleOptions.radius(300);
        Stroke stroke = new Stroke(3, 0xff00796B);
        //设置圆边框信息
        circleOptions.stroke(stroke);
        //地图添加一个覆盖物,并尝试重绘覆盖物
        mBaiduMap.clear();
        mBaiduMap.addOverlay(circleOptions);
        Log.e("绘制","绘制完成");
    }


    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

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
            drawCircle(new LatLng(location.getLatitude(),location.getLongitude()));
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
