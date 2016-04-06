package com.yrkj.yrlife.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yrkj.yrlife.R;
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

    @ViewInject(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("更多");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("更多");
        MobclickAgent.onResume(this);
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
        UIHelper.ToastMessage(this, "正在开发中...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("更多");
        MobclickAgent.onPause(this);
    }
}
