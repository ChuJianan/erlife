package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Vouchers;

import java.util.List;

/**
 * Created by cjn on 2016/3/23.
 */
public class ListViewVoucherAdapter extends BaseAdapter {

    private List<Vouchers> listItems;
    private LayoutInflater listContainer; // 视图容器
    private int i;

    static class ViewHolder { //自定义控件集合
        public TextView money;
        public TextView date;
        public TextView type;
    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     */
    public ListViewVoucherAdapter(Context context, List<Vouchers> data, int i) {
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
        this.i = i;
    }

    public void setVoucher(List<Vouchers> data) {
        this.listItems = data;
    }
    public void addVoucher(List<Vouchers> data){
        listItems.addAll(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.q_list_item, null);
            holder = new ViewHolder();
            //获取控件对象
            holder.money = (TextView) convertView.findViewById(R.id.quan_text);
            holder.date = (TextView) convertView.findViewById(R.id.qsj_text);
            holder.type = (TextView) convertView.findViewById(R.id.zt_text);
            //设置控件集到convertView
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置文字和图片
        Vouchers vouchers = listItems.get(position);
        holder.money.setText(vouchers.getRepresent_price() + "");
        holder.date.setText(vouchers.getStart_time_Str() + "——" + vouchers.getDeadline());
        if (i == 3) {
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setText("已过期");
        } else if (i==2){
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setText("已使用");
        }else if (i==0){
            holder.type.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return listItems == null ? 0 : listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems == null ? null : listItems.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
