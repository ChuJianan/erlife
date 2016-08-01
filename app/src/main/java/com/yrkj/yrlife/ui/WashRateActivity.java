package com.yrkj.yrlife.ui;

import android.os.Bundle;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Washing_no_card_record;

import org.xutils.view.annotation.ContentView;

/**
 * Created by cjn on 2016/8/1.
 */
@ContentView(R.layout.activity_wash_rate)
public class WashRateActivity extends BaseActivity {

    Washing_no_card_record wash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wash = (Washing_no_card_record) getIntent().getSerializableExtra("wash");
    }
}
