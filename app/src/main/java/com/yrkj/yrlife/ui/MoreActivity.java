package com.yrkj.yrlife.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.DensityUtil;
import com.yrkj.yrlife.utils.ImageUtils;
import com.yrkj.yrlife.utils.QRCodeUtil;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/3/28.
 */
@ContentView(R.layout.activity_more)
public class MoreActivity extends BaseActivity {

    SharedPreferences preferences;
    String filePath;

    @ViewInject(R.id.title)
    TextView title;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("更多");
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
    }

    @Event(R.id.me_rl)
    private void merlEvent(View v) {
        if (URLs.secret_code == "") {
            UIHelper.openLogin(this);
        } else {
            Intent intent = new Intent(this, MeActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 客服
     *
     * @param view
     */
    @Event(R.id.call_rl)
    private void onCallrlClick(View view) {
//        UIHelper.ToastMessage(this, "正在开发中...");
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4001891668"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    /**
     * 常见问题
     *
     * @param view
     */
    @Event(R.id.help_rl)
    private void onHelprlClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    /**
     * 意见反馈
     *
     * @param view
     */
    @Event(R.id.idea_rl)
    private void onIdearlClick(View view) {
//        UIHelper.ToastMessage(this, "正在开发中...");
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.openLogin(this);
        } else {
            Intent intent = new Intent(this, IdeaActivity.class);
            startActivity(intent);
        }
    }

    @Event(R.id.update_rl)
    private void updaterl(View view) {

    }

    /**
     * 关于我们
     *
     * @param view
     */
    @Event(R.id.about_rl)
    private void onAboutrlClick(View view) {
//        UIHelper.ToastMessage(this, "正在开发中...");
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 清除缓存
     *
     * @param view
     */
    @Event(R.id.clean_rl)
    private void onCleanrl(View view) {
        dialog("您确定要清除缓存吗？", 2);

    }

    /**
     * 退出登录
     *
     * @param view
     */
    @Event(R.id.out_btn)
    private void outEvent(View view) {
        if (StringUtils.isEmpty(URLs.secret_code)) {
            UIHelper.ToastMessage(this, "你还未登录");
        } else {
            dialog("您确定要退出登录吗？", 1);
        }
    }


    private void loginOut() {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("secret_code", "");
                    URLs.secret_code = "";
                    editor.commit();
                    UIHelper.ToastMessage(appContext, msg.obj.toString());
                } else if (msg.what == 2) {
                    UIHelper.ToastMessage(appContext, msg.obj.toString());
                }
            }

            ;
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                String url = URLs.LOGINOUT + URLs.secret_code;
                try {
                    result = ApiClient.http_test(appContext, url);
                    JSONObject jsonObject = new JSONObject(result);
                    msg.what = jsonObject.getInt("code");
                    msg.obj = jsonObject.getString("message");
                } catch (AppException e) {

                } catch (JSONException e) {

                }
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    protected void dialog(String context, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (i == 1) {
                    loginOut();
                }
                if (i == 2) {
                    UIHelper.clearAppCache(MoreActivity.this);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
