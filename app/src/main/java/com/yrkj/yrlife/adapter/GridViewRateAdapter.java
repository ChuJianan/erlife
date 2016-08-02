package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yrkj.yrlife.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/8/2.
 */
public class GridViewRateAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mContainer;  //视图容器
    private String[] iconName = {"操作简单", "价格便宜", "洗车干净", "节省时间",
            "有点时尚", "地点好找", "很有乐趣", "科技智能", "功能强大"};
    private List<String> data_list=new ArrayList<>();

    public GridViewRateAdapter(Context context) {
        mContext = context;
        getData();
        mContainer = LayoutInflater.from(context);
    }

    static class GridItemView { //自定义视图
        public TextView btn;
//        public TextView name;
    }
    private List<String> getData(){
        for (int i=0;i<iconName.length;i++){
            data_list.add(iconName[i]);
        }
        return data_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GridItemView gridItemView;
        if(convertView == null) {
            convertView = mContainer.inflate(R.layout.rate_grid_item, null);
            gridItemView = new GridItemView();
            gridItemView.btn = (TextView) convertView.findViewById(R.id.rate_item_btn);
            convertView.setTag(gridItemView);
        }else {
            gridItemView = (GridItemView) convertView.getTag();
        }
        gridItemView.btn.setText(iconName[position]);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return data_list.size();
    }

    @Override
    public Object getItem(int position) {
        return data_list.get(position);
    }
}
