package com.yrkj.yrlife.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.ui.LoginActivity;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.ShareActivity;

public class AppStart extends AppCompatActivity {

    private static final int GO_HOME = 1000;

    private static final int GO_SHARE=3000;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;
    //是否是第一次使用
    private boolean isFirstUse;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_appstart);
        //读取SharedPreferences中需要的数据
        preferences = getSharedPreferences("yrlife",MODE_WORLD_READABLE);
        isFirstUse = preferences.getBoolean("isFirstUse", true);
        if (isFirstUse){
            mHandler.sendEmptyMessageDelayed(GO_SHARE, SPLASH_DELAY_MILLIS);
        }else {
            mHandler.sendEmptyMessageDelayed(GO_HOME,SPLASH_DELAY_MILLIS);
        }
    }
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_SHARE:
                    goShare();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        overridePendingTransition(R.anim.zoomin,
                R.anim.zoomout);
        this.finish();
    }
    private void goShare() {
        //实例化Editor对象
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putBoolean("isFirstUse", false);
        //提交修改
        editor.commit();
        Intent intent = new Intent(this, ShareActivity.class);
        this.startActivity(intent);
        overridePendingTransition(R.anim.zoomin,
                R.anim.zoomout);
        this.finish();
    }
}
