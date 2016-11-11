package com.yrkj.yrlife.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.HomePage;
import com.yrkj.yrlife.widget.JDAdverView;

import java.util.List;

/**
 * Created by cjn on 2016/11/11.
 */

public class JDViewAdapter {
    private List<HomePage.SystemMessageSectionBean> mDatas;
    public JDViewAdapter(List<HomePage.SystemMessageSectionBean> mDatas) {
        this.mDatas = mDatas;
        if (mDatas == null || mDatas.isEmpty()) {
            throw new RuntimeException("nothing to show");
        }
    }
    /**
     * 获取数据的条数
     * @return
     */
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取摸个数据
     * @param position
     * @return
     */
    public HomePage.SystemMessageSectionBean getItem(int position) {
        return mDatas.get(position);
    }
    /**
     * 获取条目布局
     * @param parent
     * @return
     */
    public View getView(JDAdverView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);
    }

    /**
     * 条目数据适配
     * @param view
     * @param data
     */
    public void setItem(final View view, final HomePage.SystemMessageSectionBean data) {
        TextView tv = (TextView) view.findViewById(R.id.time);
        tv.setText(data.getMessageTimeStr());
        TextView tag = (TextView) view.findViewById(R.id.title);
        tag.setText(data.getMessageTitle());
        //你可以增加点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如打开url
//                Toast.makeText(view.getContext(), data.url, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
