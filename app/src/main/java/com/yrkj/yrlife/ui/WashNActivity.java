package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.apmobilesecuritysdk.face.APSecuritySdk;
import com.yrkj.yrlife.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/7/30.
 */
@ContentView(R.layout.activity_wash_n)
public class WashNActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.wash_nub)
    EditText wash_nub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("启动机器");
    }

    @Event(R.id.wash_btn)
    private void washbtnEvent(View view){
        Intent intent=new Intent(this,WashNnActivity.class);
        intent.putExtra("wash_nub",wash_nub.getText().toString());
        startActivity(intent);
    }
}
