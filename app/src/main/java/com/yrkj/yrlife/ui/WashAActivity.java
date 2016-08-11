package com.yrkj.yrlife.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ImagePagerAdapter;
import com.yrkj.yrlife.been.PayConfirm;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.Washing_no_card_record;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.ListUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.TimeCountImageView;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.AutoScrollViewPager;
import com.yrkj.yrlife.widget.SlideWashShowView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    AutoScrollViewPager flipper;
    @ViewInject(R.id.wash_machid)
    TextView wash_machid;
    @ViewInject(R.id.wash_finish)
    ImageView wash_finish;

    private int[] ids = {R.mipmap.ic_wash_top_g, R.mipmap.ic_wash_top_p, R.mipmap.ic_wash_top_x, R.mipmap.ic_wash_top_xs};
    private int start = 0;
    Washing_no_card_record wash;
    ProgressDialog mLoadingDialog;
    SharedPreferences preferences;
    private TimerTask task;
    private final Timer timer = new Timer();
    PayConfirm payconfirm;
    private BigDecimal spend_money;
    private CountDownTimer timers;
    private List<Integer> imageIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIHelper.wash_img_urls=new String[]{"http://dwz.cn/3VlgKG" ,
                "http://dwz.cn/3Vlht5" ,
                "http://dwz.cn/3VlhTg" ,
                "http://dwz.cn/3VlikY"};
        x.view().inject(this);

        title.setText("正在洗车");
        init();
        timers = new TimeCountImageView(10000, 1000, wash_finish);
        timers.start();
        load204Info();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        flipper.startAutoScroll();
    }
    private void init() {
        imageIdList = new ArrayList<Integer>();
        imageIdList.add(R.mipmap.ic_wash_top_g);
        imageIdList.add(R.mipmap.ic_wash_top_p);
        imageIdList.add(R.mipmap.ic_wash_top_x);
        imageIdList.add(R.mipmap.ic_wash_top_xs);
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(appContext, imageIdList);
        flipper.setAdapter(imagePagerAdapter.setInfiniteLoop(true));
        flipper.setOnPageChangeListener(new MyOnPageChangeListener());
        flipper.setInterval(2000);
        flipper.startAutoScroll();
        flipper.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                % ListUtils.getSize(imageIdList));

        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        String isWashing = preferences.getString("isWashing", "");
        if (!StringUtils.isEmpty(isWashing)) {
            if (isWashing.equals("1")) {
                wash = UIHelper.washing_no_card_record;
            } else if (isWashing.equals("0")) {
                wash = (Washing_no_card_record) getIntent().getSerializableExtra("wash");
            }
        }

        mLoadingDialog = UIHelper.progressDialog(WashAActivity.this, "正在请求，请稍后...");
        wash_machid.setText(wash.getMachine_number());
//        //动态倒入 ：设置资源
//        for (int i : ids) {
//            ImageView imageView = new ImageView(this);
//            //imageView.setBackgroundResource(i);
//            imageView.setImageResource(i);
//            flipper.addView(imageView);
//        }
//
//        //设置切换时间间隔
//        flipper.setFlipInterval(2000);
//        flipper.setInAnimation(this,R.anim.right_in);
//        flipper.setOutAnimation(this,R.anim.right_out);
//        //启动
//        flipper.startFlipping();
//        flipper.setOnTouchListener(new flipperOnTouchListener());


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
        timer.schedule(task, 0, 5000);
    }

    @Event(R.id.wash_finish)
    private void washfinishEvent(View view) {
        if (timer != null) {
            timer.cancel();
        }
        dialog();
    }

    @Event(R.id.back)
    private void backEvent(View view) {
        if (timer != null) {
            timer.cancel();
        }
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
                    spend_money = result.spend_money();
                } else if (result.isLoadOK()) {
//                        mLoadingDialog.show();
                    if (timer != null) {
                        timer.cancel();
                    }
                    payconfirm = result.payconfirm();
                    Intent intent = new Intent(WashAActivity.this, WashBillActivity.class);
                    intent.putExtra("payconfirm", payconfirm);
                    intent.putExtra("wash", wash);
                    startActivity(intent);
                    finish();
                } else if (result.isOK()) {
                    Intent intent = new Intent(WashAActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {

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
                Intent intent = new Intent(WashAActivity.this, WashConfirmActivity.class);
                intent.putExtra("spend_money", spend_money);
                intent.putExtra("wash", wash);
                startActivity(intent);
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

//    class flipperOnTouchListener implements View.OnTouchListener {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            // 手势判断
//            switch (event.getAction()) {
//
//                //手势按下
//                case MotionEvent.ACTION_DOWN:
//                    //获取手指按下的点
//                    start = (int) event.getX();
//                    break;
//                //手势移动
//                case MotionEvent.ACTION_MOVE:
//                    //移动判断
//
//                    break;
//                //手离开
//                case MotionEvent.ACTION_UP:
//
//                    //按下的点 和结束的点 的插 大于100 为 向右
//                    if (start - event.getX() > 1000) {
//                        //可以添加过度效果
//                        //下一张
//                        flipper.showPrevious();
//                    }
//
//
//                    //按下的点 和结束的点 的插 小于100 为 向左
//                    if (start - event.getX() < 1000) {
//                        //上一张
//                        flipper.showNext();
//                    }
//
//                    break;
//            }
//            return true;
//        }
//
//    }
public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
}
}
