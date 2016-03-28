package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Pay;

import org.xutils.view.annotation.ContentView;

import java.util.List;

/**
 * Created by cjn on 2016/3/25.
 */
public class ListViewCzAdapter extends BaseAdapter {

    private List<Pay> listItems;
    private LayoutInflater listContainer; // 视图容器

    static class ViewHolder { //自定义控件集合
        public TextView payType;
        public TextView dateType;
        public TextView moneyPay;
    }

    public ListViewCzAdapter(Context context,List<Pay> data){
        this.listContainer=LayoutInflater.from(context);
        this.listItems=data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=listContainer.inflate(R.layout.cz_list_items,null);
            holder=new ViewHolder();

            holder.payType=(TextView)convertView.findViewById(R.id.pay_type);
            holder.dateType=(TextView)convertView.findViewById(R.id.date_pay);
            holder.moneyPay=(TextView)convertView.findViewById(R.id.pay_money);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Pay pay=listItems.get(position);
        holder.payType.setText(pay.getPayType());
        holder.moneyPay.setText(pay.getPayMoney());
        holder.dateType.setText(pay.getPayDate());

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
