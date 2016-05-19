package com.yrkj.yrlife.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Cards;

import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class ListViewCardAdapter extends BaseAdapter {

    private List<Cards> listItems;//数据集合
    private LayoutInflater listContainer; // 视图容器

    static class ViewHolder { //自定义控件集合
        public TextView cardnumber;
        public TextView cardtype;
        public RelativeLayout cardrl;
    }

    /**
     * 实例化Adapter
     *
     * @param context
     * @param data
     */
    public ListViewCardAdapter(Context context, List<Cards> data) {
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    public void setCards(List<Cards> data){
        this.listItems=data;
    }

    public void addCards(List<Cards> data){
        listItems.addAll(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.hy_list_item, null);
            holder = new ViewHolder();
            //获取控件对象
            holder.cardnumber = (TextView) convertView.findViewById(R.id.card_number);
            holder.cardtype = (TextView) convertView.findViewById(R.id.card_type);
            holder.cardrl = (RelativeLayout) convertView.findViewById(R.id.card_rl);
            //设置控件集到convertView
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置文字和图片
        Cards cards = listItems.get(position);
        holder.cardnumber.setText(cards.getCardNumber());
//        if (cards.getCardType().equals("1")) {
            holder.cardtype.setText(cards.getType_name());
//            holder.cardrl.setBackgroundResource(R.drawable.ic_hycard_bg);
//        } else {
//            holder.cardtype.setText("体验卡");
            holder.cardrl.setBackgroundResource(R.drawable.ic_tycard_bg);
//        }

        return convertView;
    }

    @Override
    public int getCount() {
        return listItems == null ? 0 : listItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listItems == null ? null : listItems.get(position - 1);
    }
}
