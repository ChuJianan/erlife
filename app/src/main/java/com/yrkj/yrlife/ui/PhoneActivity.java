package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.TimeCount;
import com.yrkj.yrlife.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/3/29.
 */
@ContentView(R.layout.activity_phone)
public class PhoneActivity extends BaseActivity {


    private String phone;
    private String code;
    private CountDownTimer timer;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.phone_edit)
    private EditText phoneEdit;
    @ViewInject(R.id.code_edit)
    private EditText codeEdit;
    @ViewInject(R.id.code_btn)
    private Button codeBtn;
    private ProgressDialog mLoadingDialog;
    private String result;
    SharedPreferences preferences;
    private String ycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        title.setText("更换手机号");
        timer = new TimeCount(60000, 1000, codeBtn);
        init();
    }

    private void init() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在请求，请稍候……");
        mLoadingDialog.setCancelable(false);
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart("更换手机号");
//        MobclickAgent.onResume(this);
//    }

    @Event(R.id.code_btn)
    private void codeEvent(View view) {
        phone = phoneEdit.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            UIHelper.ToastMessage(this, "请输入手机号");
        } else if (StringUtils.isMobileNO(phone)) {
            mLoadingDialog.show();
            getCode(phone);
            timer.start();
        } else {
            UIHelper.ToastMessage(this, "请输入正确的手机号");
        }
    }

    @Event(R.id.sign_btn)
    private void signEvent(View view) {
        phone = phoneEdit.getText().toString();
        code = codeEdit.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            UIHelper.ToastMessage(this, "请输入手机号");

        } else if (StringUtils.isEmpty(code)) {
            UIHelper.ToastMessage(this, "请输入验证码");
        } else {
            if (code.equals(ycode)) {
                mLoadingDialog.show();
                String url = URLs.USER_INFO + "secret_code=" + URLs.secret_code + "&phone=" + phone + "&code=" + code;
                setUserInfo(url);
            } else {
                UIHelper.ToastMessage(this, "请输入正确的验证码");
            }

//            UIHelper.ToastMessage(this, "更改成功");
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
                }
            }

            ;
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    String url = URLs.CODE_GET + phone;
                    result = ApiClient.http_test(appContext, url);
                    JSONObject jsonObject = new JSONObject(result);
                    msg.what = jsonObject.getInt("code");
                    msg.obj = jsonObject.getString("message");
                    if (msg.what == 1) {
                        ycode = jsonObject.getString("result");
                    }
                } catch (AppException e) {

                } catch (JSONException e) {

                }
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    private void setUserInfo(final String url) {

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.obj != null) {
                    mLoadingDialog.dismiss();
                    if (msg.what == 1) {
                        UIHelper.ToastMessage(PhoneActivity.this, msg.obj.toString());
                        SharedPreferences preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        //存入数据
                        editor.putString("phone", phone);
                        //提交修改
                        editor.commit();
                        finish();
                    } else if (msg.what == 2) {
                        UIHelper.ToastMessage(PhoneActivity.this, msg.obj.toString());
                    }
                } else {
                    UIHelper.ToastMessage(PhoneActivity.this, "网络出错，请稍候...");
                }

            }

            ;
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    result = ApiClient.http_test(appContext, url);
                    JSONObject jsonObject = new JSONObject(result);
                    msg.what = jsonObject.getInt("code");
                    msg.obj = jsonObject.getString("message");
                } catch (AppException e) {

                } catch (JSONException e) {

                }
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }


}
