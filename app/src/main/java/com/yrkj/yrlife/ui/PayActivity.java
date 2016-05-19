package com.yrkj.yrlife.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

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
public class PayActivity extends BaseActivity {

    RadioButton radioButton;
    RadioButton moneyRadioButton;
    String payTypeNub;
    String money;
    Long mon, mon1;
    boolean isFirst = true;
    @ViewInject(R.id.pay_radio)
    private android.widget.RadioGroup payType;
    @ViewInject(R.id.money_radio)
    private RadioGroup moneyRadio;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.user_name_text)
    private TextView nameText;
    @ViewInject(R.id.phone_text)
    private TextView phoneText;
    @ViewInject(R.id.money_text)
    private TextView moneyText;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("会员充值");
        preferences = this.getSharedPreferences("yrlife", this.MODE_WORLD_READABLE);
        String name = preferences.getString("name", "");
        String phone = preferences.getString("phone", "");
        mon = preferences.getLong("money", 0);
        if (name != "" && !name.equals("")) {
            nameText.setText(name);
        }
        if (phone != "" && !phone.equals("")) {
            phoneText.setText(phone);
        }
        money = mon + "";
        moneyText.setText(money);
        money = null;

        payType.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(payType.getCheckedRadioButtonId());
                if (payType.getCheckedRadioButtonId() == R.id.weixin_radio) {
                    Log.i("type", "微信");
                    payTypeNub = "微信";
                } else {
                    Log.i("type", "支付宝");
                    payTypeNub = "支付宝";
                }
            }
        });

        moneyRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                moneyRadioButton = (RadioButton) findViewById(checkedId);
                if (moneyRadioButton != null) {
                    if (isFirst) {
                        money = moneyRadioButton.getText().toString();
                        if (money.length() <= 3) {
                            money = money.substring(0, 2);
                        }
                        if (money.length() > 3) {
                            money = money.substring(0, 3);
                        }
                        mon1 = Integer.valueOf(money).longValue();
                        mon = mon1 + mon;
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        //存入数据
                        editor.putLong("money", mon);
                        //提交修改
                        editor.commit();
                        Log.i("money", mon + "");
                        isFirst = false;
                    }
                }
            }
        });

    }


    @Event(R.id.pay_btn)
    private void payEvent(View view) {
        if (StringUtils.isEmpty(payTypeNub) || StringUtils.isEmpty(money)) {
            if (StringUtils.isEmpty(payTypeNub)) {
                UIHelper.ToastMessage(this, "请选择支付方式");
            }
            if (StringUtils.isEmpty(money)) {
                UIHelper.ToastMessage(this, "请选择充值金额");
            }
        } else {
            UIHelper.ToastMessage(this, payTypeNub + "充值" + money + "元");
            isFirst = true;
            money = mon + "";
            moneyText.setText(money);
            money = null;
            finish();
        }
    }


}
