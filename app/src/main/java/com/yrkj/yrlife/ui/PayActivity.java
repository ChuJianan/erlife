package com.yrkj.yrlife.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.RadioGroup;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;

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
    private static String appUserAgent;
    int pay_kind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("会员充值");
        preferences = this.getSharedPreferences("yrlife", this.MODE_WORLD_READABLE);


        payType.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(payType.getCheckedRadioButtonId());
                if (payType.getCheckedRadioButtonId() == R.id.weixin_radio) {
                    Log.i("type", "微信");
                    payTypeNub = "微信";
                    pay_kind=1;
                } else {
                    Log.i("type", "支付宝");
                    pay_kind=2;
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

    @Override
    protected void onResume() {
        super.onResume();
        String name = preferences.getString("name", "");
        String nick_name=preferences.getString("nick_name","");
        String phone = preferences.getString("phone", "");
        mon = preferences.getLong("money", 0);
        if (name != "" && !name.equals("")) {
            nameText.setText(name);
        }else if(nick_name!=""&&nick_name!=null){
            nameText.setText(nick_name);
        }
        if (phone != "" && !phone.equals("")) {
            phoneText.setText(phone);
        }else {
            phoneText.setText("");
        }
        money = mon + "";
        moneyText.setText(money);
        money = null;
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
            setPay();
        }
    }

    private void setPay() {
        String url = URLs.PAY;
        BigDecimal bigDecimal=new BigDecimal(money);
        RequestParams params = new RequestParams(url);
        params.setHeader("User-Agent",getUserAgent(appContext));
        params.addParameter("secret_code",URLs.secret_code);
        params.addParameter("pay_kind",pay_kind);
        params.addParameter("amount",bigDecimal);
        params.addParameter("unique_phone_code",appContext.getAppId());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Result res= JsonUtils.fromJson(result,Result.class);
                if (res.OK()){
                    UIHelper.ToastMessage(PayActivity.this,res.Message()+payTypeNub + "充值" + money + "元");
                    isFirst = true;
                    money = mon + "";
                    moneyText.setText(money);
                    money = null;
                    finish();
                }else {
                    UIHelper.ToastMessage(PayActivity.this,res.Message());
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(PayActivity.this,ex.getMessage());
//                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(PayActivity.this,"error");
//                finish();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private static String getUserAgent(YrApplication appContext) {
        if (appUserAgent == null || appUserAgent == "") {
            StringBuilder ua = new StringBuilder("iMeeting");
            ua.append('/' + appContext.getPackageInfo().versionName + '_' + appContext.getPackageInfo().versionCode);//App版本
            ua.append("/Android");//手机系统平台
            ua.append("/" + android.os.Build.VERSION.RELEASE);//手机系统版本
            ua.append("/" + android.os.Build.MODEL); //手机型号
            ua.append("/" + appContext.getAppId());//客户端唯一标识
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }

}
