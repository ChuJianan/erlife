package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
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

@ContentView(R.layout.activity_sign)
public class SignActivity extends BaseActivity {

    @ViewInject(R.id.phonenub)
    private ClearEditText phoneEdit;
    @ViewInject(R.id.yzm)
    private ClearEditText yzmEdit;
    @ViewInject(R.id.username)
    private ClearEditText nameEdit;
    @ViewInject(R.id.pwd)
    private ClearEditText pwdEdit;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.hq_code)
    private Button codeBtn;
    String phone;
    String yzm;
    String name;
    String pwd;
    String code;
    private CountDownTimer timer;
    private ProgressDialog mLoadingDialog;
    private String result;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        title.setText("注册");
        timer = new TimeCount(60000, 1000, codeBtn);
        init();
    }

    private void init() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在加载，请稍候…");
        mLoadingDialog.setCancelable(false);
    }

    @Event(R.id.signbtn)
    private void sigbtnEvent(View view) {
        phone = phoneEdit.getText().toString();
        yzm = yzmEdit.getText().toString();
        name = nameEdit.getText().toString();
        pwd = pwdEdit.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            UIHelper.ToastMessage(this, "请输入手机号");
        } else if (StringUtils.isMobileNO(phone)) {
            if (StringUtils.isEmpty(yzm)) {
                UIHelper.ToastMessage(this, "请输入验证码");
            } else {
                if (yzm.equals(code)) {
                    if (StringUtils.isEmpty(name)) {
                        UIHelper.ToastMessage(this, "请输入密码");
                    } else {
                        if (StringUtils.isEmpty(pwd)) {
                            UIHelper.ToastMessage(this, "请再次输入密码");
                        } else if (name.equals(pwd)){
                            if (pwd.length() >= 6) {
                                mLoadingDialog.show();
                                getSign();
                            } else {
                                UIHelper.ToastMessage(this, "请输入6位以上的密码");
                            }
                        }else{
                            UIHelper.ToastMessage(this, "两次输入的密码不同");
                        }
                    }
                } else {
                    UIHelper.ToastMessage(this, "请输入正确的验证码");
                }
            }
        } else {
            UIHelper.ToastMessage(this, "请输入正确的手机号");
        }
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

    private void getCode(final String phone) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                mLoadingDialog.dismiss();
                if (msg.what == 1) {
                    UIHelper.ToastMessage(appContext, msg.obj.toString());

                } else if (msg.what == 2) {
                    UIHelper.ToastMessage(appContext, msg.obj.toString());
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
                    String url = URLs.CODE_GET_Z + phone;
                    result = ApiClient.http_test(appContext, url);
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

    private void getSign() {
        RequestParams params = new RequestParams(URLs.SIGIN);
        params.addQueryStringParameter("phone", phone);
//        params.addQueryStringParameter("account", name);
        params.addQueryStringParameter("pwd", pwd);
        params.addQueryStringParameter("code", yzm);
        params.addQueryStringParameter("unique_phone_code", appContext.getAppId());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String res) {
                mLoadingDialog.dismiss();
                Message msg = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    msg.what = jsonObject.getInt("code");
                    msg.obj = jsonObject.getString("message");
                    if (msg.what == 1) {
                        result = jsonObject.getString("result");
                    }
                    if (!StringUtils.isEmpty(msg.toString())) {
                        if (msg.what == 1) {
                            UIHelper.ToastMessage(appContext, msg.obj.toString());
                            User user = JsonUtils.fromJson(result, User.class);
                            //实例化Editor对象
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
                                } else if (user.getSex().equals("2")) {
                                    editor.putString("sex", "女");
                                }
                            } else {
                                editor.putString("sex", "男");
                            }
                            if (!StringUtils.isEmpty(user.getSecret_code())) {
                                editor.putString("secret_code", user.getSecret_code());
                                URLs.secret_code = user.getSecret_code();
                            }
                            if (!StringUtils.isEmpty(user.getHead_image())){
                                editor.putString("head_image",user.getHead_image());
                            }else{
                                editor.putString("head_image","");
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
                            if (user.getTotal_balance()==null){
                                editor.putLong("money", 0);
                            }else {
                                editor.putLong("money", user.getTotal_balance().longValue());
                            }
                            editor.putInt("jifen", user.getCard_total_point());
                            //提交修改
                            editor.commit();

                            finish();
                        } else if (msg.what == 2) {
                            UIHelper.ToastMessage(appContext, msg.obj.toString());
                            timer.onFinish();
                            codeBtn.setText("获取验证码");
                        }
                    } else {
                        UIHelper.ToastMessage(appContext, "网络错误，请重试");
                        timer.onFinish();
                        codeBtn.setText("获取验证码");
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
