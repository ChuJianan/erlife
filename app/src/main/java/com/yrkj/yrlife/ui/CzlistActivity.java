package com.yrkj.yrlife.ui;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewCzAdapter;
import com.yrkj.yrlife.been.Pay;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/3/25.
 */
@ContentView(R.layout.activity_czlist)
public class CzlistActivity extends  BaseActivity {
    private ListViewCzAdapter listViewCzAdapter;
    private Pay pay;
    private List<Pay> list=new ArrayList<Pay>();

    @ViewInject(R.id.cz_listView)
    private ListView pay_listView;
    @ViewInject(R.id.title)
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("充值记录");
        init();
        initData();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart("充值记录");
//        MobclickAgent.onResume(this);
//    }

    private  void  init(){
        listViewCzAdapter=new ListViewCzAdapter(this,list);
        pay_listView.setAdapter(listViewCzAdapter);
    }

    private void initData(){
        list.clear();

        pay=new Pay();
        pay.setPayType("微信");
        pay.setPayDate("2016年3月25日   13：52：20");
        pay.setPayMoney("+120元");
        list.add(pay);
        pay=new Pay();
        pay.setPayType("中石化银川西路网点");
        pay.setPayDate("2016年3月21日   13：52：20");
        pay.setPayMoney("+100元");
        list.add(pay);
        listViewCzAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("充值记录");
//        MobclickAgent.onPause(this);
//    }
}
