package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.MyIdear;

import java.util.List;

/**
 * Created by cjn on 2016/6/28.
 */
public class ListViewFeedBackAdapter extends BaseAdapter {

    private List<MyIdear> listItems;
    private LayoutInflater listContainer; // 视图容器

    static class ViewHolder { //自定义控件集合
        public TextView content;
        public TextView date;
    }

    public ListViewFeedBackAdapter(Context context, List<MyIdear> data) {
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }
    public void setFeed(List<MyIdear> data){
        this.listItems=data;
    }
    public void addFeed(List<MyIdear> data){
        this.listItems.addAll(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=listContainer.inflate(R.layout.faceback_listitem,null);
            holder=new ViewHolder();

            holder.content=(TextView)convertView.findViewById(R.id.content);
            holder.date=(TextView)convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyIdear myIdear=listItems.get(position);
        holder.content.setText(myIdear.getContent());
        holder.date.setText(myIdear.getCreate_timeStr());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listItems == null ? null : listItems.get(position - 1);
    }

    @Override
    public int getCount() {
        return listItems == null ? 0 : listItems.size();
    }
}
