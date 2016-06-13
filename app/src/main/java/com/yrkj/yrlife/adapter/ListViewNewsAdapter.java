package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.News;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.UIHelper;

import java.util.List;

/**
 * Created by cjn on 2016/6/7.
 */
public class ListViewNewsAdapter extends BaseAdapter {
    private List<News> listItems;
    private LayoutInflater listContainer; // 视图容器

    static class ViewHolder { //自定义控件集合
        public TextView title;
        public TextView date;
        public TextView outline;
        public ImageView img;
    }

    public ListViewNewsAdapter(Context context, List<News> data) {
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    public void setNews(List<News> data) {
        this.listItems = data;
    }

    public void addNews(List<News> data) {
        this.listItems = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.list_news_item, null);
            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.title_news);
            holder.date = (TextView) convertView.findViewById(R.id.data_news);
            holder.outline = (TextView) convertView.findViewById(R.id.outline_news);
            holder.img = (ImageView) convertView.findViewById(R.id.nav_image_news);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        News news=listItems.get(position);
        holder.title.setText(news.getTitle());
        holder.date.setText(news.getPublish_timeStr());
        holder.outline.setText("");
        news.setNav_img(news.getNav_img().replaceAll("\\\\", "/"));
        UIHelper.showLoadImage(holder.img, URLs.IMGURL+news.getNav_img(),"");

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
