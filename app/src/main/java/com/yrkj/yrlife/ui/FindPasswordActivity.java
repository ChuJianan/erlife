package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.X509TrustManagerExtensions;
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
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.TimeCount;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 找回密码
 * Created by cjn on 2016/5/24.
 */
@ContentView(R.layout.activity_findpwd)
public class FindPasswordActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("找回密码");
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
                        Intent intent = new Intent(this, FindPasswordTActivity.class);
                        yzm = "";
                        yzmEdit.setText(yzm);
                        timer.onFinish();
                        timer.cancel();
                        codeBtn.setText("获取验证码");
                        intent.putExtra("code",code);
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                    } else {
                        UIHelper.ToastMessage(this, "请输入正确的验证码");
                    }
                }
            }
        } else {
            UIHelper.ToastMessage(appContext, "验证码已经过期，请重新获取");
            isT = true;
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
                    String url = URLs.CODE_GET_F + phone;
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
}
