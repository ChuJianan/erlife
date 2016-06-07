package com.yrkj.yrlife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.ListViewNoticeAdapter;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.NewsActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by cjn on 2016/3/17.
 */
@ContentView(R.layout.fragment_ad)
public class FragmentAd extends BaseFragment {

    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.notice_listView)
    private ListView notice_listview;

    ListViewNoticeAdapter mAdapter;
    LinearLayout ls;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.back).setVisibility(View.INVISIBLE);
        title.setText("消息中心");
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);
        init();
    }

    private void init() {
        mAdapter=new ListViewNoticeAdapter(getContext());
        notice_listview.setAdapter(mAdapter);
        notice_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) {
                    Intent intent=new Intent(getActivity(), NewsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
