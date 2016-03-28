package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Consumer;

import java.util.List;

/**
 * Created by cjn on 2016/3/28.
 */
public class ListViewConsumerAdapter extends BaseAdapter {

    private List<Consumer> listItems;
    private LayoutInflater listContainer;

    static class ViewHolder { //自定义控件集合
        public TextView costType;
        public TextView costDate;
        public TextView costMoney;
    }

    public  ListViewConsumerAdapter(Context context, List<Consumer> data) {
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.consumer_list_item, null);
            holder = new ViewHolder();

            holder.costType = (TextView) convertView.findViewById(R.id.cost_type);
            holder.costDate = (TextView) convertView.findViewById(R.id.cost_date);
            holder.costMoney = (TextView) convertView.findViewById(R.id.cost_money);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Consumer consumer = listItems.get(position);
        holder.costType.setText(consumer.getCostType());
        holder.costMoney.setText(consumer.getCostMoney());
        holder.costDate.setText(consumer.getCostDate());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
