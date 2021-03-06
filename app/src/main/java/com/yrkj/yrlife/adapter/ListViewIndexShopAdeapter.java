package com.yrkj.yrlife.adapter;

import android.content.Context;
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

/**
 * Created by cjn on 2016/8/5.
 */
public class ListViewIndexShopAdeapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> data_list=new ArrayList<Map<String,Object>>();
    private int[] icon = {R.mipmap.ic_index_shop, R.mipmap.ic_index_shop,
            R.mipmap.ic_index_shop};
    private String[] iconName = { "汽车内饰洗护套装", "负离子空气净化器", "高清行车记录仪"};
    private LayoutInflater mContainer;  //视图容器
    static class GridItemView { //自定义视图
        public ImageView icon;
        public TextView name;
    }

    public ListViewIndexShopAdeapter(Context context){
        mContext=context;
        getData();
        mContainer = LayoutInflater.from(context);
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
            convertView = mContainer.inflate(R.layout.index_shop_list, null);
            gridItemView = new GridItemView();
            gridItemView.icon = (ImageView) convertView.findViewById(R.id.shop_img);
            gridItemView.name = (TextView) convertView.findViewById(R.id.shop_jifen);
//            gridItemView.name.setTextColor(Color.parseColor("#000000"));
            convertView.setTag(gridItemView);
        } else {
            gridItemView = (GridItemView) convertView.getTag();
        }
        gridItemView.icon.setImageResource(icon[position]);
//        gridItemView.name.setText(iconName[position]);
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
