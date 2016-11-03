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

public class ListViewRateItemAdapter extends BaseAdapter {
    private List<HomePage.RemarkStarSectionBean> listItems;
    private LayoutInflater listContainer;
    Context context;

    static class ViewHolder { //自定义控件集合
        public TextView rate_name;
        public TextView rate_wash_name;
        public TextView rate_time;
        public TextView rate_item_name;
        public TextView rate_adr;
        public ImageView rate_pic;
        public ImageView rate_img;
        public ImageView wash_pic;
    }

    public ListViewRateItemAdapter(Context context, List<HomePage.RemarkStarSectionBean> listItems) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
    }

    public void addRateItem(List<HomePage.RemarkStarSectionBean> listItems) {
        this.listItems.addAll(listItems);
    }

    public void setRateItem(List<HomePage.RemarkStarSectionBean> listItems) {
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.rate_item, null);
            holder = new ViewHolder();

            holder.rate_name = (TextView) convertView.findViewById(R.id.rate_name);
            holder.rate_wash_name = (TextView) convertView.findViewById(R.id.rate_wash_name);
            holder.rate_time = (TextView) convertView.findViewById(R.id.rate_time);
            holder.rate_item_name = (TextView) convertView.findViewById(R.id.rate_item_name);
            holder.rate_adr = (TextView) convertView.findViewById(R.id.rate_adr);
            holder.rate_pic = (ImageView) convertView.findViewById(R.id.rate_pic);
            holder.rate_img = (ImageView) convertView.findViewById(R.id.rate_img);
            holder.wash_pic = (ImageView) convertView.findViewById(R.id.wash_pic);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HomePage.RemarkStarSectionBean remarkStarSectionBean = listItems.get(position);
        holder.rate_name.setText(remarkStarSectionBean.getUserName());
        holder.rate_adr.setText(remarkStarSectionBean.getAddress());
        holder.rate_time.setText(remarkStarSectionBean.getRemarkTime());
        holder.rate_item_name.setText(remarkStarSectionBean.getMachine_name());
        holder.rate_wash_name.setText(remarkStarSectionBean.getMachine_name());

        x.image().bind(holder.rate_pic, remarkStarSectionBean.getUserImage());
        x.image().bind(holder.wash_pic, remarkStarSectionBean.getMachine_pic());
        switch (remarkStarSectionBean.getStars()) {
            case 1:
                holder.rate_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star1));
                break;
            case 2:
                holder.rate_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star2));
                break;
            case 3:
                holder.rate_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star3));
                break;
            case 4:
                holder.rate_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star4));
                break;
            case 5:
                holder.rate_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star5));
                break;
        }
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
