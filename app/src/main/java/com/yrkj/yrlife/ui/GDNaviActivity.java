//package com.yrkj.yrlife.ui;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.amap.api.maps.CoordinateConverter;
//import com.amap.api.navi.AMapNavi;
//import com.amap.api.navi.AMapNaviListener;
//import com.amap.api.navi.AMapNaviView;
//import com.amap.api.navi.AMapNaviViewListener;
//import com.amap.api.navi.enums.NaviType;
//import com.amap.api.navi.model.AMapLaneInfo;
//import com.amap.api.navi.model.AMapNaviCross;
//import com.amap.api.navi.model.AMapNaviInfo;
//import com.amap.api.navi.model.AMapNaviLocation;
//import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
//import com.amap.api.navi.model.AimLessModeCongestionInfo;
//import com.amap.api.navi.model.AimLessModeStat;
//import com.amap.api.navi.model.NaviInfo;
//import com.amap.api.navi.model.NaviLatLng;
//import com.autonavi.tbt.TrafficFacilityInfo;
//import com.baidu.mapapi.model.LatLng;
//import com.yrkj.yrlife.R;
//import com.yrkj.yrlife.utils.TTSController;
//import com.yrkj.yrlife.utils.UIHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by cjn on 2016/7/22.
// */
//public class GDNaviActivity extends BaseActivity implements AMapNaviListener, AMapNaviViewListener {
//
//    protected AMapNaviView mAMapNaviView;
//    protected AMapNavi mAMapNavi;
//    protected TTSController mTtsManager;
//    protected NaviLatLng mEndLatlng = new NaviLatLng(36.152617, 120.490294);
//    protected NaviLatLng mStartLatlng = new NaviLatLng(36.271277, 120.486113);
//    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
//    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
//    protected List<NaviLatLng> mWayPointList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        LatLng latLng=(LatLng) getIntent().getExtras().get("latlng");
//        com.amap.api.maps.model.LatLng latLng1=new com.amap.api.maps.model.LatLng(latLng.latitude,latLng.longitude);
//        CoordinateConverter converter  = new CoordinateConverter(appContext);
//        // CoordType.GPS 待转换坐标类型
//        converter.from(CoordinateConverter.CoordType.BAIDU);
//        // sourceLatLng待转换坐标点
//        converter.coord(latLng1);
//        // 执行转换操作
//        com.amap.api.maps.model.LatLng desLatLng = converter.convert();
//        mEndLatlng=new NaviLatLng(desLatLng.latitude,desLatLng.longitude);
//        latLng1=new com.amap.api.maps.model.LatLng(UIHelper.location.getLatitude(),UIHelper.location.getLongitude());
//        // sourceLatLng待转换坐标点
//        converter.coord(latLng1);
//        // 执行转换操作
//        com.amap.api.maps.model.LatLng desLatLng1 = converter.convert();
//        mStartLatlng=new NaviLatLng(desLatLng1.latitude,desLatLng1.longitude);
//
//
//        //实例化语音引擎
//        mTtsManager = TTSController.getInstance(getApplicationContext());
//        mTtsManager.init();
//        mTtsManager.startSpeaking();
//
//        //
//        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
//        mAMapNavi.addAMapNaviListener(this);
//        mAMapNavi.addAMapNaviListener(mTtsManager);
//
//        //设置模拟导航的行车速度
////        mAMapNavi.setEmulatorNaviSpeed(75);
//
//        sList.add(mStartLatlng);
//        eList.add(mEndLatlng);
//        setContentView(R.layout.activity_gdnavi);
//        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
//        mAMapNaviView.onCreate(savedInstanceState);
//        mAMapNaviView.setAMapNaviViewListener(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mAMapNaviView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mAMapNaviView.onPause();
//
////        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
//        mTtsManager.stopSpeaking();
////
////        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
////        mAMapNavi.stopNavi();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mAMapNaviView.onDestroy();
//        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
//        mAMapNavi.stopNavi();
//        mAMapNavi.destroy();
//        mTtsManager.destroy();
//    }
//
//    @Override
//    public void onInitNaviFailure() {
//        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onInitNaviSuccess() {
//        /**
//         * 方法:
//         *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
//         * 参数:
//         * @congestion 躲避拥堵
//         * @avoidhightspeed 不走高速
//         * @cost 避免收费
//         * @hightspeed 高速优先
//         * @multipleroute 多路径
//         *
//         * 说明:
//         *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
//         * 注意:
//         *      不走高速与高速优先不能同时为true
//         *      高速优先与避免收费不能同时为true
//         */
//        int strategy=0;
//        try {
//            strategy=mAMapNavi.strategyConvert(true, false, false, false, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList,strategy);
//    }
//
//    @Override
//    public void onStartNavi(int type) {
//    }
//
//    @Override
//    public void onTrafficStatusUpdate() {
//
//    }
//
//    @Override
//    public void onLocationChange(AMapNaviLocation location) {
//
//    }
//
//    @Override
//    public void onGetNavigationText(int type, String text) {
//
//    }
//
//    @Override
//    public void onEndEmulatorNavi() {
//    }
//
//    @Override
//    public void onArriveDestination() {
//    }
//
//    @Override
//    public void onCalculateRouteSuccess() {
//        mAMapNavi.startNavi(NaviType.GPS);//设置是否是模拟导航
//    }
//
//    @Override
//    public void onCalculateRouteFailure(int errorInfo) {
//        Toast.makeText(this,errorInfo+"",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onReCalculateRouteForYaw() {
//
//    }
//
//    @Override
//    public void onReCalculateRouteForTrafficJam() {
//
//    }
//
//    @Override
//    public void onArrivedWayPoint(int wayID) {
//
//    }
//
//    @Override
//    public void onGpsOpenStatus(boolean enabled) {
//    }
//
//    @Override
//    public void onNaviSetting() {
//    }
//
//    @Override
//    public void onNaviMapMode(int isLock) {
//
//    }
//
//    @Override
//    public void onNaviCancel() {
//        finish();
//    }
//
//
//    @Override
//    public void onNaviTurnClick() {
//
//    }
//
//    @Override
//    public void onNextRoadClick() {
//
//    }
//
//
//    @Override
//    public void onScanViewButtonClick() {
//    }
//
//    @Deprecated
//    @Override
//    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
//    }
//
//    @Override
//    public void onNaviInfoUpdate(NaviInfo naviinfo) {
//    }
//
//    @Override
//    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
//
//    }
//
//    @Override
//    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
//
//    }
//
//    @Override
//    public void showCross(AMapNaviCross aMapNaviCross) {
//    }
//
//    @Override
//    public void hideCross() {
//    }
//
//    @Override
//    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
//
//    }
//
//    @Override
//    public void hideLaneInfo() {
//
//    }
//
//    @Override
//    public void onCalculateMultipleRoutesSuccess(int[] ints) {
//
//    }
//
//    @Override
//    public void notifyParallelRoad(int i) {
//
//    }
//
//    @Override
//    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
//
//    }
//
//    @Override
//    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
//
//    }
//
//
//    @Override
//    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
//
//    }
//
//
//    @Override
//    public void onLockMap(boolean isLock) {
//    }
//
//    @Override
//    public void onNaviViewLoaded() {
//        Log.d("wlx", "导航页面加载成功");
//        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
//    }
//
//    @Override
//    public boolean onNaviBackClick() {
//        return false;
//    }
//
//
//}
