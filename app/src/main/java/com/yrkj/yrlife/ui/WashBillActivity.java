package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.media.TimedText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.PayConfirm;
import com.yrkj.yrlife.been.Washing_no_card_record;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/8/1.
 */
@ContentView(R.layout.activity_wash_bill)
public class WashBillActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.wash_pay_dis)
    private TextView wash_pay_dis;
    @ViewInject(R.id.wash_order_no)
    private TextView wash_order_no;
    @ViewInject(R.id.wash_machid_dis)
    private TextView wash_machid_dis;
    @ViewInject(R.id.wash_date)
    private TextView wash_date;
    @ViewInject(R.id.wash_rate)
    Button wash_rate;


    PayConfirm payConfirm;
    Washing_no_card_record wash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("订单详情");
        payConfirm = (PayConfirm) getIntent().getSerializableExtra("payconfirm");
        wash = (Washing_no_card_record) getIntent().getSerializableExtra("wash");
        init();
    }

    private void init() {
        wash_pay_dis.setText(payConfirm.getTotalmoney().toString());
        wash_order_no.setText(payConfirm.getBelong());
        wash_machid_dis.setText(payConfirm.getMachinenumber());
        wash_date.setText(payConfirm.getTime());
        if (payConfirm == null) {
            wash_rate.setText("返回");
        } else if (payConfirm.getTotalmoney().floatValue() == 0) {
            wash_rate.setText("返回");
        }
    }

    @Event(R.id.wash_rate)
    private void washrateEvent(View view) {
        if (payConfirm == null) {
            finish();
        } else if (payConfirm.getTotalmoney().floatValue() == 0) {
            finish();
        } else {
            Intent intent = new Intent(this, WashRateActivity.class);
            intent.putExtra("wash", wash);
            startActivity(intent);
            finish();
        }
    }
}
