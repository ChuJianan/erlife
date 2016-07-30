package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.yrkj.yrlife.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/7/30.
 */
@ContentView(R.layout.activity_wash_a)
public class WashAActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("正在洗车");
    }
}
