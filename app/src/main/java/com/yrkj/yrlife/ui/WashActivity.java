package com.yrkj.yrlife.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("无卡洗车");
        init();
        Intent intent = getIntent();
        mach_id = intent.getStringExtra("result");
        String wash_gson = preferences.getString("wash_gson", "");
        boolean iswash = preferences.getBoolean("isWash", false);
        if (StringUtils.isEmpty(mach_id)) {
            wash_btn.setText("确定");
            iBtn = 0;
            wash_top_l.setVisibility(View.VISIBLE);
            wash_center_l.setVisibility(View.GONE);
            wash_btm_l.setVisibility(View.GONE);
            wash_balance_l.setVisibility(View.GONE);
            wash_warn_l.setVisibility(View.GONE);
        } else {
            if (iswash) {
                wash=JsonUtils.fromJson(wash_gson,Washing_no_card_record.class);
                spend_money=getIntent().getFloatExtra("spend_money",0);
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
                isWash=false;
                load_Info();
            } else {
                mLoadingDialog.show();
                wash_top_l.setVisibility(View.GONE);
                getWash_record();
            }
        }
    }

    private void init() {
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        name = preferences.getString("name", "");
        nick_name = preferences.getString("nick_name", "");
        mLoadingDialog = UIHelper.progressDialog(this, "正在努力加载......");
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
                isWash = false;
                Result result = JsonUtils.fromJson(string, Result.class);
                UIHelper.ToastMessage(appContext, result.Message());
                if (!result.OK()) {
                    wash_btn.setText("返回");
                    warn_text.setText(result.Message());
                    wash_top_l.setVisibility(View.INVISIBLE);
                    wash_center_l.setVisibility(View.GONE);
                    wash_warn_l.setVisibility(View.VISIBLE);
                    wash_btm_l.setVisibility(View.GONE);
                    wash_balance_l.setVisibility(View.GONE);
                    iBtn = -1;
                } else {
                    iBtn = 1;
                    wash = result.washing();
                    wash_btn.setText("查看消费金额");
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
                mach_id = wash_machid_edit.getText().toString();
                getWash_record();
                isWash = false;
                break;
            case 1://开始洗车，启动请求实时金额
                isWash = false;
                wash_top_l.setVisibility(View.GONE);
                wash_center_l.setVisibility(View.VISIBLE);
                wash_warn_l.setVisibility(View.GONE);
                wash_btm_l.setVisibility(View.VISIBLE);
                wash_balance_l.setVisibility(View.GONE);
                wash_pay.setText("0");
                wash_btn.setText("结算");
                iBtn = 2;
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
                break;
            case 2://无卡洗车结算
                wash_btn.setText("去评价");
                mLoadingDialog.show();
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
                    if (result.isOK()) {
                        wash_btn.setText("去评价");
                        mLoadingDialog.show();
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
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                isWash = true;
                Result result = JsonUtils.fromJson(string, Result.class);
                UIHelper.ToastMessage(appContext, result.Message());
                if (result.OK()) {
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
                    float mon = wash.getTotal_money().floatValue() - payconfirm.getTotalmoney().floatValue();
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putFloat("money", mon);
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
                    payconfirm();
                    Intent intent = new Intent(WashActivity.this, RateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wash", wash);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                if (i == 2) {
                    payconfirm();
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
}
