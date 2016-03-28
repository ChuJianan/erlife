package com.yrkj.yrlife.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.prefs.PreferenceChangeEvent;

@ContentView(R.layout.activity_sign)
public class SignActivity extends BaseActivity {

    @ViewInject(R.id.phonenub) private EditText phoneEdit;
    @ViewInject(R.id.yzm) private  EditText yzmEdit;
    @ViewInject(R.id.pwd)private EditText pwdEdit;
    @ViewInject(R.id.dubpwd)private EditText dubpwdEdit;
    @ViewInject(R.id.title) private TextView title;
    String phone;
    String yzm;
    String pwd;
    String dubpwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("注册");
        init();
    }

    private void init() {

    }
    @Event(R.id.hqyzm)
    private void hqyzmEvent(View view){
        phone=phoneEdit.getText().toString();
        if (StringUtils.isEmpty(phone)){
            UIHelper.ToastMessage(this,"请输入手机号");
        }else if (StringUtils.isMobileNO(phone)){
            UIHelper.ToastMessage(this,"正在请求..");
        }else {
            UIHelper.ToastMessage(this,"请输入正确的手机号");
        }
    }
}
