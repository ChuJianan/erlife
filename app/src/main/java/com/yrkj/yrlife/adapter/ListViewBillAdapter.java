package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Bill;
import com.yrkj.yrlife.been.Consumer;

import java.util.List;

/**
 * Created by cjn on 2016/3/28.
 */
public class ListViewBillAdapter extends BaseAdapter {

    private List<Bill> listItems;
    private LayoutInflater listContainer;

    static class ViewHolder { //自定义控件集合
        public TextView costType;
        public TextView costDate;
        public TextView costMoney;
    }

    public ListViewBillAdapter(Context context, List<Bill> data) {
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    public void setConsumer(List<Bill> data) {
        this.listItems = data;
    }
    public void addConsumer(List<Bill> data){
        listItems.addAll(data);
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

        Bill bill = listItems.get(position);
        holder.costType.setText(bill.getTitle());
        holder.costMoney.setText(bill.getPrice());
        holder.costDate.setText(bill.getCreate_time());
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
