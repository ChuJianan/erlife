package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Complaints;
import com.yrkj.yrlife.ui.BaseActivity;
import com.yrkj.yrlife.utils.StringUtils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjn on 2016/11/14.
 */

public class ListViewComplaintsAdapter extends BaseAdapter {

    private Context context;
    private List<Complaints> listItems;
    private LayoutInflater listContainer; // 视图容器

    static class ViewHolder { //自定义控件集合
        public TextView name_text;
        public TextView time_text;
        public TextView floor_text;
        public TextView content_text;
        public ImageView me_img;
        public ImageView content_img;
    }

    public ListViewComplaintsAdapter(Context context, List<Complaints> data) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    public void setData(List<Complaints> data) {
        this.listItems = data;
    }

    public void addData(List<Complaints> data) {
        listItems.addAll(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.complaints_items, null);
            holder = new ViewHolder();

            holder.name_text = (TextView) convertView.findViewById(R.id.name_text);
            holder.time_text = (TextView) convertView.findViewById(R.id.time_text);
            holder.floor_text = (TextView) convertView.findViewById(R.id.floor_text);
            holder.content_text = (TextView) convertView.findViewById(R.id.content_text);
            holder.me_img = (ImageView) convertView.findViewById(R.id.me_img);
            holder.content_img = (ImageView) convertView.findViewById(R.id.content_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Complaints complaints = listItems.get(position);
        holder.time_text.setText(complaints.getTime());
        holder.name_text.setText(complaints.getName());
        holder.floor_text.setText(complaints.getFloor());
        if (StringUtils.isEmpty(complaints.getImg_urls())) {

        } else {
            x.image().bind(holder.me_img, complaints.getImg_urls());
        }
        if (!StringUtils.isEmpty(complaints.getContent())) {
            holder.content_text.setVisibility(View.VISIBLE);
            holder.content_img.setVisibility(View.GONE);
            holder.content_text.setText(complaints.getContent());
        } else {
            holder.content_text.setVisibility(View.GONE);
            holder.content_img.setVisibility(View.VISIBLE);
            switch (complaints.getStar_nub()) {
                case 1:
                    holder.content_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star1));
                    break;
                case 2:
                    holder.content_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star2));
                    break;
                case 3:
                    holder.content_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star3));
                    break;
                case 4:
                    holder.content_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star4));
                    break;
                case 5:
                    holder.content_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_star5));
                    break;
            }
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
