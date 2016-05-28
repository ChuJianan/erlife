package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrkj.yrlife.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/3/17.
 */
public class GridViewMainAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> data_list=new ArrayList<Map<String,Object>>();
    private int[] icon = {R.mipmap.ic_pay, R.mipmap.ic_near,
            R.mipmap.ic_mycar, R.mipmap.ic_cards,R.mipmap.ic_discount, R.mipmap.ic_knowledge,
            R.mipmap.ic_disco, R.mipmap.ic_tools,};
    private String[] iconName = { "充 值", "附近网点", "我的爱车", "会员卡绑定","优惠券", "扫码洗车", "账 单", "违章查询"};
    private LayoutInflater mContainer;  //视图容器
    public  GridViewMainAdapter(Context context){
        mContext=context;
        getData();
        mContainer = LayoutInflater.from(context);
   }
    static class GridItemView { //自定义视图
        public ImageView icon;
        public TextView name;
    }
    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("imageview", icon[i]);
            map.put("textview", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView gridItemView;
        if(convertView == null) {
            convertView = mContainer.inflate(R.layout.grid_view, null);
            gridItemView = new GridItemView();
            gridItemView.icon = (ImageView) convertView.findViewById(R.id.icon);
            gridItemView.name = (TextView) convertView.findViewById(R.id.name);
            gridItemView.name.setTextColor(Color.parseColor("#000000"));
            convertView.setTag(gridItemView);
        } else {
            gridItemView = (GridItemView) convertView.getTag();
        }
        gridItemView.icon.setImageResource(icon[position]);
        gridItemView.name.setText(iconName[position]);
        return convertView;
    }

    @Override
    public int getCount() {
        return data_list.size();
    }

    @Override
    public Object getItem(int position) {
        return data_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
