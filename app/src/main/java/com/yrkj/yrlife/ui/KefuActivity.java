package com.yrkj.yrlife.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.c.a;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewKeFuAdapter;
import com.yrkj.yrlife.been.CustomerService;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.been.User;
import com.yrkj.yrlife.hx.ui.ChatActivity;
import com.yrkj.yrlife.utils.JsonUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cjn on 2016/8/19.
 */
@ContentView(R.layout.activity_customer)
public class KefuActivity extends BaseActivity {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.kefu_list)
    ListView kefu_list;
    List<CustomerService> mlist = new ArrayList<>();
    ListViewKeFuAdapter mAdapter;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("客服中心");
        init();
        loadData();
    }

    private void init() {
        preferences = getSharedPreferences("yrlife", MODE_WORLD_READABLE);
        mAdapter = new ListViewKeFuAdapter(this, mlist);
        kefu_list.setAdapter(mAdapter);
        kefu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerService customerService = (CustomerService) mAdapter.getItem(position);
                startActivity(new Intent(KefuActivity.this, ChatActivity.class).putExtra("userId", customerService.getCid()));
            }
        });
    }

    int i = 0;

    private void loadData() {
        kefu();
    }


    private void kefu() {
        final SharedPreferences.Editor editor = preferences.edit();
        RequestParams params = new RequestParams(URLs.CustomerService);
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    for (int i = 0; i < result.customersList.size(); i++) {
                        editor.putString(result.customersList.get(i).getCid(), JsonUtils.toJson(result.customersList.get(i)));
                    }
                    editor.commit();
                    mAdapter.setKeFu(result.customersList);
                    mAdapter.notifyDataSetChanged();
                } else if (result.isOK()) {
                    UIHelper.ToastMessage(appContext, result.Message());
                    UIHelper.openLogin(KefuActivity.this);
                    finish();
                } else {
                    UIHelper.ToastMessage(appContext, result.Message());
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIHelper.ToastMessage(appContext, ex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
