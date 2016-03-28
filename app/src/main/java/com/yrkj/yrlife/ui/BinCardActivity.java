package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.StringUtils;
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
    }

    @Event(R.id.code_btn)
    private void codeEvent(View view){
        cardNub=cardCode.getText().toString();
        phoneNub=phoneEdit.getText().toString();
        if (StringUtils.isEmpty(cardNub)||StringUtils.isEmpty(phoneNub)){
            if (StringUtils.isEmpty(cardNub)){
                UIHelper.ToastMessage(this,"请输入会员卡号码");
            }
            if(StringUtils.isEmpty(phoneNub)){
                UIHelper.ToastMessage(this,"请输入手机号码");
            }
        }else {
            UIHelper.ToastMessage(this,"正在获取验证码，请稍后");
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
}
