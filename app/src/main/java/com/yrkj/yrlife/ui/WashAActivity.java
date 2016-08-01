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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.PayConfirm;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 无卡洗车正在洗车
 * Created by cjn on 2016/7/30.
 */
@ContentView(R.layout.activity_wash_a)
public class WashAActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.flipper)
    ViewFlipper flipper;


    private int[] ids = {R.mipmap.ic_wash_top_g, R.mipmap.ic_wash_top_p, R.mipmap.ic_wash_top_x, R.mipmap.ic_wash_top_xs};
    private int start = 0;
    Washing_no_card_record wash;
    ProgressDialog mLoadingDialog;
    SharedPreferences preferences;
    private TimerTask task;
    private final Timer timer = new Timer();
    PayConfirm payconfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("正在洗车");
        wash = (Washing_no_card_record) getIntent().getSerializableExtra("wash");
        init();
        load204Info();
    }

    private void init() {
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        mLoadingDialog = UIHelper.progressDialog(WashAActivity.this, "正在请求，请稍后...");
        //动态倒入 ：设置资源
        for (int i : ids) {
            ImageView imageView = new ImageView(this);
            //imageView.setBackgroundResource(i);
            imageView.setImageResource(i);
            flipper.addView(imageView);
        }

        //设置切换时间间隔
        flipper.setFlipInterval(3000);
        //启动
        flipper.startFlipping();
        flipper.setOnTouchListener(new flipperOnTouchListener());


    }

    private void load204Info() {
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

    @Event(R.id.wash_finish)
    private void washfinishEvent(View view) {
        dialog();
    }

    @Event(R.id.back)
    private void backEvent(View view) {
        dialog();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (timer != null) {
                timer.cancel();
            }
            dialog();

        }
        return flag;
    }

    private void load_Info() {
        RequestParams params = new RequestParams(URLs.Load204Info);
        params.addQueryStringParameter("machineNo", wash.getMachine_number());
        params.addQueryStringParameter("belongCode", wash.getBelong());
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                } else {
                    if (result.isLoadOK()) {
//                        mLoadingDialog.show();
                        if (timer != null) {
                            timer.cancel();
                        }
                        payconfirm = result.payconfirm();
                        mLoadingDialog.dismiss();
                        float mon = wash.getTotal_money().floatValue() - payconfirm.getTotalmoney().floatValue();
                    }
                    if (result.isOK()) {
                        Intent intent = new Intent(WashAActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WashAActivity.this);
        builder.setMessage("您确定结束洗车吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent=new Intent(WashAActivity.this,WashConfirmActivity.class);
                intent.putExtra("payconfirm",payconfirm);
                intent.putExtra("wash",wash);
                startActivity(intent);
                finish();
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

    class flipperOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 手势判断
            switch (event.getAction()) {

                //手势按下
                case MotionEvent.ACTION_DOWN:
                    //获取手指按下的点
                    start = (int) event.getX();
                    break;
                //手势移动
                case MotionEvent.ACTION_MOVE:
                    //移动判断

                    break;
                //手离开
                case MotionEvent.ACTION_UP:

                    //按下的点 和结束的点 的插 大于100 为 向右
                    if (start - event.getX() > 1000) {
                        //可以添加过度效果
                        //下一张
                        flipper.showPrevious();
                    }


                    //按下的点 和结束的点 的插 小于100 为 向左
                    if (start - event.getX() < 1000) {
                        //上一张
                        flipper.showNext();
                    }

                    break;
            }
            return true;
        }

    }
}
