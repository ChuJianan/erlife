package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewConsumerAdapter;
import com.yrkj.yrlife.been.Consumer;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费记录
 * Created by cjn on 2016/3/28.
 */
@ContentView(R.layout.activity_consumer)
public class ConsumerActivity extends BaseActivity {

    private ListViewConsumerAdapter mAdapter;
    private List<Consumer> list = new ArrayList<Consumer>();
    private Consumer consumer;

    @ViewInject(R.id.cost_listView)
    private ListView costListView;
    @ViewInject(R.id.title)
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("消费记录");
        initView();
        initData();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart("消费记录");
//        MobclickAgent.onResume(this);
//    }

    private void initView() {
        mAdapter = new ListViewConsumerAdapter(this, list);
        costListView.setAdapter(mAdapter);
    }

    private void initData() {
        list.clear();
        consumer = new Consumer();
        consumer.setCostType("中石化第28加油站 洗车机");
        consumer.setCostDate("2016年2月26日  15:15:35");
        consumer.setCostMoney("-10元");
        list.add(consumer);
        consumer = new Consumer();
        consumer.setCostType("辽阳东路15号 网点 洗车机");
        consumer.setCostDate("2016年3月16日  09:15:30");
        consumer.setCostMoney("-10元");
        list.add(consumer);
        consumer = new Consumer();
        consumer.setCostType("银川西路123号 网点 洗车机");
        consumer.setCostDate("2016年3月28日  19:15:30");
        consumer.setCostMoney("-10元");
        list.add(consumer);
        mAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("消费记录");
//        MobclickAgent.onPause(this);
//    }
}
