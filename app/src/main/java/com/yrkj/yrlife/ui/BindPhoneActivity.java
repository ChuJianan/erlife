package com.yrkj.yrlife.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.TimeCount;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/6/15.
 */
@ContentView(R.layout.activity_bind_phone)
public class BindPhoneActivity extends Activity {

    @ViewInject(R.id.back)
    LinearLayout back;
    @ViewInject(R.id.phonenub)
    private ClearEditText phoneEdit;
    @ViewInject(R.id.yzm)
    private ClearEditText yzmEdit;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.hq_code)
    private Button codeBtn;

    String phone;
    String yzm;
    String code;
    private CountDownTimer timer;
    private ProgressDialog mLoadingDialog;
    private String result;
    boolean isT = true;
    String params;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        back.setVisibility(View.INVISIBLE);
        title.setText("绑定手机号");
        init();
    }

    private void init() {
        params = getIntent().getStringExtra("params");
        timer = new TimeCount(60000, 1000, codeBtn);
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        mLoadingDialog = UIHelper.progressDialog(this, "正在获取验证码，请稍后...");
    }

    @Event(R.id.hq_code)
    private void hqyzmEvent(View view) {
        phone = phoneEdit.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            UIHelper.ToastMessage(this, "请输入手机号");
        } else if (StringUtils.isMobileNO(phone)) {
            timer.start();
            mLoadingDialog.show();
            getCode(phone);
            UIHelper.ToastMessage(this, "正在请求..");
        } else {
            UIHelper.ToastMessage(this, "请输入正确的手机号");
        }
    }

    @Event(R.id.next_step)
    private void nextstepEvent(View view) {
        yzm = yzmEdit.getText().toString();
        if (isT) {
            if (StringUtils.isEmpty(phone)) {
                UIHelper.ToastMessage(this, "请输入手机号");
            } else if (StringUtils.isMobileNO(phone)) {
                if (StringUtils.isEmpty(yzm)) {
                    UIHelper.ToastMessage(this, "请输入验证码");
                } else {
                    if (yzm.equals(code)) {
                        isT = false;
                        yzm = "";
                        yzmEdit.setText(yzm);
                        timer.onFinish();
                        timer.cancel();
                        codeBtn.setText("获取验证码");
                        mLoadingDialog = UIHelper.progressDialog(this, "正在加载，请稍后...");
                        mLoadingDialog.show();
                        setUser();
                    } else {
                        UIHelper.ToastMessage(this, "请输入正确的验证码");
                    }
                }
            }
        } else {
            UIHelper.ToastMessage(BindPhoneActivity.this, "验证码已经过期，请重新获取");
            isT = true;
        }
    }

    private void getCode(final String phone) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                mLoadingDialog.dismiss();
                if (msg.what == 1) {
                    UIHelper.ToastMessage(BindPhoneActivity.this, msg.obj.toString());

                } else if (msg.what == 2) {
                    UIHelper.ToastMessage(BindPhoneActivity.this, msg.obj.toString());
                    timer.onFinish();
                    timer.cancel();
                    codeBtn.setText("获取验证码");
                }
            }

            ;
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    String url = URLs.CODE_GET_B + phone;
                    result = ApiClient.http_test((YrApplication) BindPhoneActivity.this.getApplication(), url);
                    JSONObject jsonObject = new JSONObject(result);
                    msg.what = jsonObject.getInt("code");
                    msg.obj = jsonObject.getString("message");
                    if (msg.what == 1) {
                        code = jsonObject.getString("result");
                    }
                } catch (AppException e) {
                    e.printStackTrace();
                } catch (JSONException e) {

                }
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    User user;

    private void setUser() {
        final RequestParams params = new RequestParams(URLs.Thread_Login_JUDGE + this.params);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("code", code);
        params.addQueryStringParameter("tokenAndFlag", UIHelper.token + "And");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    int code = json.getInt("code");
                    String message = json.getString("message");
                    if (code == 1) {
                        user = JsonUtils.fromJson(json.getString("result"), User.class);
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        //存入数据
                        editor.putInt("id", user.getId());
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
                        if (!StringUtils.isEmpty(user.getHead_image())) {
                            editor.putString("head_image", user.getHead_image());
                        } else {
                            editor.putString("head_image", "");
                        }
                        if (!StringUtils.isEmpty(user.getWx_head_pic())) {
                            editor.putString("wx_head_image", user.getWx_head_pic());
                        } else {
                            editor.putString("wx_head_image", "");
                        }
                        if (!StringUtils.isEmpty(user.getSecret_code())) {
                            editor.putString("secret_code", user.getSecret_code());
                            URLs.secret_code = user.getSecret_code();
                        }
                        if (StringUtils.isEmpty(user.getIsBind())) {
                            editor.putString("isBind", user.getIsBind());
                        } else {
                            editor.putString("isBind", "");
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
                        editor.putInt("jifen", user.getCard_total_point());
                        //提交修改
                        editor.commit();
                        mLoadingDialog.dismiss();
                        //                            user.getId()+"", user.getId()+"0"
                        EMClient.getInstance().login(user.getId()+"", user.getId()+"0", new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                Log.d("main", "登录聊天服务器成功！");
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                                Log.d("main", "登录聊天服务器失败！");
                            }
                        });

                        finish();
                    } else {
                        mLoadingDialog.dismiss();
                        timer.onFinish();
                        timer.cancel();
                        codeBtn.setText("获取验证码");
                    }
                } catch (JSONException e) {

                }
                AppManager.getAppManager().finishActivity(LoginActivity.class);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(BindPhoneActivity.this, ex.getMessage());
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(BindPhoneActivity.this, "error");
                finish();
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
