package com.yrkj.yrlife.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.db.UserDao;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.ShareActivity;
import com.yrkj.yrlife.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AppStart extends AppCompatActivity {

    private static final int GO_HOME = 1000;

    private static final int GO_SHARE = 3000;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;
    //是否是第一次使用
    private boolean isFirstUse;
    SharedPreferences preferences;
    YrApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_appstart);
        application = (YrApplication) getApplication();
        //读取SharedPreferences中需要的数据
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        isFirstUse = preferences.getBoolean("isFirstUse", true);

        new Thread() {
            public void run() {
                Message msg = new Message();
                if (isFirstUse) {
                    msg.what = GO_SHARE;
//                    mHandler.sendEmptyMessageDelayed(GO_SHARE, SPLASH_DELAY_MILLIS);
                } else {
                    msg.what = GO_HOME;
//                    mHandler.sendEmptyMessageDelayed(GO_HOME,SPLASH_DELAY_MILLIS);
                }
                try {
                    String url = URLs.SECRET_CODE + URLs.secret_code;
                    String s = ApiClient.http_test(application, url);
                    JSONObject jsonObject=new JSONObject(s);
                    msg.arg1=jsonObject.getInt("code");
                    String message=jsonObject.getString("message");
                    if (msg.arg1==1){
                        User user= JsonUtils.fromJson(jsonObject.getString("result"),User.class);
                        UserDao.delete();
                        UserDao.insert(user);
                    }else if (msg.arg1==2){
                        URLs.secret_code="";
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("secret_code","");
                        //提交修改
                        editor.commit();
                    }
                } catch (JSONException e) {

                } catch (AppException e) {

                }
                mHandler.sendMessage(msg);
            }

            ;
        }.start();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart("appstart");
//        MobclickAgent.onResume(this);
//    }

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
//            super.handleMessage(msg);
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

//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("appstart");
//        MobclickAgent.onPause(this);
//    }
}
