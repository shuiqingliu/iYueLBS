package com.iyuelbs.ui.main;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
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
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
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
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.entity.Place;
import com.iyuelbs.entity.Tag;
import com.iyuelbs.entity.User;
import com.iyuelbs.support.widget.RoundedImageView;
import com.iyuelbs.ui.chat.service.CacheService;
import com.iyuelbs.ui.chat.ui.chat.ChatRoomActivity;
import com.iyuelbs.ui.settings.MyOrientationListener;
import com.iyuelbs.ui.settings.TimeCal;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xifan on 15-4-14.
 */
public class MapFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton tagBtn;
    private RecyclerView mRecyclerView;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    protected LocationClient mLocationClient = null;
    private BitmapDescriptor mCurrentMarker;
    private BitmapDescriptor mMarkerIcon;
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
    private boolean isFirstLoc = true;// 是否首次定位
    private boolean isFirsCircle = true; //是否首次绘制圆
    private UiSettings uiSettings;
    private BDLocation location;
    private Marker mMarker;
    private Circle circle;
    private LatLngBounds latLngBounds;
    private LatLng ne;
    private LatLng sw;
    public String placeStr; //位置信息
    private LatLng lastpostion;
    private GeoCoder mSearch; //搜索模块
    public Toast mToast;
    private Context mContext;
    private Map<String,String> mMarkerExist;
    public ArrayList<Place> placeId;
    private Marker lastmark;
    private boolean isFirst=true;
    private boolean isFirstMarker = true;
    public int nowNum;
    private int fabStatus = 2;
    protected SlidingUpPanelLayout mLayout;
    public MyOrientationListener myOrientationListener;
    private DisplayImageOptions mImageOptions;
    private ImageLoader mImageLoader;
    private RelativeLayout relativeLayout;
    private RoundedImageView userAvatar;
    private TextView tagTitle,tagDetial,tagTime;
    private User chatUser;
    private MaterialEditText tagTitleText, tagDetailText;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mContext = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.baidumap, viewGroup, false);
        mMapView = (MapView) view.findViewById(R.id.mapview);
        mMapView.showZoomControls(false);
        userAvatar = (RoundedImageView) view.findViewById(R.id.tag_avatar);
        tagTitle = (TextView) view.findViewById(R.id.tag_title);
        tagDetial = (TextView) view.findViewById(R.id.tag_detial);
        tagTime = (TextView) view.findViewById(R.id.tag_time);
        tagTitleText = (MaterialEditText) view.findViewById(R.id.tag_title_text);
        tagDetailText = (MaterialEditText) view.findViewById(R.id.tag_detial_text);
        tagBtn =(FloatingActionButton) view.findViewById(R.id.tag_btn);
        tagBtn.setColorNormal(R.color.pink);
        tagBtn.setIcon(R.drawable.ic_add_white_24dp);
        tagBtn.setColorNormalResId(R.color.pink_pressed);
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        mLayout.setTouchEnabled(false);
        //设置slidinguppanel支持锚点
        if (mLayout.getAnchorPoint() == 1.0f) {
            mLayout.setAnchorPoint(0.7f);
        }

        //marker采用相同图标节省资源，后续可以添加分类tag的图标
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
        mMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_selected_tag);
        //初始化map集合
        mBaiduMap = mMapView.getMap();

        //初始化Place ObjectID
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //设置默认为跟 随模式
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode,true,null));
        //方向传感器
        initOrientation();
        //slidingPanel监听器
        mLayout.setPanelSlideListener(panelListener);
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
        tagBtn.setOnClickListener(this);
        mBaiduMap.setOnMapStatusChangeListener(statusChangeListener);
        //设置事件监听
        mBaiduMap.setOnMapLoadedCallback(onMapLoadedCallback);
        //maker点击事件监听
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
        //地图点击事件
        mBaiduMap.setOnMapClickListener(onMapClickListener);
        return view;
    }

    @Override
    public void onClick(View v) {
      if (v == tagBtn) {
          switch (fabStatus){
              case 1:{
                  if (null == chatUser.getObjectId()){
                      Toast.makeText(mContext,"不能获得对方ID，暂不能聊天",Toast.LENGTH_SHORT).show();
                  }else{
                      if (AppHelper.getCurrentUser().getObjectId().equals(chatUser.getObjectId())) {
                          Toast.makeText(mContext, "对不起,您不可以和自己聊天！", Toast.LENGTH_SHORT).show();
                      }else {
                          List<AVUser> userList = new ArrayList<>();
                          userList.add(AppHelper.getCurrentUser());
                          userList.add(chatUser);
                          CacheService.registerUsers(userList);
                          List<String> userString = new ArrayList<>();
                          userString.add(chatUser.getObjectId());
                          userString.add(chatUser.getObjectId());
                          try {

                              CacheService.cacheUsers(userString);
                          }catch (AVException e){
                              e.printStackTrace();
                          }
                          ChatRoomActivity.chatByUserId(getActivity(), chatUser.getObjectId());
                      }
                  }

                  break;
              }
              case 2:{
                  if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                      expansion();
                      tagBtn.setColorNormal(R.color.purple);
                      tagBtn.setIcon(R.drawable.ic_send_white);
                      tagBtn.setColorNormalResId(R.color.deep_purple);
                      userAvatar.setVisibility(View.VISIBLE);
                      getImageLoader().displayImage(AppHelper.getCurrentUser().getAvatarUrl(), userAvatar, mImageOptions);
                      tagTitle.setText(R.string.add_tag);
                      tagDetial.setText("");
                      tagTime.setText("");
                  }
                  fabStatus = 3;
                  break;
              }
              case 3:{
                  addMarker();
                  break;
              }

          }
        }
    }

    //地图模式转化
    //将此功能以后放到设置里面
    private void onRequestLocation() {
        switch (mCurrentMode) {
            case NORMAL:
                //mRequestLocButton.setText("跟随");
                mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, null));
                break;
            case COMPASS:
                //mRequestLocButton.setText("普通");
                mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, null));
                break;
            case FOLLOWING:
                //mRequestLocButton.setText("罗盘");
                mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                //关闭俯视手势
                uiSettings = mBaiduMap.getUiSettings();
                uiSettings.setOverlookingGesturesEnabled(false);
                uiSettings.setCompassEnabled(true);
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, null));
                break;
        }
    }

    SlidingUpPanelLayout.PanelSlideListener panelListener = new SlidingUpPanelLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View view, float v) {
        }

        @Override
        public void onPanelCollapsed(View view) {
            tagBtn.setColorNormal(R.color.pink);
            tagBtn.setColorNormalResId(R.color.pink_pressed);
            tagBtn.setIcon(R.drawable.ic_add_white_24dp);
            fabStatus = 2;
        }

        @Override
        public void onPanelExpanded(View view) {
        }

        @Override
        public void onPanelAnchored(View view) {
        }

        @Override
        public void onPanelHidden(View view) {
        }

        @Override
        public void onPanelHiddenExecuted(View view, Interpolator interpolator, int i) {
        }

        @Override
        public void onPanelShownExecuted(View view, Interpolator interpolator, int i) {
        }

        @Override
        public void onPanelExpandedStateY(View view, boolean b) {
        }

        @Override
        public void onPanelCollapsedStateY(View view, boolean b) {
        }

        @Override
        public void onPanelLayout(View view, SlidingUpPanelLayout.PanelState panelState) {
        }
    };

    //展开slidinguppanel
    public void expansion(){
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    public void slidingClose(){
        if(mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            userAvatar.setVisibility(View.GONE);
            tagTitle.setText("");
            tagDetial.setText("");
            tagTime.setText("");
        }
    }

    //添加marker
    public void addMarker() {
        //添加marker
        double latitude = mBaiduMap.getLocationData().latitude;
        double longitude = mBaiduMap.getLocationData().longitude;
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
                Toast.makeText(mContext, "对不起，获取当前位置出错,请稍候再试！", Toast.LENGTH_LONG).show();
                return;
            }
            placeStr = reverseGeoCodeResult.getAddress();
            if (placeStr == null){
                Toast.makeText(mContext, mContext.getString(R.string.placeNull), Toast.LENGTH_SHORT).show();
            }
        }
    };

    //地图load完成回调函数,地图首次加载就绘制当前屏幕范围tag
    BaiduMap.OnMapLoadedCallback onMapLoadedCallback = new BaiduMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            drawTag();
//            if (isFirstMarker){
//                drawTag();
//                //isFirsCircle = false;
//            }
        }
    };

    //Marker事件监听
    BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (isFirst){
                marker.setIcon(mMarkerIcon);

            }else{
                lastmark.setIcon(mCurrentMarker);
                marker.setIcon(mMarkerIcon);
            }
            isFirst = false;
            lastmark = marker;
            userAvatar.setVisibility(View.VISIBLE);
            MapDataHelper mapDataHelper = (MapDataHelper) marker.getExtraInfo().get("mapdate");
            mapDataHelper.getUsername();
            mapDataHelper.getPlacename();
            mapDataHelper.getDate();
            chatUser = mapDataHelper.getUser();
            tagInfo(mapDataHelper);
            //设置fab 状态
            fabStatus = 1;
            tagBtn.setColorNormal(R.color.teal_dark);
            tagBtn.setIcon(R.drawable.ic_chat_white);
            tagBtn.setColorNormalResId(R.color.teal);
            return false;
        }
    };

    public void tagInfo(MapDataHelper mapDataHelper){
        getImageLoader().displayImage(mapDataHelper.getUserAv(), userAvatar, mImageOptions);
        tagTitle.setText(mapDataHelper.getTitle());
        tagDetial.setText(mapDataHelper.getDetail());
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        TimeCal timeCal = new TimeCal();
        String timeStr = timeCal.twoDateDistance(mapDataHelper.getDate(), time);
        tagTime.setText(timeStr);

    }

    private ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = AppHelper.getImageLoader();
            mImageOptions = AppHelper.getDefaultOptsBuilder().displayer(new SimpleBitmapDisplayer()).build();
        }
        return mImageLoader;
    }

    //Map单击事件
    BaiduMap.OnMapClickListener onMapClickListener = new BaiduMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                fabStatus = 2;
                tagBtn.setColorNormal(R.color.pink);
                tagBtn.setColorNormalResId(R.color.pink_pressed);
                tagBtn.setIcon(R.drawable.ic_add_white_24dp);
                if (lastmark != null) {
                        lastmark.setIcon(mCurrentMarker);
                }
                userAvatar.setVisibility(View.GONE);
                tagTitle.setText("");
                tagDetial.setText("");
                tagTime.setText("");
            }else if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED) {
                setPanelNull();
            }
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    };


    //place info
    public Place placeInfo(String placegeo) {
        double latitude = mBaiduMap.getLocationData().latitude;
        double longitude = mBaiduMap.getLocationData().longitude;
        //发起反地理编码查询
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(new LatLng(latitude, longitude)));
        Place place = new Place();
        place.setGeoLocation(new AVGeoPoint(mBaiduMap.getLocationData().latitude
                , mBaiduMap.getLocationData().longitude));
        place.setPlaceName(placegeo);
        return place;
    }

    //marker information
    public void markerInfo() {
        if (checkField()) {
            Tag tag = new Tag();
            User user = AppHelper.getCurrentUser();
            Calendar calendar = Calendar.getInstance();
            Date time = calendar.getTime();
            tag.setTitle(tagTitleText.getText().toString());
            tag.setDetail(tagDetailText.getText().toString());
            tag.setAppointTime(time);
            tag.setUser(user);
            tag.setPlace(placeInfo(placeStr));
            Log.e("time", "" + time);

            //后台异步存储数据
            if (placeStr != null) {
                tag.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.e("saveok", "保存数据成功");
                            Toast.makeText(mContext, "标签发表成功\n地点：" + placeStr, Toast.LENGTH_SHORT).show();
                            setPanelNull();
                        } else {
                            Toast.makeText(mContext, "发表标签出错，没关系再试一次！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(mContext,"网络不佳或定位失败，请重试。",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean checkField(){
        boolean valid = true;
        if (TextUtils.isEmpty(tagTitleText.getText())){
            tagTitleText.setError("标题不能为空！");
            valid = false;
        }
        return valid;
    }

    public void setPanelNull(){
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        userAvatar.setVisibility(View.GONE);
        tagTitle.setText("");
        tagDetial.setText("");
        tagTime.setText("");
        tagTitleText.setText("");
        tagDetailText.setText("");
    }

    //初始化Circleoption


    public void drawCircle(BDLocation bdloc) {
        CircleOptions circleOptions = new CircleOptions();
        //CircleOptions circleOptions = new CircleOptions();
        double latitude = bdloc.getLatitude();
        double longitude = bdloc.getLongitude();
        //double latitude = mBaiduMap.getLocationData().latitude;
        //double longitude = mBaiduMap.getLocationData().longitude;
        circleOptions.center(new LatLng(latitude, longitude));
        circleOptions.fillColor(0x204DB6AC);
        circleOptions.radius(300);
        Stroke stroke = new Stroke(3, 0xff00796B);
        //设置圆边框信息
        circleOptions.stroke(stroke);
        //地图添加一个覆盖物,不再重绘覆盖物，大大节省资源
        if (isFirsCircle) {
            circle = (Circle) mBaiduMap.addOverlay(circleOptions);
            isFirsCircle = false;
        } else {
            circle.setCenter(new LatLng(latitude, longitude));
        }
    }

    //监听地图状态变化
    BaiduMap.OnMapStatusChangeListener statusChangeListener
            = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus,int test) {
        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            drawTag();
        }
    };

    /**
    *从服务器获取tag数据并将数据与maker绑定开始
    */
    public void drawTag() {
    //获取当前地图范围
        //获取地图边界
        int b = mMapView.getBottom();
        int t = mMapView.getTop();
        int r = mMapView.getRight();
        int l = mMapView.getLeft();
        //将边界坐标转化为经纬度
        ne = mBaiduMap.getProjection().fromScreenLocation(new Point(r, t));
        sw = mBaiduMap.getProjection().fromScreenLocation(new Point(l, b));
        //创建地理范围
        latLngBounds = new LatLngBounds.Builder()
                .include(ne).include(sw).build();
        Log.e("northeast", ne.longitude + "," + ne.latitude);
        Log.e("sourthwest", sw.longitude + "," + sw.latitude);
        getMarkerData();
    }

    //将tag数据和marker绑定
    public void getTagObject(){
        //getMarkerData();
        Log.e("Place信息","大小:"+ placeId.size());
        //if (placeId.size() > 0){
            AVQuery<Tag> tagAVQuery = AVQuery.getQuery(Tag.class);
            for (Place j : placeId){
                Log.e("Place信息","k" + j);
            }
            tagAVQuery.whereContainedIn("place", placeId);
            tagAVQuery.findInBackground(new FindCallback<Tag>() {
                @Override
                public void done(List<Tag> list, AVException e) {
                    if (e == null){
                        mMarkerExist = new HashMap<>();
                        for (int i = 0;i< placeId.size();i++) {
                            Log.e("数据大小","dataSize:" + list.size());
                            //首先取得place和tag的基本数据
                            Log.e("Place","对象ID：" + list.get(i).getPlace());
                            double latitude = placeId.get(i).getGeoLocation().getLatitude();
                            double longitude = placeId.get(i).getGeoLocation().getLongitude();
                            MarkerOptions options = new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .icon(mCurrentMarker)
                                    .zIndex(9);
                            User user = list.get(i).getUser();
                            String tagid = list.get(i).getObjectId();
                            String placeid = placeId.get(i).getObjectId();
                            String placename = list.get(i).getPlace().getPlaceName();
                            String username = list.get(i).getUser().getUsername();
                            String title = list.get(i).getTitle();
                            String detial = list.get(i).getDetail();
                            String userav = list.get(i).getUser().getAvatarUrl();
                            Date date = list.get(i).getAppointTime();
                            MapDataHelper mapDataHelper = new MapDataHelper(user,username, title, detial, userav, date, placename);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mapdate",mapDataHelper);

                            //判断已经绘制到地图上的tag不再绘制节省资源。这块有bug
                            if (isFirstMarker) {
                                mMarker = (Marker) mBaiduMap.addOverlay(options);
                                mMarker.setExtraInfo(bundle);
                                mMarkerExist.put(tagid,placeid);
                                //TODO： 如何做到判断已经有overly的点不再重绘？？？
                                //将当前marker保存起来啊，然后判断LatLng！！！
                            } else if (mMarkerExist.get(tagid) == placeid) {
                                //TODO ： 上面判断条件也得加上用户判断和tag对象判断因为map的键值不能重复
                                //  所以有可能造成tag丢失，这存在BUG，
                            } else {
                                mMarker = (Marker) mBaiduMap.addOverlay(options);
                                mMarker.setExtraInfo(bundle);
                                mMarkerExist.put(tagid,placeid);
                            }

                        }
                    } else {
                        Log.e("tag","Draw Error");
                    }
                }
            });
        //}
    }

    //从lc获取maker数据集
    public void getMarkerData() {
        placeId = new ArrayList<>();
        AVQuery<Place> query = AVObject.getQuery(Place.class);
        query.whereWithinGeoBox("geoLocation",
                new AVGeoPoint(sw.latitude, sw.longitude), new AVGeoPoint(ne.latitude, ne.longitude));
        //不能设置1000灰常卡，当前缩放级没必要设置50足矣
        query.limit(50);
        query.findInBackground(new FindCallback<Place>() {
            @Override
            public void done(List<Place> list, AVException e) {
                if (e == null) {
                    for (Place placeObj : list) {
                        placeId.add(placeObj);
                        Log.e("添加","placie" + placeObj);
                    }
                    getTagObject();
                }
            }
        });
        System.out.println(placeId.size());
    }
    /**
     *从服务器获取tag数据并将数据与maker绑定结束
     */

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
                    .direction(nowNum).latitude(location.getLatitude())
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
            drawCircle(location);
        }
    }

    public int initOrientation(){
        myOrientationListener = new MyOrientationListener(mContext);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                nowNum = (int) x;
                new MyLocationData.Builder().direction(nowNum);
                //mLocationClient.requestLocation();
            }
        });
        return  nowNum;
    }

    @Override
    public void onStart() {
        super.onStart();
        myOrientationListener.start();
        //绘制圆形覆盖物
        //drawCircle();
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
    public void onStop() {
        super.onStop();
        myOrientationListener.stop();
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
