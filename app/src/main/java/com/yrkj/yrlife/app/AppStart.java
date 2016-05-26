package com.yrkj.yrlife.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.ShareActivity;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_appstart);
        application = (YrApplication) getApplication();
        //读取SharedPreferences中需要的数据
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        isFirstUse = preferences.getBoolean("isFirstUse", true);

        new Thread() {
            public void run() {
                final Message msg = new Message();
                if (isFirstUse) {
                    msg.what = GO_SHARE;
//                    mHandler.sendEmptyMessageDelayed(GO_SHARE, SPLASH_DELAY_MILLIS);
                } else {
                    msg.what = GO_HOME;
//                    mHandler.sendEmptyMessageDelayed(GO_HOME,SPLASH_DELAY_MILLIS);
                }
                String url = URLs.SECRET_CODE;
                RequestParams params = new RequestParams(url);
                params.addQueryStringParameter("secret_code", URLs.secret_code);
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String res) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            msg.arg1 = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            if (msg.arg1 == 1) {
                               String result = jsonObject.getString("result");
                                User user = JsonUtils.fromJson(result, User.class);
                                SharedPreferences.Editor editor = preferences.edit();
                                //存入数据
                                if (StringUtils.isEmpty(user.getReal_name())) {
                                    editor.putString("name", user.getAccount());
                                } else {
                                    editor.putString("name", user.getReal_name());
                                }
                                editor.putString("phone", user.getPhone());
                                if (!StringUtils.isEmpty(user.getSex())) {
                                    if (user.getSex().equals("1")) {
                                        editor.putString("sex", "男");
                                    } else if (user.getSex().equals("0")) {
                                        editor.putString("sex", "女");
                                    }
                                } else {
                                    editor.putString("sex", "男");
                                }
                                if (!StringUtils.isEmpty(user.getHead_image())){
                                    editor.putString("head_image",user.getHead_image());
                                }else{
                                    editor.putString("head_image","");
                                }
                                if (!StringUtils.isEmpty(user.getSecret_code())){
                                    editor.putString("secret_code",user.getSecret_code());
                                    URLs.secret_code=user.getSecret_code();
                                }
                                if (!StringUtils.isEmpty(user.getWx_head_pic())){
                                    editor.putString("wx_head_image",user.getWx_head_pic());
                                }else{
                                    editor.putString("wx_head_image","");
                                }
                                if (StringUtils.isEmpty(user.getIsBind())){
                                    editor.putString("isBind",user.getIsBind());
                                }else {
                                    editor.putString("isBind","");
                                }
                                editor.putLong("money", user.getTotal_balance().longValue());
                                editor.putInt("jifen", user.getCard_total_point());
                                //提交修改
                                editor.putString("user", jsonObject.getString("result"));
                                editor.commit();
                            } else if (msg.arg1 == 2) {
                                URLs.secret_code = "";
                                //实例化Editor对象
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("secret_code", "");
                                //提交修改
                                editor.commit();
                            }
                        } catch (JSONException e) {

                        }

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }
                });
                mHandler.sendMessage(msg);
            }

            ;
        }.start();
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
