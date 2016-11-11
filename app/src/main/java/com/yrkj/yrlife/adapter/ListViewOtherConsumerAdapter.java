//package com.yrkj.yrlife.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.yrkj.yrlife.R;
//import com.yrkj.yrlife.been.HomePage;
//import com.yrkj.yrlife.utils.StringUtils;
//
//import org.xutils.x;
//
//import java.util.List;
//
///**
// * Created by cjn on 2016/11/3.
// */
//
//public class ListViewOtherConsumerAdapter extends BaseAdapter {
//    private List<HomePage.WashMessageSectionBean> listItems;
//    private LayoutInflater listContainer;
//
//    static class ViewHolder { //自定义控件集合
//        public TextView other_name;
//        public TextView other_time;
//        public TextView other_msg;
//        public ImageView other_pic;
//        public ImageView wash_pic;
//    }
//
//    public ListViewOtherConsumerAdapter(Context context, List<HomePage.WashMessageSectionBean> listItems) {
//        this.listContainer = LayoutInflater.from(context);
//        this.listItems = listItems;
//    }
//
//    public void addOtherConsumer(List<HomePage.WashMessageSectionBean> listItems) {
//        this.listItems.addAll(listItems);
//    }
//
//    public void setOtherConsumer(List<HomePage.WashMessageSectionBean> listItems) {
//        this.listItems = listItems;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = listContainer.inflate(R.layout.other_consumption_item, null);
//            holder = new ViewHolder();
//
//            holder.other_msg = (TextView) convertView.findViewById(R.id.other_msg);
//            holder.other_time = (TextView) convertView.findViewById(R.id.other_time);
//            holder.other_name = (TextView) convertView.findViewById(R.id.other_name);
//            holder.other_pic = (ImageView) convertView.findViewById(R.id.other_pic);
//            holder.wash_pic = (ImageView) convertView.findViewById(R.id.wash_pic);
//
//            convertView.setTag(holder);
//
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        HomePage.WashMessageSectionBean washMessageSectionBean = listItems.get(position);
//        holder.other_name.setText(washMessageSectionBean.getUserName());
//        holder.other_time.setText(washMessageSectionBean.getWashTime());
//        holder.other_msg.setText(washMessageSectionBean.getContent());
//        if (!StringUtils.isEmpty(washMessageSectionBean.getUserImage())){
//            x.image().bind(holder.other_pic, washMessageSectionBean.getUserImage());
//        }
//        if (!StringUtils.isEmpty(washMessageSectionBean.getMachinePicture())){
//            x.image().bind(holder.wash_pic, washMessageSectionBean.getMachinePicture());
//        }
//        return convertView;
//    }
//
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return listItems == null ? null : listItems.get(position - 1);
//    }
//
//    @Override
//    public int getCount() {
//        return listItems == null ? 0 : listItems.size();
//    }
//}
