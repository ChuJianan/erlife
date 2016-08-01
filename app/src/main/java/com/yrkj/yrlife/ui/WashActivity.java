package com.yrkj.yrlife.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.PayConfirm;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.TestData;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.ClearEditText;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cjn on 2016/6/3.
 */
@ContentView(R.layout.activity_wash)
public class WashActivity extends BaseActivity {

    @ViewInject(R.id.wash_top_l)
    private LinearLayout wash_top_l;
    @ViewInject(R.id.wash_center1_l)
    private LinearLayout wash_center_l;
    @ViewInject(R.id.wash_btm_l)
    private LinearLayout wash_btm_l;
    @ViewInject(R.id.wash_balance_l)
    private LinearLayout wash_balance_l;
    @ViewInject(R.id.wash_warn_l)
    private LinearLayout wash_warn_l;
    @ViewInject(R.id.wash_machid_edit)
    private ClearEditText wash_machid_edit;
    @ViewInject(R.id.wash_adr)
    private TextView wash_adr;
    @ViewInject(R.id.wash_adr_dis)
    private TextView wash_adr_dis;
    @ViewInject(R.id.wash_machid)
    private TextView wash_machid;
    @ViewInject(R.id.wash_isfree)
    private TextView wash_isfree;
    @ViewInject(R.id.wash_name)
    private TextView wash_name;
    @ViewInject(R.id.wash_cardnub)
    private TextView wash_cardnub;
    @ViewInject(R.id.wash_nub)
    private TextView wash_nub;
    @ViewInject(R.id.wash_pay)
    private TextView wash_pay;
    @ViewInject(R.id.wash_pay_dis)
    private TextView wash_pay_dis;
    @ViewInject(R.id.wash_order_no)
    private TextView wash_order_no;
    @ViewInject(R.id.wash_machid_dis)
    private TextView wash_machid_dis;
    @ViewInject(R.id.wash_date)
    private TextView wash_date;
    @ViewInject(R.id.wash_cardnub_dis)
    private TextView wash_cardnub_dis;
    @ViewInject(R.id.warn_text)
    private TextView warn_text;
    @ViewInject(R.id.wash_btn)
    private Button wash_btn;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.textView28)
    private TextView textView28;

    String mach_id;
    private ProgressDialog mLoadingDialog;
    SharedPreferences preferences;
    String name;
    String nick_name;
    int iBtn = 0;
    Washing_no_card_record wash;
    private final Timer timer = new Timer();
    private TimerTask task;
    private boolean isWash = true;
    float spend_money;
    Dialog dialog;
    boolean isd = false;
    boolean isb = true;
    String pay_kind = "1";
    String if_have_useful_coupon;
    float money;
    boolean iswash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("无卡洗车");
        init();
        Intent intent = getIntent();
        mach_id = intent.getStringExtra("result");
        String wash_gson = preferences.getString("wash_gson", "");
        String isWashing = preferences.getString("isWashing", "");
        if (!StringUtils.isEmpty(isWashing)) {
            if (isWashing.equals("1")) {
                isWash = true;
            } else if (isWashing.equals("0")) {
                isWash = false;
            }
        }
        if (StringUtils.isEmpty(mach_id)) {
            wash_btn.setText("确定");
            isWash = true;
            iBtn = 0;
            wash_top_l.setVisibility(View.VISIBLE);
            wash_center_l.setVisibility(View.GONE);
            wash_btm_l.setVisibility(View.GONE);
            wash_balance_l.setVisibility(View.GONE);
            wash_warn_l.setVisibility(View.GONE);
        } else {
            if (iswash) {
                isWash = false;
                wash = UIHelper.washing_no_card_record;
                wash_btn.setText("结算");
                wash_adr.setText(wash.getAddress());
                wash_machid.setText(wash.getMachine_number());
                wash_isfree.setText("正在洗车");
                if (name != "" && !name.equals("")) {
                    wash_name.setText(name);
                } else if (nick_name != "" && nick_name != null) {
                    wash_name.setText(nick_name);
                }
                wash_top_l.setVisibility(View.GONE);
                wash_center_l.setVisibility(View.VISIBLE);
                wash_warn_l.setVisibility(View.GONE);
                wash_btm_l.setVisibility(View.VISIBLE);
                wash_balance_l.setVisibility(View.GONE);
                wash_cardnub.setText(wash.getCard_number());
                wash_nub.setText(wash.getTotal_money() + "");
                wash_pay.setText(spend_money + "");
                iBtn = 2;
                isWash = false;
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        // 要做的事情
                        super.handleMessage(msg);
                        load_Info();
                    }
                };
                task = new TimerTask() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                };
                timer.schedule(task, 5000, 5000);
            } else {
                isWash = false;
                mLoadingDialog.show();
                wash_top_l.setVisibility(View.GONE);
                getWash_record();
            }
        }
    }

    private void init() {
        String digits = "0123456789";
        wash_machid_edit.setKeyListener(DigitsKeyListener.getInstance(digits));
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        name = preferences.getString("name", "");
        nick_name = preferences.getString("nick_name", "");
        money = preferences.getFloat("money", 0);
        if_have_useful_coupon = preferences.getString("if_have_useful_coupon", "");
        mLoadingDialog = UIHelper.progressDialog(this, "正在努力加载......");
        if (money == 0 && !if_have_useful_coupon.equals("0") && !StringUtils.isEmpty(if_have_useful_coupon)) {
            textView28.setText("优惠券金额");
        } else if (money != 0) {
            textView28.setText("卡内余额");
        }
    }

    private void getWash_record() {
        RequestParams params = new RequestParams(URLs.WASH_NO_CARD);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("machineNo", mach_id);
        params.addQueryStringParameter("lat", UIHelper.location.getLatitude() + "");
        params.addQueryStringParameter("lng", UIHelper.location.getLongitude() + "");
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                UIHelper.ToastMessage(appContext, result.Message());
                if (!result.OK()) {
                    isWash = true;
                    wash_btn.setText("返回");
                    warn_text.setText(result.Message());
                    wash_top_l.setVisibility(View.INVISIBLE);
                    wash_center_l.setVisibility(View.GONE);
                    wash_warn_l.setVisibility(View.VISIBLE);
                    wash_btm_l.setVisibility(View.GONE);
                    wash_balance_l.setVisibility(View.GONE);
                    iBtn = -1;
                } else {
                    isWash = false;
                    iBtn = 2;
                    wash = result.washing();
                    wash_btn.setText("结算");
                    wash_adr.setText(wash.getAddress());
                    wash_machid.setText(wash.getMachine_number());
                    wash_isfree.setText("空闲");
                    if (name != "" && !name.equals("")) {
                        wash_name.setText(name);
                    } else if (nick_name != "" && nick_name != null) {
                        wash_name.setText(nick_name);
                    }
                    wash_top_l.setVisibility(View.GONE);
                    wash_center_l.setVisibility(View.VISIBLE);
                    wash_warn_l.setVisibility(View.GONE);
                    wash_btm_l.setVisibility(View.GONE);
                    wash_balance_l.setVisibility(View.GONE);
                    wash_cardnub.setText(wash.getCard_number());
                    wash_nub.setText(wash.getTotal_money() + "");
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putString("Machine_number", wash.getMachine_number());
                    editor.putString("belongCode", wash.getBelong());
                    editor.putBoolean("isWash", true);
                    editor.putString("wash_gson", JsonUtils.toJson(wash));
                    //提交修改
                    editor.commit();
                    wash_top_l.setVisibility(View.GONE);
                    wash_center_l.setVisibility(View.VISIBLE);
                    wash_warn_l.setVisibility(View.GONE);
                    wash_btm_l.setVisibility(View.VISIBLE);
                    wash_balance_l.setVisibility(View.GONE);
                    wash_pay.setText("0");
                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            // TODO Auto-generated method stub
                            // 要做的事情
                            super.handleMessage(msg);
                            load_Info();
                        }
                    };
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    };
                    timer.schedule(task, 5000, 5000);
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
            }

            @Override
            public void onFinished() {
                mLoadingDialog.dismiss();
            }
        });

    }

    @Event(R.id.wash_btn)
    private void washbtnEvent(View view) {
        switch (iBtn) {
            case 0://通过编号去查找洗车机
                isWash = false;
                mach_id = wash_machid_edit.getText().toString();
                if (!StringUtils.isEmpty(mach_id)) {
                    getWash_record();
                } else {
                    UIHelper.ToastMessage(appContext, "请输入洗车机编号");
                }
                break;
            case 2://无卡洗车结算
                isWash = false;
//                mLoadingDialog.show();
                if (timer != null) {
                    timer.cancel();
                }
//                payconfirm();
                dialog("您确定结束洗车吗？", 2);
                break;
            case 3://去评级
                isWash = true;
                Intent intent = new Intent(WashActivity.this, RateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("wash", wash);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case -1://错误，返回
                finish();
                break;
        }
    }

    private void load_Info() {
        RequestParams params = new RequestParams(URLs.Load204Info);
        params.addQueryStringParameter("machineNo", wash.getMachine_number());
        params.addQueryStringParameter("belongCode", wash.getBelong());
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                isWash = false;
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    iBtn = 2;
                    wash_pay.setText(result.spend_money() + "");
                } else {
                    if (result.isLoadOK()) {
                        wash_btn.setText("去评价");
//                        mLoadingDialog.show();
                        if (timer != null) {
                            timer.cancel();
                        }
                        iBtn = 3;
                        PayConfirm payconfirm = result.payconfirm();
                        wash_top_l.setVisibility(View.GONE);
                        wash_center_l.setVisibility(View.GONE);
                        wash_warn_l.setVisibility(View.GONE);
                        wash_btm_l.setVisibility(View.GONE);
                        wash_balance_l.setVisibility(View.VISIBLE);
                        wash_order_no.setText(payconfirm.getBelong());
                        wash_adr_dis.setText(payconfirm.getAddress());
                        wash_pay_dis.setText(payconfirm.getTotalmoney() + "");
                        wash_date.setText(payconfirm.getTime());
                        wash_machid_dis.setText(payconfirm.getMachinenumber());
                        wash_cardnub_dis.setText(payconfirm.getCardnumber());
                        mLoadingDialog.dismiss();
                        float mon = wash.getTotal_money().floatValue() - payconfirm.getTotalmoney().floatValue();
                        //实例化Editor对象
                        SharedPreferences.Editor editor = preferences.edit();
                        //存入数据
                        editor.putFloat("money", mon);
                        editor.putBoolean("isWash", false);
                        //提交修改
                        editor.commit();
                    }
                    if (result.isOK()){
                        Intent intent=new Intent(WashActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
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

            }
        });
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
                isWash = true;
                Result result = JsonUtils.fromJson(string, Result.class);
                UIHelper.ToastMessage(appContext, result.Message());
                if (result.OK()) {
                    wash_btn.setText("去评价");
                    iBtn = 3;
                    PayConfirm payconfirm = result.payconfirm();
                    wash_top_l.setVisibility(View.GONE);
                    wash_center_l.setVisibility(View.GONE);
                    wash_warn_l.setVisibility(View.GONE);
                    wash_btm_l.setVisibility(View.GONE);
                    wash_balance_l.setVisibility(View.VISIBLE);
                    wash_order_no.setText(payconfirm.getBelong());
                    wash_adr_dis.setText(payconfirm.getAddress());
                    wash_pay_dis.setText(payconfirm.getTotalmoney().doubleValue() + "");
                    wash_date.setText(payconfirm.getTime());
                    wash_machid_dis.setText(payconfirm.getMachinenumber());
                    wash_cardnub_dis.setText(payconfirm.getCardnumber());
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

    @Event(R.id.back)
    private void backEvent(View view) {
        if (timer != null) {
            timer.cancel();
        }
        if (isWash) {
            finish();
        } else {
            dialog("您确定结束洗车吗？", 1);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (timer != null) {
                timer.cancel();
            }
            if (isWash) {
                finish();
            } else {
                dialog("您确定结束洗车吗？", 1);

            }
        }
        return flag;
    }

    private void dialog(String context, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(context);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (i == 1) {
                    showDialog();
                }
                if (i == 2) {
                    showDialog();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showDialog() {
        final View view = getLayoutInflater().inflate(R.layout.pay_dialog, null);
        final CheckedTextView balance = (CheckedTextView) view.findViewById(R.id.pay_balance);
        final CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.pay_discount);
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏 (getWindowManager().getDefaultDisplay().getHeight() / 2) - view.getHeight()
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
//        dialog.onWindowAttributesChanged(wl);
        window.setAttributes(wl);
        // 设置点击外围解散
//        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        view.findViewById(R.id.pay_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isb) {
                    balance.setChecked(true);
                    pay_kind = "0";
                    isb = false;
                } else {
                    balance.setChecked(false);
                    pay_kind = "";
                    isb = true;
                }
            }
        });

        view.findViewById(R.id.pay_discount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isd) {
                    checkedTextView.setChecked(true);
                    pay_kind = "1";
                    isd = false;
                } else {
                    checkedTextView.setChecked(false);
                    pay_kind = "";
                    isd = true;
                }
            }
        });

        view.findViewById(R.id.pay_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(pay_kind)) {
                    UIHelper.ToastMessage(appContext, "请选择付款方式");
                } else {
                    dialog.dismiss();
                    payconfirm();
                    mLoadingDialog.show();
                }
            }
        });
        if (StringUtils.isEmpty(if_have_useful_coupon)) {
            view.findViewById(R.id.rl_discount).setVisibility(View.GONE);
        } else {
            if (if_have_useful_coupon.equals("1")) {
                view.findViewById(R.id.rl_discount).setVisibility(View.VISIBLE);
                view.findViewById(R.id.pay_discount).setClickable(true);
            } else if (if_have_useful_coupon.equals("0")) {
                view.findViewById(R.id.rl_discount).setVisibility(View.GONE);
            }
        }
        if (money == 0) {
            view.findViewById(R.id.yue_ll).setVisibility(View.GONE);
            view.findViewById(R.id.pay_balance).setVisibility(View.GONE);
            view.findViewById(R.id.pay_balance).setClickable(false);
            view.findViewById(R.id.pay_discount).setClickable(false);
            checkedTextView.setChecked(true);
            pay_kind = "1";
        }
    }
}
