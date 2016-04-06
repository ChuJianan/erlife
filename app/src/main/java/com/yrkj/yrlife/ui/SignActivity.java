package com.yrkj.yrlife.ui;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.TimeCount;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.prefs.PreferenceChangeEvent;

@ContentView(R.layout.activity_sign)
public class SignActivity extends BaseActivity {

    @ViewInject(R.id.phonenub) private ClearEditText phoneEdit;
    @ViewInject(R.id.yzm) private  ClearEditText yzmEdit;
    @ViewInject(R.id.username)private ClearEditText nameEdit;
    @ViewInject(R.id.pwd)private ClearEditText pwdEdit;
    @ViewInject(R.id.title) private TextView title;
    @ViewInject(R.id.hq_code)private Button codeBtn;
    String phone;
    String yzm;
    String pwd;
    String dubpwd;
    private CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("注册");
        timer = new TimeCount(60000, 1000, codeBtn);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("注册");
        MobclickAgent.onResume(this);
    }

    private void init() {

    }
    @Event(R.id.hq_code)
    private void hqyzmEvent(View view){
        phone=phoneEdit.getText().toString();
        if (StringUtils.isEmpty(phone)){
            UIHelper.ToastMessage(this,"请输入手机号");
        }else if (StringUtils.isMobileNO(phone)){
            timer.start();
            UIHelper.ToastMessage(this,"正在请求..");
        }else {
            UIHelper.ToastMessage(this,"请输入正确的手机号");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("注册");
        MobclickAgent.onPause(this);
    }
}
