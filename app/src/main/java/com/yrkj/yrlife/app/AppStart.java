package com.yrkj.yrlife.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.db.DbUtils;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.ShareActivity;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.SlideShowView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class AppStart extends AppCompatActivity {

    private static final int GO_HOME = 1000;

    private static final int GO_SHARE = 3000;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 2000;
    //是否是第一次使用
    private boolean isFirstUse;
    SharedPreferences preferences;
    YrApplication application;
    private boolean isFirstUseThis = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_appstart);
        application = (YrApplication) getApplication();
        //读取SharedPreferences中需要的数据
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        isFirstUse = preferences.getBoolean("isFirstUse", true);
        isFirstUseThis = preferences.getBoolean("isFirstUseThis", true);
        loadBanner();
        if (isFirstUseThis) {
            //实例化Editor对象
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("homePage", "");
            editor.putBoolean("isFirstUseThis",false);
            //提交修改
            editor.commit();
        } else {

        }
        final Message msg = new Message();
        isFirstUse = false;
        if (isFirstUse) {
            msg.what = GO_SHARE;
//                    mHandler.sendEmptyMessageDelayed(GO_SHARE, SPLASH_DELAY_MILLIS);
        } else {
            msg.what = GO_HOME;
//                    mHandler.sendEmptyMessageDelayed(GO_HOME,SPLASH_DELAY_MILLIS);
        }
        if (URLs.secret_code != "") {
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
                            final User user = JsonUtils.fromJson(result, User.class);
                            final SharedPreferences.Editor editor = preferences.edit();
                            //存入数据
                            editor.putInt("id", user.getId());
                            if (StringUtils.isEmpty(user.getReal_name())) {
                                editor.putString("name", user.getAccount());
                            } else {
                                editor.putString("name", user.getReal_name());
                            }
                            if (StringUtils.isEmpty(user.getNick_name())) {
                                editor.putString("nick_name", user.getNick_name());
                            } else {
                                editor.putString("nick_name", "");
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
                            if (!StringUtils.isEmpty(user.getHead_image())) {
                                editor.putString("head_image", user.getHead_image());
                            } else {
                                editor.putString("head_image", "");
                            }
                            if (!StringUtils.isEmpty(user.getSecret_code())) {
                                editor.putString("secret_code", user.getSecret_code());
                                URLs.secret_code = user.getSecret_code();
                            }
                            if (!StringUtils.isEmpty(user.getWx_head_pic())) {
                                editor.putString("wx_head_image", user.getWx_head_pic());
                            } else {
                                editor.putString("wx_head_image", "");
                            }
                            if (!StringUtils.isEmpty(user.getIsBind())) {
                                editor.putString("isBind", user.getIsBind());
                            } else {
                                editor.putString("isBind", "");
                            }
                            if (!StringUtils.isEmpty(user.getCard_number())) {
                                editor.putString("card_number", user.getCard_number());
                            } else {
                                editor.putString("card_number", "");
                            }
                            if (user.getTotal_balance() == null) {
                                editor.putFloat("money", 0);
                            } else {
                                editor.putFloat("money", user.getTotal_balance().floatValue());
                            }
                            if (!StringUtils.isEmpty(user.getIf_have_useful_coupon())) {
                                editor.putString("if_have_useful_coupon", user.getIf_have_useful_coupon());
                            } else {
                                editor.putString("if_have_useful_coupon", "0");
                            }
                            if (!StringUtils.isEmpty(user.getIsWashing())) {
                                editor.putString("isWashing", user.getIsWashing());
                            } else {
                                editor.putString("isWashing", "0");
                            }
                            editor.putInt("jifen", user.getCard_total_point());
                            //提交修改
                            editor.putString("user", jsonObject.getString("result"));
                            editor.commit();
                            boolean isEMClient = preferences.getBoolean("emclient", false);
                            if (!isEMClient) {
                                EMClient.getInstance().login(user.getId() + "", user.getId() + "0", new EMCallBack() {//回调
                                    @Override
                                    public void onSuccess() {
                                        EMClient.getInstance().groupManager().loadAllGroups();
                                        EMClient.getInstance().chatManager().loadAllConversations();
                                        editor.putBoolean("emclient", true);
                                        editor.commit();
                                        Log.d("main", "登录聊天服务器成功！");
                                    }

                                    @Override
                                    public void onProgress(int progress, String status) {

                                    }

                                    @Override
                                    public void onError(int code, String message) {
                                        if (code == 200) {
                                            editor.putBoolean("emclient", true);
                                            editor.commit();
                                        }
                                        Log.d("main", "登录聊天服务器失败！");
                                    }
                                });
                            }

                        } else if (msg.arg1 == 3) {
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
//                        mHandler.sendMessage(msg);
                    switch (msg.what) {
                        case GO_HOME:
                            goHome();
                            break;
                        case GO_SHARE:
                            goShare();
                            break;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    URLs.secret_code = "";
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("secret_code", "");
                    //提交修改
                    editor.commit();
                }
            });
        } else {
            try {
                Thread.sleep(SPLASH_DELAY_MILLIS);
                switch (msg.what) {
                    case GO_HOME:
                        goHome();
                        break;
                    case GO_SHARE:
                        goShare();
                        break;
                }
            } catch (InterruptedException e) {

            }
        }
//        RequestParams params = new RequestParams(URLs.HOME_ADS);
//        x.http().get(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String string) {
//                Result result=JsonUtils.fromJson(string,Result.class);
//                UIHelper.ToastMessage(application,result.Message());
//                if (result.OK()){
//                    UIHelper.img_urls=result.getImg_urls();
//                    SlideShowView.IMAGE_COUNT=result.count();
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                switch (msg.what) {
//                    case GO_HOME:
//                        goHome();
//                        break;
//                    case GO_SHARE:
//                        goShare();
//                        break;
//                }
//            }
//        });

    }

//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case GO_HOME:
//                    goHome();
//                    break;
//                case GO_SHARE:
//                    goShare();
//                    break;
//            }
////            super.handleMessage(msg);
//        }
//    };

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
    private void loadBanner() {
        RequestParams params = new RequestParams(URLs.APP_Banner);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    UIHelper.img_urls = result.getImg_urls();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
