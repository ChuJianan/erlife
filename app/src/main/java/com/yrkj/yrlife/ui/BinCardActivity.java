package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by cjn on 2016/3/24.
 */
@ContentView(R.layout.activity_bincard)
public class BinCardActivity extends BaseActivity {

    String cardNub;
    String phoneNub;
    String codeNub;
    private CountDownTimer timer;
    @ViewInject(R.id.code_btn) private Button codeBtn;
    @ViewInject(R.id.sub_btn) private Button sub_btn;
    @ViewInject(R.id.card_code) private ClearEditText cardCode;
    @ViewInject(R.id.phone_edit) private ClearEditText phoneEdit;
    @ViewInject(R.id.code_edit) private ClearEditText codeEdit;
    @ViewInject(R.id.title) private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("绑定会员卡");
        timer = new TimeCount(60000, 1000, codeBtn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("绑定会员卡");
        MobclickAgent.onResume(this);
    }

    @Event(R.id.code_btn)
    private void codeEvent(View view){
        cardNub=cardCode.getText().toString();
        phoneNub=phoneEdit.getText().toString();
        if (StringUtils.isEmpty(cardNub)||StringUtils.isEmpty(phoneNub)){
            if (StringUtils.isEmpty(cardNub)){
                UIHelper.ToastMessage(this,"请输入会员卡号码");
            }
           else if(StringUtils.isEmpty(phoneNub)){
                UIHelper.ToastMessage(this,"请输入手机号码");
            }
        }else if (StringUtils.isMobileNO(phoneNub)){
            UIHelper.ToastMessage(this,"正在获取验证码，请稍后");
            timer.start();
        }else {
            UIHelper.ToastMessage(this,"请输入正确的手机号码");
        }
    }

    @Event(R.id.sub_btn)
    private void subEvent(View view){
        codeNub=codeEdit.getText().toString();
        if (StringUtils.isEmpty(cardNub)||StringUtils.isEmpty(phoneNub)||StringUtils.isEmpty(codeNub)){
            if (StringUtils.isEmpty(cardNub)){
                UIHelper.ToastMessage(this,"请输入会员卡号码");
            }
            if(StringUtils.isEmpty(phoneNub)){
                UIHelper.ToastMessage(this,"请输入手机号码");
            }
            if (StringUtils.isEmpty(codeNub)){
                UIHelper.ToastMessage(this,"请输入验证码");
            }
        }else {
            UIHelper.ToastMessage(this,"正在提交，请稍后...");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("绑定会员卡");
        MobclickAgent.onPause(this);
    }
}
