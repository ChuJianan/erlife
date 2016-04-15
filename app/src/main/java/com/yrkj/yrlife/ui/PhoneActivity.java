package com.yrkj.yrlife.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.TimeCount;
import com.yrkj.yrlife.utils.UIHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("更换手机号");
        timer = new TimeCount(60000, 1000, codeBtn);
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
            UIHelper.ToastMessage(this, "正在请求..");
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
            SharedPreferences preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
            //实例化Editor对象
            SharedPreferences.Editor editor = preferences.edit();
            //存入数据
            editor.putString("phone", phone);
            //提交修改
            editor.commit();
            finish();
            UIHelper.ToastMessage(this, "更改成功");
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("更换手机号");
//        MobclickAgent.onPause(this);
//    }
}
