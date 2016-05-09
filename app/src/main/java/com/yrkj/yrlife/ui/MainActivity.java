package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.service.LocationService;
import com.yrkj.yrlife.ui.fragment.FragmentAd;
import com.yrkj.yrlife.ui.fragment.FragmentIndex;
import com.yrkj.yrlife.ui.fragment.FragmentMe;
import com.yrkj.yrlife.ui.fragment.FragmentNear;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.zxing.activity.CaptureActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@ContentView(value = R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    private LocationService locationService;//定位
    YrApplication application;
    // 定义FragmentTabHost对象
    private FragmentTabHost mTabHost;
    // 定义一个布局
    private LayoutInflater layoutInflater;
    // 定义数组来存放Fragment界面
    private Class fragmentArray[] = {FragmentIndex.class,
            FragmentNear.class, FragmentAd.class, FragmentMe.class,};

    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.main_tab_item_home,
            R.drawable.main_tab_item_category, R.drawable.main_tab_item_down,
            R.drawable.main_tab_item_user};
    // Tab选项卡的文字
    private String mTextviewArray[] = {"主页", "附近", "卡券", "我的"};

    @ViewInject(R.id.title_home)
    TextView title_home;
    @ViewInject(R.id.location_text)
    TextView LocationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getAppManager().addActivity(this);
//      setContentView(R.layout.activity_main);
        x.view().inject(this);
        initView();
        application = (YrApplication) getApplication();


        Log.i("uuid", application.getAppId());


        //检测新版本
//        UpdateManager.getUpdateManager().checkAppUpdate(this,false);
    }


    /**
     * 初始化组件
     */
    private void initView() {
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());

        //设置字体
        AssetManager mgr = this.getAssets();//得到AssetManager
        Typeface face = Typeface.createFromAsset(mgr, "fonts/mini.ttf");
        title_home.setTypeface(face);

        // 实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.mipmap.ic_bg_btm);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
        if (UIHelper.city != "" && !UIHelper.city.equals("")) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
            LocationResult.setText(UIHelper.city);
        }
    }

    @Event(R.id.sweep)
    private void sweepEvent(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String url = bundle.getString("result");
            Log.i("result",url);
           if (StringUtils.isUrl(url)){
               UIHelper.showBrowser(this,url);
           }else {
               UIHelper.ToastMessage(this,url);
           }

        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);
        return view;
    }

    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (LocationResult != null)
                LocationResult.setText(str);
            UIHelper.city = str;
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService = application.locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                    sb.append("gps定位成功");
                    UIHelper.location = location;
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
//                    sb.append("网络定位成功");
                    UIHelper.location = location;
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                    sb.append("离线定位成功");
                    UIHelper.location = location;
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    UIHelper.ToastMessage(MainActivity.this, "服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    UIHelper.ToastMessage(MainActivity.this, "网络异常导致定位失败，请检查网络是否通畅，尝试重新请求定位。");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    UIHelper.ToastMessage(MainActivity.this, "无法获取有效定位，请检查网络是否通畅，尝试重新请求定位。");
                }
                if (StringUtils.isEmpty(location.getDistrict())) {
                    logMsg(location.getCity());
                } else {
                    logMsg(location.getDistrict());
                }

            }
        }

    };

    /**
     * 监听返回--是否退出程序
     */
    private static long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //是否退出应用
            UIHelper.onExit(this);
        } else {
            flag = super.onKeyDown(keyCode, event);
        }
        return flag;
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIHelper.city = "";
        //结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }
}
