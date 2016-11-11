package com.yrkj.yrlife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StatusBarUtil;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.TimeCount;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
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
    private ProgressDialog mLoadingDialog;
    String code;
    private String result;
    SharedPreferences preferences;

    @ViewInject(R.id.code_btn)
    private Button codeBtn;
    @ViewInject(R.id.sub_btn)
    private Button sub_btn;
    @ViewInject(R.id.card_code)
    private ClearEditText cardCode;
    @ViewInject(R.id.phone_edit)
    private ClearEditText phoneEdit;
    @ViewInject(R.id.code_edit)
    private ClearEditText codeEdit;
    @ViewInject(R.id.title)
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("绑定会员卡");
        timer = new TimeCount(60000, 1000, codeBtn);
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        init();
    }

    private void init() {
        String phone = preferences.getString("phone", "");
        phoneEdit.setText(phone);
        StatusBarUtil.StatusBarLightMode(this);
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在加载，请稍候…");
        mLoadingDialog.setCancelable(false);
    }

    @Event(R.id.code_btn)
    private void codeEvent(View view) {
        cardNub = cardCode.getText().toString();
        phoneNub = phoneEdit.getText().toString();
        if (StringUtils.isEmpty(cardNub) || StringUtils.isEmpty(phoneNub)) {
            if (StringUtils.isEmpty(cardNub)) {
                UIHelper.ToastMessage(this, "请输入会员卡号码");
            } else if (StringUtils.isEmpty(phoneNub)) {
                UIHelper.ToastMessage(this, "请输入手机号码");
            }
        } else if (StringUtils.isMobileNO(phoneNub)) {
            timer.start();
            codeBtn.setTextColor(getResources().getColor(R.color.near_listitem_adr));
            codeBtn.setBackground(getResources().getDrawable(R.drawable.ic_yzm_c));
            mLoadingDialog.show();
            getCode(phoneNub);
        } else {
            UIHelper.ToastMessage(this, "请输入正确的手机号码");
        }
    }

    @Event(R.id.sub_btn)
    private void subEvent(View view) {
        codeNub = codeEdit.getText().toString();
        if (StringUtils.isEmpty(cardNub) || StringUtils.isEmpty(phoneNub) || StringUtils.isEmpty(codeNub)) {
            if (StringUtils.isEmpty(cardNub)) {
                UIHelper.ToastMessage(this, "请输入会员卡号码");
            }
            if (StringUtils.isEmpty(phoneNub)) {
                UIHelper.ToastMessage(this, "请输入手机号码");
            }
            if (StringUtils.isEmpty(codeNub)) {
                UIHelper.ToastMessage(this, "请输入验证码");
            }
        } else if (phoneNub.equals(phoneEdit.getText().toString())) {
            if (code.equals(codeNub)) {
                mLoadingDialog.show();
                setCard();
            } else {
                UIHelper.ToastMessage(this, "请输入正确的验证码");
            }
        } else {
            UIHelper.ToastMessage(this, "你的手机号已更换，请重新获取验证码");
        }
    }

    private void setCard() {
        String url = URLs.BIND_CARDS;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("phone", phoneNub);
        params.addQueryStringParameter("code", codeNub);
        params.addQueryStringParameter("card_Number", cardNub);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    mLoadingDialog.dismiss();
                    JSONObject json = new JSONObject(result);
                    int code = json.getInt("code");
                    String message = json.getString("message");
                    UIHelper.ToastMessage(appContext, message);
                    if (code==1){
                        String card_number=json.getString("card_number");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("card_number", card_number);
                        editor.putString("isBind","1");
                        //提交修改
                        editor.commit();
                        finish();
                    }
                    if (code == 2) {
                        timer.onFinish();
                        codeBtn.setTextColor(getResources().getColor(R.color.shop_item_money));
                        codeBtn.setBackground(getResources().getDrawable(R.drawable.ic_yzm));
                        timer.cancel();
                        codeBtn.setText("发送验证码");
                    } else if (code == 3) {
                        Intent intent = new Intent(BinCardActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "cancelled");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
                code = "";
                timer.onFinish();
                timer.cancel();
                codeBtn.setTextColor(getResources().getColor(R.color.shop_item_money));
                codeBtn.setBackground(getResources().getDrawable(R.drawable.ic_yzm));
                codeBtn.setText("获取验证码");
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });
    }

    private void getCode(final String phone) {
        RequestParams params = new RequestParams(URLs.CODE_GET + phone);
//        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("card_Number", cardNub);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = JsonUtils.fromJson(s, Result.class);
                UIHelper.ToastMessage(appContext, result.Message());
                mLoadingDialog.dismiss();
                if (result.OK()) {
                    code = result.Result();
                } else if (result.isOK()) {
                    code = "";
                    timer.onFinish();
                    timer.cancel();
                    codeBtn.setTextColor(getResources().getColor(R.color.shop_item_money));
                    codeBtn.setBackground(getResources().getDrawable(R.drawable.ic_yzm));
                    codeBtn.setText("获取验证码");
                    Intent intent = new Intent(BinCardActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    code = "";
                    codeBtn.setTextColor(getResources().getColor(R.color.shop_item_money));
                    codeBtn.setBackground(getResources().getDrawable(R.drawable.ic_yzm));
                    timer.onFinish();
                    timer.cancel();
                    codeBtn.setText("获取验证码");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoadingDialog.dismiss();
                code = "";
                timer.onFinish();
                timer.cancel();
                codeBtn.setTextColor(getResources().getColor(R.color.shop_item_money));
                codeBtn.setBackground(getResources().getDrawable(R.drawable.ic_yzm));
                codeBtn.setText("获取验证码");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mLoadingDialog.dismiss();
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
