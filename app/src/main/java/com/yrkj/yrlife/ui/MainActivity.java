package com.yrkj.yrlife.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.PayConfirm;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.service.LocationService;
import com.yrkj.yrlife.ui.fragment.FragmentAd;
import com.yrkj.yrlife.ui.fragment.FragmentIndex;
import com.yrkj.yrlife.ui.fragment.FragmentMe;
import com.yrkj.yrlife.ui.fragment.FragmentNear;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.utils.UpdateManager;
import com.zxing.activity.CaptureActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.zip.Inflater;


@ContentView(value = R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    public static final int MY_PERMISSIONS_CAMERA = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
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
    private String mTextviewArray[] = {"主页", "洗车", "消息", "我的"};

    @ViewInject(R.id.title_home)
    TextView title_home;
    @ViewInject(R.id.location_text)
    TextView LocationResult;
    Message m = null;
    SharedPreferences preferences;
    private ProgressDialog mLoadingDialog;
    Washing_no_card_record wash;
    BigDecimal spend_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getAppManager().addActivity(this);
        x.view().inject(this);
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        initView();
//        locationService = new LocationService(getApplicationContext());
//        if (locationService == null) {
        insertDummyContactWrapper();
//        }

        application = (YrApplication) getApplication();
        boolean isWash = preferences.getBoolean("isWash", false);

        if (isWash) {
            String washstring = preferences.getString("wash_gson", "");
            wash = JsonUtils.fromJson(washstring, Washing_no_card_record.class);
            isWash();
        }

        //检测新版本
        //  UpdateManager.getUpdateManager().checkAppUpdate(this,false);
    }

    private void isWash() {
        String belongCode = preferences.getString("belongCode", "");
        String Machine_number = preferences.getString("Machine_number", "");
        RequestParams params = new RequestParams(URLs.IsWashing);
        params.addQueryStringParameter("belongCode", belongCode);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("machineNo", Machine_number);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    spend_money = result.spend_money();
                    dialog("您的洗车还未结束，是否继续洗车？");

                } else {
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isWash", false);
                    //提交修改
                    editor.commit();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void insertDummyContactWrapper() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            locationService = new LocationService(getApplicationContext());
        } else if (hasWriteContactsPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationService = new LocationService(getApplicationContext());
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setMessage("定位服务需要赋予使用的权限，不开启将无法正常工作！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startAppSettings();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).create();
                        dialog.show();
                        return;
                    }
                }
                break;
            case MY_PERMISSIONS_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mTabHost.setCurrentTab(2);
                } else {
                    //用户不同意，向用户展示该权限作用
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setMessage("该相机需要赋予使用的权限，不开启将无法正常工作！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startAppSettings();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).create();
                        dialog.show();
                        return;
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        mLoadingDialog = UIHelper.progressDialog(this, "正在努力加载...");
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
        mTabHost.getTabWidget().getChildTabViewAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(URLs.secret_code)) {
                    UIHelper.openLogin(MainActivity.this);
                } else {
                    mTabHost.setCurrentTab(3);
                }
            }
        });
        mTabHost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(URLs.secret_code)) {
                    UIHelper.openLogin(MainActivity.this);
                } else {
                    if (UIHelper.location == null) {
                        UIHelper.ToastMessage(MainActivity.this, "无法获取定位，无法使用此功能");
                        finish();
                    } else {
                        int perssion = ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.CAMERA);
                        if (perssion == PackageManager.PERMISSION_GRANTED) {
                            mTabHost.setCurrentTab(2);
                        } else if (perssion == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_CAMERA);
                        }
                    }
                }
            }
        });
        mTabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(URLs.secret_code)) {
                    UIHelper.openLogin(MainActivity.this);
                } else {
                    mTabHost.setCurrentTab(1);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
        if (locationService == null) {
            UIHelper.ToastMessage(MainActivity.this, "请到设置中打开定位，并允许程序获取定位权限");
        } else {
            if (!StringUtils.isEmpty(UIHelper.city)) {
                locationService.unregisterListener(mListener); //注销掉监听
                locationService.stop(); //停止定位服务
                LocationResult.setText(UIHelper.city);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String url = bundle.getString("result");
            Log.i("result", url);
            if (StringUtils.isUrl(url)) {
                UIHelper.showBrowser(this, url);
            } else {
                UIHelper.ToastMessage(this, url);
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
//        locationService = application.locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        if (locationService == null) {
//            UIHelper.ToastMessage(MainActivity.this, "请到设置中打开定位，并允许程序获取定位权限");
        } else {
            locationService.registerListener(mListener);
            locationService.start();// 定位SDK
            // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        }
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if (locationService == null) {
            UIHelper.ToastMessage(MainActivity.this, "请到设置中打开定位，并允许程序获取定位权限");
        } else {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
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
            Log.e("baidu", location.getLocType() + "");
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
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIHelper.city = "";
        //结束Activity&从堆栈中移除
//        AppManager.getAppManager().finishActivity(this);
    }

    private void dialog(String context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, WashActivity.class);
                intent.putExtra("result", wash.getMachine_number());
                intent.putExtra("spend_money", spend_money.floatValue());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mLoadingDialog.show();
                payconfirm();
            }
        });
        builder.create().show();
    }

    private void payconfirm() {
        RequestParams params = new RequestParams(URLs.PAYCONFIRM);
        params.setConnectTimeout(1000 * 30);
        params.addQueryStringParameter("machineNo", wash.getMachine_number());
        params.addQueryStringParameter("belongCode", wash.getBelong());
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                UIHelper.ToastMessage(MainActivity.this, result.Message());
                if (result.OK()) {
                    PayConfirm payconfirm = result.payconfirm();
                    float mon = wash.getTotal_money().floatValue() - payconfirm.getTotalmoney().floatValue();
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putFloat("money", mon);
                    editor.putBoolean("isWash", false);
                    //提交修改
                    editor.commit();
                } else {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(MainActivity.this, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(MainActivity.this, "error");
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });

    }
}
