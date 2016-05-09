package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A login screen that offers login via email/password.
 */
@ContentView(value = R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(value = R.id.name)
    private EditText nameText;
    @ViewInject(value = R.id.password)
    private EditText pwdText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the login form.
        x.view().inject(this);
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

    @Event(R.id.sign)
    private void signEvent(View view) {
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
    }

    private void login() {

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                mLoadingDialog.dismiss();
                if (msg.what == 1) {
                    UIHelper.ToastMessage(appContext, msg.obj.toString());
                    User user = JsonUtils.fromJson(result, User.class);
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putString("name", user.getReal_name());
                    editor.putString("phone", user.getPhone());
                    if (user.getSex().equals("1")) {
                        editor.putString("sex", "男");
                    } else if (user.getSex().equals("2")) {
                        editor.putString("sex", "女");
                    }
                    //提交修改
                    editor.commit();
                } else if (msg.what == 2) {
                    UIHelper.ToastMessage(appContext, msg.obj.toString());
                }
                finish();
            };
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    String url = URLs.LOGIN + "conditions=" + name + "&pwd=" + password;
                    result = ApiClient.http_test(appContext, url);
                    JSONObject json = new JSONObject(result);
                    msg.what = json.getInt("code");
                    msg.obj = json.getString("message");
                    result = json.getString("result");
                } catch (AppException e) {

                } catch (JSONException e) {

                }
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
}

