package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
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
 * A login screen that offers login via email/password.
 */
@ContentView(value = R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.name_phone)
    private ClearEditText nameText;
    @ViewInject(value = R.id.password)
    private ClearEditText pwdText;
    @ViewInject(value = R.id.sign)
    private TextView sign;
    @ViewInject(value = R.id.login)
    private Button loginbtn;
    @ViewInject(R.id.wjpwd)
    private TextView wjpwd;
    @ViewInject(value = R.id.title)
    private TextView title;
    public String name;
    public String password;
    private String result;
    private ProgressDialog mLoadingDialog;
    SharedPreferences preferences;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the login form.
        x.view().inject(this);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, UIHelper.APP_ID, false);
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        init();
    }


    private void init() {
        title.setText("登录");
        sign.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        sign.getPaint().setAntiAlias(true);//抗锯齿
        wjpwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        wjpwd.getPaint().setAntiAlias(true);//抗锯齿
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在登录……");
        mLoadingDialog.setCancelable(false);
    }

    @Event(R.id.login)
    private void loginEvent(View view) {
        name = nameText.getText().toString();
        password = pwdText.getText().toString();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            if (StringUtils.isEmpty(name)) {
                UIHelper.CenterToastMessage(this, "请填写用户名");
            } else if (StringUtils.isEmpty(password)) {
                UIHelper.CenterToastMessage(this, "请填写密码");
            }
        } else {
            mLoadingDialog.show();
            login();
        }
    }

    @Event(R.id.wx_btn)
    private void wxbtnEvent(View view) {
        // send oauth request
        SendAuth.Req req=new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = ApiClient.getUserAgent(appContext);
        api.sendReq(req);
    }

    @Event(R.id.wjpwd)
    private void wjpwdEvent(View view) {
        Intent intent = new Intent(this, FindPasswordActivity.class);
        startActivity(intent);
    }

    @Event(R.id.sign)
    private void signEvent(View view) {
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
    }

    private void login() {
        RequestParams params = new RequestParams(URLs.LOGIN);
        params.addQueryStringParameter("conditions", name);
        params.addQueryStringParameter("pwd", password);
        params.addQueryStringParameter("unique_phone_code", appContext.getAppId());
        params.addQueryStringParameter("tokenAndFlag",UIHelper.token+"And");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String res) {
                Message msg = new Message();
                try {
                    JSONObject json = new JSONObject(res);
                    msg.what = json.getInt("code");
                    msg.obj = json.getString("message");
                    UIHelper.ToastMessage(appContext,msg.obj.toString());
                    if (msg.what == 1) {
                        result = json.getString("result");
                        User user = JsonUtils.fromJson(result, User.class);
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        //存入数据
                        if (StringUtils.isEmpty(user.getReal_name())) {
                            editor.putString("name", user.getAccount());
                        } else {
                            editor.putString("name", user.getReal_name());
                        }
                        if (StringUtils.isEmpty(user.getNick_name())){
                            editor.putString("nick_name", user.getNick_name());
                        }else {
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
                        if (!StringUtils.isEmpty(user.getWx_head_pic())) {
                            editor.putString("wx_head_image", user.getWx_head_pic());
                        } else {
                            editor.putString("wx_head_image", "");
                        }
                        if (!StringUtils.isEmpty(user.getSecret_code())) {
                            editor.putString("secret_code", user.getSecret_code());
                            URLs.secret_code = user.getSecret_code();
                        }
                        if (StringUtils.isEmpty(user.getIsBind())){
                            editor.putString("isBind",user.getIsBind());
                        }else {
                            editor.putString("isBind","");
                        }
                        if (user.getTotal_balance()==null){
                            editor.putFloat("money", 0);
                        }else {
                            editor.putFloat("money", user.getTotal_balance().floatValue());
                        }
                        if (!StringUtils.isEmpty(user.getIf_have_useful_coupon())){
                            editor.putString("if_have_useful_coupon",user.getIf_have_useful_coupon());
                        }else {
                            editor.putString("if_have_useful_coupon","0");
                        }
                        editor.putString("openid", "");
                        editor.putString("access_token", "");
                        editor.putString("refresh_token", "");
                        editor.putInt("jifen", user.getCard_total_point());
                        //提交修改
                        editor.commit();
                        finish();
                    } else if (msg.what == 2) {
                        mLoadingDialog.dismiss();
                    }

                } catch (JSONException e) {

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "error");
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });
    }
}

