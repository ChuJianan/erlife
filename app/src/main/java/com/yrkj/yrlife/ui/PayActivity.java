package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.RadioGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/3/24.
 */
@ContentView(R.layout.activity_pay)
public class PayActivity extends  BaseActivity {

    RadioButton radioButton;
    RadioButton moneyRadioButton;
    String payTypeNub;
    String money;
    @ViewInject(R.id.pay_radio) private android.widget.RadioGroup payType;
    @ViewInject(R.id.money_radio) private RadioGroup moneyRadio;
    @ViewInject(R.id.title) private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("会员充值");
        payType.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(payType.getCheckedRadioButtonId());
                if (payType.getCheckedRadioButtonId() == R.id.weixin_radio) {
                    Log.i("type", "微信");
                    payTypeNub="微信";
                } else {
                    Log.i("type","支付宝");
                    payTypeNub="支付宝";
                }
            }
        });

        moneyRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                money=null;
                moneyRadioButton=(RadioButton)findViewById(checkedId);
                money=moneyRadioButton.getText().toString();
                Log.i("money",money);
            }
        });
    }
    @Event(R.id.pay_btn)
    private void payEvent(View view){
        if (StringUtils.isEmpty(payTypeNub)||StringUtils.isEmpty(money)){
            if (StringUtils.isEmpty(payTypeNub)){
            UIHelper.ToastMessage(this,"请选择支付方式");
            }
            if (StringUtils.isEmpty(money)){
                UIHelper.ToastMessage(this,"请选择充值金额");
            }
        }else {
            UIHelper.ToastMessage(this, payTypeNub + "充值" + money);
//            finish();
        }
    }
}
