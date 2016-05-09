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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.security.PrivateKey;

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

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("注册");
//        MobclickAgent.onResume(this);
    }

    private void init() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在加载，请稍候…");
        mLoadingDialog.setCancelable(false);
    }

    @Event(R.id.signbtn)
    private void sigbtnEvent(View view){
        phone = phoneEdit.getText().toString();
        yzm=yzmEdit.getText().toString();
        name=nameEdit.getText().toString();
        pwd=pwdEdit.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            UIHelper.ToastMessage(this, "请输入手机号");
        } else if (StringUtils.isMobileNO(phone)) {
            if (StringUtils.isEmpty(yzm)){
                UIHelper.ToastMessage(this, "请输入验证码");
            }else {
                if (yzm.equals(result)){
                    if (StringUtils.isEmpty(name)){
                        UIHelper.ToastMessage(this, "请输入用户名");
                    }else {
                        if (StringUtils.isEmpty(pwd)){
                            UIHelper.ToastMessage(this, "请输入密码");
                        }else{
                            if (pwd.length()>=6){
                                mLoadingDialog.show();
                                getSigin();
                            }else{
                                UIHelper.ToastMessage(this, "请输入6位以上的验证码");
                            }
                        }
                    }
                }else {
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
                    msg.obj = jsonObject.getString("message").toString();
                    if (msg.what == 1) {
                        result = jsonObject.getString("result");
                    }
                } catch (AppException e) {

                } catch (JSONException e) {

                }
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    private void getSigin(){
        final Handler handler=new Handler(){
            public void  handleMessage(Message msg){
                mLoadingDialog.dismiss();
                if (msg.what==1){
                    UIHelper.ToastMessage(appContext,msg.obj.toString());
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
                }else if (msg.what==2){
                    UIHelper.ToastMessage(appContext,msg.obj.toString());
                }
                finish();
            };
        };
        new Thread(){
            public void run(){
                Message msg=new Message();
              try{
                  String url=URLs.SIGIN+"phone="+phone+"&account="+""+"&pwd"+pwd+"&code="+yzm;
                  result=ApiClient.http_test(appContext,url);
                  JSONObject jsonObject=new JSONObject(result);
                  msg.what=jsonObject.getInt("code");
                  msg.obj=jsonObject.getString("message");
                  if (msg.what==1){
                      result=jsonObject.getString("result");
                  }
              }catch (AppException e){

              }catch (JSONException e){

              }
                handler.sendMessage(msg);
            };
        }.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("注册");
//        MobclickAgent.onPause(this);
    }
}
