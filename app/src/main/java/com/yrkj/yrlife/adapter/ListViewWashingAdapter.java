package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.HomePage;

import org.xutils.x;

import java.util.List;

/**
 * Created by cjn on 2016/11/3.
 */

public class ListViewWashingAdapter extends BaseAdapter {
    private List<HomePage.RunningMachineSectionBean> listItems;
    private LayoutInflater listContainer;
    static class ViewHolder { //自定义控件集合
        public TextView wash_item_nub;
        public TextView wash_item_name;
        public TextView wash_item_adr;
        public ImageView wash_item_pic;
    }

    public ListViewWashingAdapter(Context context, List<HomePage.RunningMachineSectionBean> listItems){
        this.listContainer = LayoutInflater.from(context);
        this.listItems=listItems;
    }
    public void addWashing(List<HomePage.RunningMachineSectionBean> listItems) {
        this.listItems.addAll(listItems);
    }

    public void setWashing(List<HomePage.RunningMachineSectionBean> listItems) {
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.washing_item, null);
            holder = new ViewHolder();

            holder.wash_item_nub=(TextView) convertView.findViewById(R.id.wash_item_nub);
            holder.wash_item_adr=(TextView) convertView.findViewById(R.id.adr_text);
            holder.wash_item_pic=(ImageView) convertView.findViewById(R.id.wash_item_pic);
            holder.wash_item_name=(TextView) convertView.findViewById(R.id.id_text);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomePage.RunningMachineSectionBean runningMachineSectionBean=listItems.get(position);
        holder.wash_item_nub.setText(runningMachineSectionBean.getOrders()+"");
        holder.wash_item_name.setText(runningMachineSectionBean.getMachine_name());
        holder.wash_item_adr.setText(runningMachineSectionBean.getAddress());
        x.image().bind(holder.wash_item_pic,runningMachineSectionBean.getMachine_pic());

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
