package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.AppManager;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.TimeCount;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.ClearEditText;

import org.apache.commons.httpclient.CircularRedirectException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

//@ContentView(value = R.layout.activity_sign)
public class SignActivity extends BaseActivity {

    @ViewInject(value = R.id.phonenub)
    private EditText phoneEdit;
    @ViewInject(value = R.id.yzm)
    private EditText yzmEdit;
    @ViewInject(value = R.id.edit_pwd)
    private EditText nameEdit;
    @ViewInject(value = R.id.edit_pwds)
    private EditText pwdEdit;
    @ViewInject(value = R.id.title)
    private TextView title;
    @ViewInject(value = R.id.hq_code)
    private Button codeBtn;
    @ViewInject(value = R.id.hq_codes)
    private Button codeBtns;
    @ViewInject(value = R.id.sign_top)
    private LinearLayout sign_top;
    @ViewInject(value = R.id.sign_center)
    private LinearLayout sign_center;
    @ViewInject(value = R.id.sign_bottom)
    private LinearLayout sign_bottom;
    @ViewInject(value = R.id.input_code)
    private TextView input_code;
    @ViewInject(value = R.id.input_phone)
    private TextView input_phone;
    @ViewInject(value = R.id.input_pwd)
    private TextView input_pwd;
    @ViewInject(value = R.id.sub_code)
    private Button sub_code;
    @ViewInject(value = R.id.signbtn)
    private Button sign_btn;
    @ViewInject(value = R.id.xieding_check)
    private CheckBox checkBox;
    private String phone;
    private String yzm;
    private String name;
    private String pwd;
    private String code;
    private CountDownTimer timer;
    private ProgressDialog mLoadingDialog;
    private String result;
    private SharedPreferences preferences;
    private boolean isCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        x.view().inject(this);
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        if (title != null) {
            title.setText("注册");
        }
        timer = new TimeCount(60000, 1000, codeBtns);
        init();
    }

    private void init() {
        String digits = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        nameEdit.setKeyListener(DigitsKeyListener.getInstance(digits));
        pwdEdit.setKeyListener(DigitsKeyListener.getInstance(digits));
        if (input_phone!=null){
        input_phone.setTextColor(getResources().getColor(R.color.paycolor));
        }
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在加载，请稍候…");
        mLoadingDialog.setCancelable(false);
        if (codeBtn!=null){
        codeBtn.setClickable(false);
        codeBtn.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
        }
        if (sub_code!=null){
        sub_code.setClickable(false);
        sub_code.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
        }
        if (sign_btn!=null){
        sign_btn.setClickable(false);
        sign_btn.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
        }
        if (phoneEdit!=null){
        phoneEdit.addTextChangedListener(codeBtnWatcher);
        }
        if (yzmEdit!=null){
        yzmEdit.addTextChangedListener(sub_codeWatcher);
        }
        if (pwdEdit!=null){
        pwdEdit.addTextChangedListener(sign_btnWatcher);
        }
        if (checkBox!=null){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCheck = true;
                    int phone = phoneEdit.getText().toString().length();
                    if (phone == 11) {
                        codeBtn.setBackgroundResource(R.drawable.bg_btn_code);
                    } else {
                        codeBtn.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
                    }
                } else {
                    codeBtn.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
                    isCheck = false;
                }
            }
        });
        }
    }

    TextWatcher sign_btnWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = pwdEdit.getSelectionStart();
            editEnd = pwdEdit.getSelectionEnd();
            if (temp.length() >= 6) {
                sign_btn.setBackgroundResource(R.drawable.bg_btn_code);
                sign_btn.setClickable(true);
            } else {

                sign_btn.setClickable(false);
            }
        }
    };
    TextWatcher sub_codeWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = yzmEdit.getSelectionStart();
            editEnd = yzmEdit.getSelectionEnd();
            if (temp.length() > 6) {
                sub_code.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
                sub_code.setClickable(false);
            } else if (temp.length() < 6) {
                sub_code.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
                sub_code.setClickable(false);
            } else if (temp.length() == 6) {
                sub_code.setBackgroundResource(R.drawable.bg_btn_code);
                sub_code.setClickable(true);
            }
        }
    };
    TextWatcher codeBtnWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
//            if (temp.length() > 11) {
//                codeBtn.setClickable(false);
//            } else if (temp.length() < 11) {
//                codeBtn.setClickable(false);
//            } else if (temp.length() == 11&&isCheck) {
//                codeBtn.setBackgroundResource(R.drawable.bg_btn_code);
//                codeBtn.setClickable(true);
//            }
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
//            if (temp.length() > 11) {
//                codeBtn.setClickable(false);
//            } else if (temp.length() < 11) {
//                codeBtn.setClickable(false);
//            } else if (temp.length() == 11&&isCheck) {
//                codeBtn.setBackgroundResource(R.drawable.bg_btn_code);
//                codeBtn.setClickable(true);
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = phoneEdit.getSelectionStart();
            editEnd = phoneEdit.getSelectionEnd();
            if (temp.length() > 11) {
                codeBtn.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
                codeBtn.setClickable(false);
            } else if (temp.length() < 11) {
                codeBtn.setBackgroundColor(getResources().getColor(R.color.dim_foreground_light_inverse_disabled));
                codeBtn.setClickable(false);
            } else if (temp.length() == 11 && isCheck) {
                codeBtn.setBackgroundResource(R.drawable.bg_btn_code);
                codeBtn.setClickable(true);
            }
        }
    };

    @Event(R.id.xieding)
    private void xiedingEvent(View view) {
        Intent intent = new Intent(this, XiedingActivity.class);
        startActivity(intent);
    }

    @Event(R.id.signbtn)
    private void sigbtnEvent(View view) {
        phone = phoneEdit.getText().toString();
        yzm = yzmEdit.getText().toString();
        name = nameEdit.getText().toString();
        pwd = pwdEdit.getText().toString();

        if (StringUtils.isEmpty(name)) {
            UIHelper.ToastMessage(this, "请输入密码");
        } else {
            if (StringUtils.isEmpty(pwd)) {
                UIHelper.ToastMessage(this, "请再次输入密码");
            } else if (name.equals(pwd)) {
                if (pwd.length() >= 6) {
                    mLoadingDialog.show();
                    getSign();
                } else {
                    UIHelper.ToastMessage(this, "请输入6位以上的密码");
                }
            } else {
                UIHelper.ToastMessage(this, "两次输入的密码不同");
            }
        }
    }

    @Event(R.id.sub_code)
    private void subcodeEvent(View view) {
        yzm = yzmEdit.getText().toString();
        mLoadingDialog.show();
        if (!StringUtils.isEmpty(yzm)) {
            if (code.equals(yzm)) {
                RequestParams params = new RequestParams(URLs.Code_Check);
                params.addQueryStringParameter("phone", phone);
                params.addQueryStringParameter("code", code);
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String string) {
                        Result result = JsonUtils.fromJson(string, Result.class);
                        UIHelper.ToastMessage(appContext, result.Message());
                        if (result.OK()) {
                            input_pwd.setTextColor(getResources().getColor(R.color.paycolor));
                            input_phone.setTextColor(getResources().getColor(R.color.sign_top));
                            input_code.setTextColor(getResources().getColor(R.color.sign_top));
                            sign_bottom.setVisibility(View.VISIBLE);
                            sign_center.setVisibility(View.GONE);
                            sign_top.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        UIHelper.ToastMessage(appContext, ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        UIHelper.ToastMessage(appContext, "cancel");
                    }

                    @Override
                    public void onFinished() {
                        mLoadingDialog.dismiss();
                    }
                });

            } else {
                UIHelper.ToastMessage(appContext, "验证码输入错误，请重新输入");
            }
        } else {
            UIHelper.ToastMessage(appContext, "请输入验证码");
        }
    }

    @Event(R.id.hq_codes)
    private void hqcodesEvent(View view) {
        timer.start();
        mLoadingDialog.show();
        getCode(phone);
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
                    input_phone.setTextColor(getResources().getColor(R.color.sign_top));
                    input_code.setTextColor(getResources().getColor(R.color.paycolor));
                    sign_top.setVisibility(View.GONE);
                    sign_center.setVisibility(View.VISIBLE);
                    sign_bottom.setVisibility(View.GONE);
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
        params.addQueryStringParameter("pwd", pwd);
        params.addQueryStringParameter("unique_phone_code", appContext.getAppId());
        params.addQueryStringParameter("tokenAndFlag", UIHelper.token + "And");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String res) {
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
                            if (!StringUtils.isEmpty(user.getIf_have_useful_coupon())){
                                editor.putString("if_have_useful_coupon",user.getIf_have_useful_coupon());
                            }else {
                                editor.putString("if_have_useful_coupon","0");
                            }
                            if (!StringUtils.isEmpty(user.getIsWashing())){
                                editor.putString("isWashing",user.getIsWashing());
                            }else {
                                editor.putString("isWashing","0");
                            }
                            editor.putInt("jifen", user.getCard_total_point());
                            //提交修改
                            editor.commit();
                            AppManager.getAppManager().finishActivity(LoginActivity.class);
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
                mLoadingDialog.dismiss();
            }
        });
    }
}
