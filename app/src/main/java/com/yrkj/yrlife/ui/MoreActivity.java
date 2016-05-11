package com.yrkj.yrlife.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.DensityUtil;
import com.yrkj.yrlife.utils.ImageUtils;
import com.yrkj.yrlife.utils.QRCodeUtil;
import com.yrkj.yrlife.utils.UIHelper;

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
    @ViewInject(R.id.qrcode)
    ImageView qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("更多");
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("更多");
//        MobclickAgent.onResume(this);
        filePath = preferences.getString("filepath","");
        if (filePath!=null&&filePath!=""&&!filePath.equals("")){
            qrcode.setImageBitmap(ImageUtils.getBitmap(MoreActivity.this,filePath));
        }else {
            //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String filepath="qr_1.png";
//                    "/data/data/com.yrkj.yrlife/files/"+
                    int widht= DensityUtil.dip2px(MoreActivity.this,120);
                    boolean success = QRCodeUtil.createQRImage(MoreActivity.this, "https://www.baidu.com/", 200, 200, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                            filepath);
                    if (success) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                qrcode.setImageBitmap(ImageUtils.getBitmap(MoreActivity.this,filepath));
                                //实例化Editor对象
                                SharedPreferences.Editor editor = preferences.edit();
                                //存入数据
                                editor.putString("filepath", filepath);
                                //提交修改
                                editor.commit();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    @Event(R.id.call_rl)
    private void onCallrlClick(View view) {
//        UIHelper.ToastMessage(this, "正在开发中...");
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "13165094770"));
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

    @Event(R.id.help_rl)
    private void onHelprlClick(View view){
        UIHelper.ToastMessage(this, "正在开发中...");
    }

    @Event( R.id.idea_rl)
    private void onIdearlClick(View view){
        UIHelper.ToastMessage(this, "正在开发中...");
    }

    @Event(R.id.about_rl)
    private void onAboutrlClick(View view){
        UIHelper.ToastMessage(this, "正在开发中...");
    }

    @Event(R.id.clean_rl)
    private void onCleanrl(View view){
        UIHelper.clearAppCache(this);
    }
    @Event(R.id.out_btn)
    private void outEvent(View view){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("secret_code","");
        URLs.secret_code="";
        editor.commit();
        UIHelper.ToastMessage(this, "成功退出当前帐号");
    }


    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("更多");
//        MobclickAgent.onPause(this);
    }
}
