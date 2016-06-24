package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.yrkj.yrlife.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/6/24.
 */
@ContentView(R.layout.activity_test)
public class TestActivity extends BaseActivity {

    @ViewInject(R.id.title)
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("正在开发中...");
    }
}
