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
import com.yrkj.yrlife.app.AppManager;
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

import java.math.BigDecimal;

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
    //    PayConfirm payConfirm;
    String if_have_useful_coupon;
    SharedPreferences preferences;
    String pay_kind = "";
    ProgressDialog mLoadingDialog;
    private BigDecimal spend_money;
    boolean isd = false;
    boolean isb = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("订单支付");
        spend_money = (BigDecimal) getIntent().getExtras().get("spend_money");
        wash = (Washing_no_card_record) getIntent().getSerializableExtra("wash");
        init();
    }

    private void init() {
        mLoadingDialog = UIHelper.progressDialog(WashConfirmActivity.this, "结算中，请稍后...");
        if (spend_money == null) {
            confirm_wash.setText("0.00");
        } else {
            confirm_wash.setText(spend_money.toString());
        }
        balance_wash.setText(wash.getTotal_money().toString());
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        if_have_useful_coupon = preferences.getString("if_have_useful_coupon", "");

        pay_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isb) {
                    pay_balance.setChecked(true);
                    pay_kind = "0";
                    isb = false;
                } else {
                    pay_balance.setChecked(false);
                    pay_kind = "";
                    isb = true;
                }
            }
        });
        pay_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isd) {
                    pay_coupon.setChecked(true);
                    pay_kind = "1";
                    isd = false;
                } else {
                    pay_coupon.setChecked(false);
                    pay_kind = "";
                    isd = true;
                }
            }
        });
        if (StringUtils.isEmpty(if_have_useful_coupon)) {
            rl_discount.setVisibility(View.GONE);
        } else {
            if (if_have_useful_coupon.equals("1")) {
                rl_discount.setVisibility(View.VISIBLE);
                pay_coupon.setChecked(true);
            } else if (if_have_useful_coupon.equals("0")) {
                pay_balance.setChecked(true);
                pay_kind="0";
                rl_discount.setVisibility(View.GONE);
            }
        }
    }

    @Event(R.id.wash_pay)
    private void washpayEvent(View view) {
        if (StringUtils.isEmpty(pay_kind)) {
            UIHelper.ToastMessage(appContext, "请选择付款方式");
        } else {
            mLoadingDialog.show();
            payconfirm();
        }
    }

    private void payconfirm() {
        RequestParams params = new RequestParams(URLs.PAYCONFIRM);
        params.setConnectTimeout(1000 * 35);
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
                    editor.putString("isWashing","0");
                    editor.putBoolean("isWash", false);
                    //提交修改
                    editor.commit();
                    Intent intent = new Intent(WashConfirmActivity.this, WashBillActivity.class);
                    intent.putExtra("payconfirm", payconfirm);
                    intent.putExtra("wash", wash);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(WashAActivity.class);
                    finish();
                } else if (result.isOK()) {
                    Intent intent = new Intent(WashConfirmActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(WashAActivity.class);
                    finish();
                } else {

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
