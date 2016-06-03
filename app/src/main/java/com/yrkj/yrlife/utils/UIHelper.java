package com.yrkj.yrlife.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.ui.BrowserActivity;
import com.yrkj.yrlife.ui.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;


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
    public static final String PARTNERID="1347776201";
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
    public static String OpenId = "";
    public static String access_token = "";
    public static String refresh_token = "";
    public static BigDecimal bigDecimal;
    public static String orderNumber="";
    public static String city = "";
    public static BDLocation location;
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
        context.startActivity(intent);
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


    public static void openLogin(Activity activity, boolean isMe) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("isme", isMe);
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
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"qddagu@126.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "爱会议Android客户端 - 错误报告");
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
            Toast toast = Toast.makeText(cont,
                    "再按一次退出亿人生活", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
//                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit(cont);
            AppManager.getAppManager().finishAllActivity();
            System.exit(0);
        }
    }
}
