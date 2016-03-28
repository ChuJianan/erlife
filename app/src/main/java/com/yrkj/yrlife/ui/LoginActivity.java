package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A login screen that offers login via email/password.
 */
@ContentView( value = R.layout.activity_login )
public class LoginActivity extends BaseActivity{

    @ViewInject(value = R.id.name) private EditText nameText;
    @ViewInject(value = R.id.password) private EditText pwdText;
    @ViewInject(value = R.id.sign) private TextView sign;
    @ViewInject(value = R.id.login) private Button loginbtn;
    @ViewInject(R.id.wjpwd) private  TextView wjpwd;
    @ViewInject(value = R.id.title) private TextView title;
    public  String name;
    public  String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the login form.
        x.view().inject(this);
        init();
    }

    private void init() {
        title.setText("登录");
        sign.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        sign.getPaint().setAntiAlias(true);//抗锯齿
        wjpwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        wjpwd.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Event(R.id.login)
    private void loginEvent(View view) {
                name=nameText.getText().toString();
                password=pwdText.getText().toString();
                if(StringUtils.isEmpty(name)||StringUtils.isEmpty(password)){
                if (StringUtils.isEmpty(name)){
                    UIHelper.CenterToastMessage(this, "请填写用户名");
                }else
                if (StringUtils.isEmpty(password)){
                    UIHelper.CenterToastMessage(this, "请填写密码");
                }
                }else {
                    UIHelper.CenterToastMessage(this, "正在登录...");
                }
    }

    @Event(R.id.sign)
    private void signEvent(View view){
        Intent intent=new Intent(this,SignActivity.class);
        startActivity(intent);
    }
}

