package com.yrkj.yrlife.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.ui.BrowserActivity;
import com.yrkj.yrlife.ui.LoginActivity;
import com.yrkj.yrlife.ui.TestActivity;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


/**
 * 应用程序UI工具包：封装UI相关的一些操作
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class UIHelper {

    public final static int LISTVIEW_ACTION_INIT = 0x01;
    public final static int LISTVIEW_ACTION_REFRESH = 0x02;
    public final static int LISTVIEW_ACTION_SCROLL = 0x03;
    public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;

    public final static int LISTVIEW_DATA_MORE = 0x01;
    public final static int LISTVIEW_DATA_LOADING = 0x02;
    public final static int LISTVIEW_DATA_FULL = 0x03;
    public final static int LISTVIEW_DATA_EMPTY = 0x04;

    public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
    public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
    public final static int LISTVIEW_DATATYPE_POST = 0x03;
    public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
    public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
    public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
    public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;

    public final static int REQUEST_CODE_FOR_RESULT = 0x01;
    public final static int REQUEST_CODE_FOR_REPLY = 0x02;
    public static final String APP_ID = "wx4aca65f58fe822e2";//APP_ID是从网站申请的
    public static final String App_Secret = "e289ab5aba52ce52941ead32519859ca";//密钥是从网站申请的
    public static final String PARTNERID = "1347776201";
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
    public static String OpenId = "";
    public static String access_token = "";
    public static String refresh_token = "";
    public static BigDecimal bigDecimal;
    public static String orderNumber = "";
    public static String city = "";
    public static BDLocation location;
    public static String token;
    public static String[] img_urls = {};
    public static String[] wash_img_urls = {};
    public static int IMAGE_COUNT = 0;
    public static Washing_no_card_record washing_no_card_record;
    public static boolean isWash;
    public static int COUNT = 0;
    /**
     * 全局web样式
     */
    public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} " +
            "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} " +
            "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} " +
            "a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
    private static long mExitTime;


    /**
     * 判断是否已安装packageName应用程序
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 打开系统应用
     *
     * @param context
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 坐标转换
     *
     * @param sourceLatLng
     * @return
     */
    public static LatLng converter(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    public static ProgressDialog progressDialog(Activity context, String message) {
        ProgressDialog mLoadingDialog = new ProgressDialog(context);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage(message);
        mLoadingDialog.setCancelable(false);
        return mLoadingDialog;
    }

    public static void openNavigation(Context activity, LatLng p2, String name) {
//        Intent intent = new Intent(activity, NaviActivity.class);
//        intent.putExtra("lng", p2);
//        intent.putExtra("name", name);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
    }

    public static void openBaiduMap(double lat, double lon, String describle, Context activity) {
        try {
            StringBuilder loc = new StringBuilder();
            loc.append("intent://map/direction?origin=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append("我的位置");
            loc.append("&destination=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append(describle);
            loc.append("&mode=driving");
            loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = Intent.getIntent(loc.toString());
            if (isInstallPackage("com.baidu.BaiduMap")) {
                activity.startActivity(intent); //启动调用
                Log.e("GasStation", "百度地图客户端已经安装");
            } else {
                Log.e("GasStation", "没有安装百度地图客户端");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openGaoDeMap(double lon, double lat, String describle, Context activity) {
        try {
            double[] gd_lat_lon = bdToGaoDe(lat, lon, activity);
            StringBuilder loc = new StringBuilder();
            loc.append("androidamap://viewMap?sourceApplication=XX");
            loc.append("&poiname=");
            loc.append(describle);
            loc.append("&lat=");
            loc.append(gd_lat_lon[0]);
            loc.append("&lon=");
            loc.append(gd_lat_lon[1]);
            loc.append("&dev=0");
            Intent intent = Intent.getIntent(loc.toString());
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Navi_Local(Context context, LatLng latLng) {
//        Intent intent=new Intent(context, GDNaviActivity.class);
//        intent.putExtra("latlng",latLng);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

    public static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static double[] bdToGaoDe(double bd_lat, double bd_lon, Context context) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }


    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 加载显示图片
     *
     * @param imgView
     * @param imgURL
     * @param errMsg
     */
    public static void showLoadImage(final ImageView imgView, final String imgURL, final String errMsg) {

        if (imgView == null || StringUtils.isEmpty(imgURL)) return;

        //是否有缓存图片
        final String filename = FileUtils.getFileName(imgURL);
        //Environment.getExternalStorageDirectory();返回/sdcard
        String filepath = imgView.getContext().getFilesDir() + File.separator + filename;
        File file = new File(filepath);
        if (file.exists()) {
            Bitmap bmp = ImageUtils.getBitmap(imgView.getContext(), filename);
            imgView.setImageBitmap(bmp);
            imgView.setVisibility(View.VISIBLE);
            return;
        }

        //从网络获取&写入图片缓存
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1 && msg.obj != null) {
                    imgView.setImageBitmap((Bitmap) msg.obj);
                    imgView.setVisibility(View.VISIBLE);
                    try {
                        //写图片缓存
                        ImageUtils.saveImage(imgView.getContext(), filename, (Bitmap) msg.obj);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (!StringUtils.isEmpty(errMsg)) {
                    ToastMessage(imgView.getContext(), errMsg);
                }
            }
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    Bitmap bmp = ApiClient.getNetBitmap(imgURL);
                    msg.what = 1;
                    msg.obj = bmp;
                } catch (AppException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    static Bitmap bitmap;

    public static Bitmap loadBitmap(final String url, final Context context) {

        final String filename = FileUtils.getFileName(url);
        String filepath = context.getFilesDir() + File.separator + filename;
        File file = new File(filepath);
        if (file.exists()) {
            Bitmap bmp = ImageUtils.getBitmap(context, filename);
            return bmp;
        }
        //从网络获取&写入图片缓存
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1 && msg.obj != null) {
                    try {
                        bitmap = (Bitmap) msg.obj;
                        //写图片缓存
                        ImageUtils.saveImage(context, filename, (Bitmap) msg.obj);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    Bitmap bmp = ApiClient.getNetBitmap(url);
                    msg.what = 1;
                    msg.obj = bmp;
                } catch (AppException e) {

                }
            }
        }.start();
        return bitmap;
    }

    public static void openTestActivity(Activity activity) {
        Intent intent = new Intent(activity, TestActivity.class);
        activity.startActivity(intent);
    }

    public static void openLogin(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 内部浏览器打开
     *
     * @param activity
     * @param url
     */
    public static void showBrowser(Activity activity, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri, activity, BrowserActivity.class);
            activity.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            ToastMessage(activity, "无法浏览此网页", 500);
        }
    }

    public static void showBrowser(Context activity, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri, activity, BrowserActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            ToastMessage(activity, "无法浏览此网页", 500);
        }
    }


    /**
     * 打开浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            ToastMessage(context, "无法浏览此网页", 500);
        }
    }

    /**
     * 获取TextWatcher对象
     *
     * @param context
     * @param tmlKey
     * @return
     */
    public static TextWatcher getTextWatcher(final Activity context, final String tmlKey) {
        return new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //保存当前EditText正在编辑的内容
                ((YrApplication) context.getApplication()).setProperty(tmlKey, s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    /**
     * 弹出Toast消息
     *
     * @param msg
     */
    public static void ToastMessage(Context cont, String msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, int msg) {
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, String msg, int time) {
        Toast.makeText(cont, msg, time).show();
    }

    public static void CenterToastMessage(Context cont, String msg) {
        Toast toast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void CenterToastMessage(Context cont, String msg, int time) {
        Toast toast = Toast.makeText(cont, msg, time);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 点击返回监听事件
     *
     * @param activity
     * @return
     */
    public static View.OnClickListener finish(final Activity activity) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                activity.finish();
            }
        };
    }

    /**
     * 发送App异常崩溃报告
     *
     * @param cont
     * @param crashReport
     */
    public static void sendAppCrashReport(final Context cont, final String crashReport) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.app_error);
        builder.setMessage(R.string.app_error_message);
        builder.setPositiveButton(R.string.submit_report, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //发送异常报告
                Intent i = new Intent(Intent.ACTION_SEND);
                //i.setType("text/plain"); //模拟器
                i.setType("message/rfc822"); //真机
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"yirenlovekeji@163.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "亿人生活Android客户端 - 错误报告");
                i.putExtra(Intent.EXTRA_TEXT, crashReport);
                cont.startActivity(Intent.createChooser(i, "发送错误报告"));
                //退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.show();
    }


    /**
     * 清除app缓存
     *
     * @param activity
     */
    public static void clearAppCache(Activity activity) {
        final YrApplication ac = (YrApplication) activity.getApplication();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    ToastMessage(ac, "缓存清除成功");
                } else {
                    ToastMessage(ac, "缓存清除失败");
                }
            }
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    ac.clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 退出程序
     *
     * @param cont
     */
    public static void Exit(final Context cont) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        //builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.prompt);
        builder.setMessage(R.string.app_menu_surelogout);
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //退出
                AppManager.getAppManager().AppExit(cont);
            }
        });
        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 双击退出程序
     *
     * @param cont
     */
    public static void onExit(final Context cont) {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
//            Toast toast = Toast.makeText(cont,
//                    "再按一次退出亿人生活", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
            CenterToastMessage(cont, "再按一次退出亿人生活");
//                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit(cont);
//            AppManager.getAppManager().finishAllActivity();
//            System.exit(0);
            Log.d("out", "finish");
        }
    }
}
