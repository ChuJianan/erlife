package com.yrkj.yrlife.ui;

import android.os.Bundle;

import com.yrkj.yrlife.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by cjn on 2016/11/14.
 */

@ContentView(R.layout.activity_complaints)
public class ComplaintsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
