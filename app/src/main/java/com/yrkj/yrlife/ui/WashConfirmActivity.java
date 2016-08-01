package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.PayConfirm;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 洗车支付
 * Created by cjn on 2016/8/1.
 */
@ContentView(R.layout.activity_wash_confirm)
public class WashConfirmActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.confirm_wash)
    TextView confirm_wash;
    @ViewInject(R.id.balance_wash)
    TextView balance_wash;
    @ViewInject(R.id.rl_discount)
    RelativeLayout rl_discount;
    @ViewInject(R.id.pay_balance)
    CheckedTextView pay_balance;
    @ViewInject(R.id.pay_coupon)
    CheckedTextView pay_coupon;


    Washing_no_card_record wash;
    PayConfirm payConfirm;
    String if_have_useful_coupon;
    SharedPreferences preferences;
    String pay_kind = "1";
    ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("订单支付");
        payConfirm = (PayConfirm) getIntent().getSerializableExtra("payconfirm");
        wash = (Washing_no_card_record) getIntent().getSerializableExtra("wash");

    }

    private void init() {
        mLoadingDialog = UIHelper.progressDialog(WashConfirmActivity.this, "结算中，请稍后...");
        confirm_wash.setText(payConfirm.getTotalmoney().toString());
        balance_wash.setText(wash.getTotal_money().toString());
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        if_have_useful_coupon = preferences.getString("if_have_useful_coupon", "");
        if (StringUtils.isEmpty(if_have_useful_coupon)) {
            rl_discount.setVisibility(View.GONE);
        } else {
            if (if_have_useful_coupon.equals("1")) {
                rl_discount.setVisibility(View.VISIBLE);
                pay_coupon.setChecked(true);
            } else if (if_have_useful_coupon.equals("0")) {
                rl_discount.setVisibility(View.GONE);
            }
        }
        pay_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pay_balance.isChecked()) {
                    pay_kind = "0";
                } else {
                    pay_kind = "";
                }
            }
        });
        pay_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pay_coupon.isChecked()) {
                    pay_kind = "1";
                } else {
                    pay_kind = "";
                }
            }
        });
    }
    @Event(R.id.wash_pay)
    private void washpayEvent(View view){
        if (StringUtils.isEmpty(pay_kind)) {
            UIHelper.ToastMessage(appContext, "请选择付款方式");
        }else {
            mLoadingDialog.show();
            payconfirm();
        }
    }
    private void payconfirm() {
        RequestParams params = new RequestParams(URLs.PAYCONFIRM);
        params.setConnectTimeout(1000 * 30);
        params.addQueryStringParameter("machineNo", wash.getMachine_number());
        params.addQueryStringParameter("belongCode", wash.getBelong());
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("pay_kind", pay_kind);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                UIHelper.ToastMessage(appContext, result.Message());
                if (result.OK()) {
                    PayConfirm payconfirm = result.payconfirm();
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    if (pay_kind.equals("0")) {
                        float mon = wash.getTotal_money().floatValue() - payconfirm.getTotalmoney().floatValue();
                        //存入数据
                        editor.putFloat("money", mon);
                    }
                    editor.putBoolean("isWash", false);
                    //提交修改
                    editor.commit();
                    Intent intent=new Intent(WashConfirmActivity.this,WashBillActivity.class);
                    intent.putExtra("payconfirm",payconfirm);
                    intent.putExtra("wash",wash);
                    startActivity(intent);
                    finish();
                } else if (result.isOK()){
                    Intent intent=new Intent(WashConfirmActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "error");
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });

    }
}
