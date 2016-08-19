package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.hx.ui.ChatActivity;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by cjn on 2016/8/19.
 */
@ContentView(R.layout.activity_customer)
public class KefuActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("客服中心");

    }
    @Event(R.id.kefu1)
    private void onkefu1Event(View view){
        startActivity(new Intent(KefuActivity.this, ChatActivity.class).putExtra("userId", "322"));
    }
    @Event(R.id.kefu2)
    private void onkefu2Event(View view){
        startActivity(new Intent(KefuActivity.this, ChatActivity.class).putExtra("userId", "240"));
    }
}
