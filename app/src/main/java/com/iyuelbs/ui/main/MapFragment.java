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
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.SaveCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.entity.Place;
import com.iyuelbs.entity.Tag;
import com.iyuelbs.entity.User;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by xifan on 15-4-14.
 */
public class MapFragment extends Fragment implements View.OnClickListener{

    private Button mRequestLocButton;
    private Button mLocationReq;
    private Button mMarkerButton;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient = null;
    private BitmapDescriptor mCurrentMarker;
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    private boolean isFirstLoc = true;// 是否首次定位
    private boolean isFirsCircle = true; //是否首次绘制圆
    private UiSettings uiSettings;
    private BDLocation location;
    private Marker mMarker;
    private Circle circle;
    public String placeStr; //位置信息
    private GeoCoder mSearch; //搜索模块
    public Toast mToast;
    private Context mContext;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.baidumap, viewGroup, false);

        mMapView = (MapView) view.findViewById(R.id.mapview);
        mRequestLocButton = (Button) view.findViewById(R.id.request);
        mLocationReq = (Button) view.findViewById(R.id.location_req);
        mMarkerButton = (Button) view.findViewById(R.id.marker);
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
        option.setScanSpan(0);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(geoListener);

        // common ui
        mRequestLocButton.setText("普通");
        mRequestLocButton.setOnClickListener(this);
        mLocationReq.setText("+");
        mLocationReq.setOnClickListener(this);
        mMarkerButton.setText("tag");
        mMarkerButton.setOnClickListener(this);
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

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mRequestLocButton) {
            onRequestLocation();
        }else if (v == mLocationReq) {
            mLocationClient.requestLocation();
        }else if (v == mMarkerButton) {
            addMarker();
        }
    }

    //地图模式转化
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
                //关闭俯视收拾
                uiSettings = mBaiduMap.getUiSettings();
                uiSettings.setOverlookingGesturesEnabled(false);
                uiSettings.setCompassEnabled(true);
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                break;
        }
    }

    //添加marker
    public void addMarker(){
        //添加marker
            double latitude = mBaiduMap.getLocationData().latitude;
            double longitude = mBaiduMap.getLocationData().longitude;
            mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(mCurrentMarker)
                    .zIndex(9);
            mMarker = (Marker) mBaiduMap.addOverlay(options);
            markerInfo();
    }

    //反向地理编码
    OnGetGeoCoderResultListener geoListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null ||
                    reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(mContext,"对不起，获取当前位置出错,请稍候再试！",Toast.LENGTH_LONG).show();
                return;
            }
            placeStr = reverseGeoCodeResult.getAddress();
            Toast.makeText(mContext,placeStr,Toast.LENGTH_LONG).show();
        }
    };

    //place info
    public Place placeInfo(String placegeo) {
        Place place = new Place();
        double latitude = mBaiduMap.getLocationData().latitude;
        double longitude = mBaiduMap.getLocationData().longitude;
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(new LatLng(latitude, longitude)));
        place.setGeoLocation(new AVGeoPoint(mBaiduMap.getLocationData().latitude
                , mBaiduMap.getLocationData().longitude));
        place.setPlaceName(placegeo);
        place.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    //Toast.makeText(mContext,"place保存成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext,"保存出错",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return place;
    }

    //marker information
    public void markerInfo(){
        Tag tag = new Tag();
        User user = AppHelper.getCurrentUser();
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        tag.setTitle("test");
        tag.setAppointTime(time);
        tag.setDetail("测试数据");
        tag.setUser(user);
        tag.setPlace(placeInfo(placeStr));
        Log.e("time", "" + time);

        //后台异步存储数据
        tag.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.e("saveok", "保存数据成功");
                } else {
                    Toast.makeText(mContext,"出错了讨厌啦",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //初始化Circleoption
    CircleOptions circleOptions = new CircleOptions();
    public void drawCircle(){
        double latitude = mBaiduMap.getLocationData().latitude;
        double longitude = mBaiduMap.getLocationData().longitude;
        circleOptions.center(new LatLng(latitude,longitude));
        circleOptions.fillColor(0x204DB6AC);
        circleOptions.radius(300);
        Stroke stroke = new Stroke(3, 0xff00796B);
        //设置圆边框信息
        circleOptions.stroke(stroke);
        //地图添加一个覆盖物,不再重绘覆盖物，大大节省资源
        if (isFirsCircle) {
            circle =(Circle) mBaiduMap.addOverlay(circleOptions);
            isFirsCircle = false;
        }else{
            circle.setCenter(new LatLng(latitude,longitude));
        }

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
                //设置初始化缩放级别
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(17));
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
            //绘制圆形覆盖物
            drawCircle();
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationClient.stop();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocationClient.stop();
        //销毁查询
        mSearch.destroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }
}
