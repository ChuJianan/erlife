package com.yrkj.yrlife.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.alipay.PayResult;
import com.yrkj.yrlife.api.ApiClient;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.AliPay;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Upmp;
import com.yrkj.yrlife.been.WeixinPay;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.Md5;
import com.yrkj.yrlife.utils.StartPluginAssist;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.RadioGroup;
import com.yrkj.yrlife.wxapi.WXEntryActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * Created by cjn on 2016/3/24.
 */
@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseActivity {

    RadioButton radioButton;
    RadioButton moneyRadioButton;
    String payTypeNub = "微信";
    String money;
    float mon, mon1;
    boolean isFirst = true;
    Upmp ipsPay;
    //    @ViewInject(R.id.pay_radio)
//    private android.widget.RadioGroup payType;
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
    private ProgressDialog mLoadingDialog;
    private static String appUserAgent;
    int pay_kind = 1;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    Dialog dialog;
    private static final int SDK_PAY_FLAG = 1;
    String orderInfo;
    String sign;
    String orderNumber;
    String orderEncodeType = "5";// 签名方式:5#采用MD5签名认证方式 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("会员充值");
        init();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, UIHelper.APP_ID, false);
        preferences = this.getSharedPreferences("yrlife", this.MODE_WORLD_READABLE);


//        payType.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(android.widget.RadioGroup group, int checkedId) {
//                radioButton = (RadioButton) findViewById(payType.getCheckedRadioButtonId());
//                if (payType.getCheckedRadioButtonId() == R.id.weixin_radio) {
//                    Log.i("type", "微信");
//                    payTypeNub = "微信";
//                    pay_kind = 1;
//                } else {
//                    Log.i("type", "支付宝");
//                    pay_kind = 2;
//                    payTypeNub = "支付宝";
//                }
//            }
//        });

        moneyRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                moneyRadioButton = (RadioButton) findViewById(checkedId);
                if (moneyRadioButton != null) {
                    if (isFirst) {
                        money = moneyRadioButton.getText().toString();

                        money = money.substring(0, money.length() - 1);

                        Log.i("money", mon + "");
                        isFirst = true;
                    }
                }
            }
        });

    }

    private void init() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mLoadingDialog.setTitle("提示");
        mLoadingDialog.setMessage("正在支付，请稍后……");
        mLoadingDialog.setCancelable(false);
    }

    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.pay_choose_dialog, null);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        view.findViewById(R.id.wx_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.show();
                setPay();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.zfb_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.show();
                setAliPay();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.cancel_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        String name = preferences.getString("name", "");
        String nick_name = preferences.getString("nick_name", "");
        String phone = preferences.getString("phone", "");
        mon = preferences.getFloat("money", 0);
        if (name != "" && !name.equals("")) {
            nameText.setText(name);
        } else if (nick_name != "" && nick_name != null) {
            nameText.setText(nick_name);
        }
        if (phone != "" && !phone.equals("")) {
            phoneText.setText(phone);
        } else {
            phoneText.setText("");
        }
        money = mon + "";
        moneyText.setText(money);
        money = null;
    }

    @Event(R.id.pay_btn)
    private void payEvent(View view) {
        if (StringUtils.isEmpty(money)) {
            UIHelper.ToastMessage(this, "请选择充值金额");
        } else {
            showDialog();
        }
    }

    private void setPay() {
        String url = URLs.PAY;
//        BigDecimal bigDecimal = new BigDecimal("0.01");
        BigDecimal bigDecimal = new BigDecimal(money);
        UIHelper.bigDecimal = bigDecimal;
        RequestParams params = new RequestParams(url);
        params.setHeader("User-Agent", getUserAgent(appContext));
        params.addParameter("secret_code", URLs.secret_code);
//        params.addParameter("pay_kind",pay_kind);
        params.addParameter("amount", bigDecimal);
        params.addParameter("unique_phone_code", appContext.getAppId());
//        params.addParameter("ip",appContext.getLocalHostIp());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Result res = JsonUtils.fromJson(result, Result.class);
                if (res.OK()) {
                    WeixinPay weixinPay = res.getWxpay();
                    UIHelper.orderNumber = weixinPay.getOut_trade_no();
                    PayReq request = new PayReq();
                    request.appId = UIHelper.APP_ID;
                    request.partnerId = UIHelper.PARTNERID;
                    request.openId = UIHelper.OpenId;
                    request.prepayId = weixinPay.getPrePayId();
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = weixinPay.getNonceStr();
                    request.timeStamp = weixinPay.getTimeStamp();
                    String md5 = Md5.encode("appid=" + UIHelper.APP_ID + "&noncestr=" + weixinPay.getNonceStr() + "&package=Sign=WXPay" + "&partnerid=" +
                            UIHelper.PARTNERID + "&prepayid=" + weixinPay.getPrePayId() + "&timestamp=" + weixinPay.getTimeStamp() + "&key=qingdaoyirenkeji8888888888888888");
                    request.sign = md5;
                    if (!StringUtils.isEmpty(request.prepayId)) {
                        appContext.api.sendReq(request);
                    } else {
                        UIHelper.ToastMessage(appContext, "出现一点小问题，请稍后再试");
                        finish();
                    }

                } else if (res.isOK()) {
                    UIHelper.ToastMessage(PayActivity.this, res.Message());
                    UIHelper.openLogin(PayActivity.this);
                } else {
                    UIHelper.ToastMessage(PayActivity.this, res.Message());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(PayActivity.this, ex.getMessage());
//                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(PayActivity.this, "error");
//                finish();
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
//                finish();
            }
        });
    }

    private void setAliPay() {
        BigDecimal bigDecimal = new BigDecimal(money);
        UIHelper.bigDecimal = bigDecimal;
        RequestParams params = new RequestParams(URLs.ALI_PAY);
        params.setHeader("User-Agent", getUserAgent(appContext));
        params.addParameter("secret_code", URLs.secret_code);
        params.addParameter("amount", bigDecimal);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                UIHelper.ToastMessage(appContext, result.Message());
                if (result.OK()) {
                    AliPay aliPay = result.aliPay();
                    orderInfo = aliPay.getCreateLinkString();
                    sign = aliPay.getSign();
                    orderNumber = aliPay.getOut_trade_no();
                    pay();
                } else if (result.isOK()) {
                    UIHelper.ToastMessage(PayActivity.this, result.Message());
                    UIHelper.openLogin(PayActivity.this);
                } else {
                    UIHelper.ToastMessage(PayActivity.this, result.Message());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "error");
                finish();
            }

            @Override
            public void onFinished() {

//                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        UIHelper.ToastMessage(appContext, "支付成功");
                        isAliPay();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            UIHelper.ToastMessage(appContext, "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            UIHelper.ToastMessage(appContext, "支付失败");
                            mLoadingDialog.dismiss();
                            finish();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void isAliPay() {
        RequestParams params = new RequestParams(URLs.QUERY_PAY);
        params.addQueryStringParameter("out_trade_no", orderNumber);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String res) {
                Result result = JsonUtils.fromJson(res, Result.class);
                UIHelper.ToastMessage(appContext, result.Message());
                if (result.OK()) {
                    mon1 = UIHelper.bigDecimal.floatValue();
                    mon = mon1 + mon;
                    BigDecimal b = new BigDecimal(mon);
                    float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putFloat("money", f1);
                    //提交修改
                    editor.commit();
                    Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
                    startActivity(intent);
                } else if (result.isOK()) {
                    UIHelper.openLogin(PayActivity.this);
                } else {

                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "cancel");
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
                finish();
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
                finish();
            }
        });
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {


        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */

        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=" + sign + "&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private void ipsPay() {
        BigDecimal bigDecimal = new BigDecimal(money);
        UIHelper.bigDecimal = bigDecimal;
        RequestParams params = new RequestParams(URLs.IPSPAY);
        params.addParameter("secret_code", URLs.secret_code);
        params.addParameter("amount", bigDecimal);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    ipsPay = result.ipsPay();
                    ipsPay.setTranAmt(money);
                    Bundle mobilePayInfo = new Bundle();
                    mobilePayInfo.putString("merCode", ipsPay.getMerCode());
                    mobilePayInfo.putString("merRequestInfo", ipsPay.getMerRequestInfo());
                    mobilePayInfo.putString("sign", ipsPay.getSign());
                    mobilePayInfo.putString("orderEncodeType", orderEncodeType);
                    mobilePayInfo.putString("bankCard", ipsPay.getBankCard());

                    StartPluginAssist.start_ips_plugin(PayActivity.this, mobilePayInfo);
                } else if (result.isOK()) {
                    UIHelper.ToastMessage(appContext, result.Message());
                    UIHelper.openLogin(PayActivity.this);
                    finish();
                } else {
                    UIHelper.ToastMessage(appContext, result.Message());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                UIHelper.ToastMessage(appContext, "cancel");
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });
    }

    // 重用该实例
    protected void onNewIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        mLoadingDialog.show();
        isAliPay();
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=RSA";
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
