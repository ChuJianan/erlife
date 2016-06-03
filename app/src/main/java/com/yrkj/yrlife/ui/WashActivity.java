package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.TestData;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.widget.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("无卡洗车");
        Intent intent = getIntent();
        mach_id = intent.getStringExtra("result");
        if (StringUtils.isEmpty(mach_id)) {
            wash_btn.setText("确定");
            wash_top_l.setVisibility(View.VISIBLE);
            wash_center_l.setVisibility(View.GONE);
            wash_btm_l.setVisibility(View.GONE);
            wash_balance_l.setVisibility(View.GONE);
            wash_warn_l.setVisibility(View.GONE);
        } else {
            wash_btn.setText("开始洗车");
            wash_top_l.setVisibility(View.GONE);
            wash_center_l.setVisibility(View.VISIBLE);
            wash_warn_l.setVisibility(View.GONE);
            wash_btm_l.setVisibility(View.GONE);
            wash_balance_l.setVisibility(View.GONE);
        }
    }
}
