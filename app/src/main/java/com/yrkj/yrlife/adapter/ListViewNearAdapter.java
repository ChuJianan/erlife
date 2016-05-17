package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Near;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cjn on 2016/3/29.
 */
public class ListViewNearAdapter extends BaseAdapter {

    private List<Near> listItems;
    private LayoutInflater listContainer; // 视图容器

    static class ViewHolder { //自定义控件集合
        public TextView nid;
        public TextView adr;
        public TextView tel;
        public TextView dis;
        public ImageView img;
    }

    public ListViewNearAdapter(Context context, List<Near> data) {
        this.listContainer = LayoutInflater.from(context);
        this.listItems = data;
    }

    public void setNear(List<Near> data) {
        this.listItems = data;
    }
    public void addNear(List<Near> data){
        listItems.addAll(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.near_list_item, null);
            holder = new ViewHolder();

            holder.nid = (TextView) convertView.findViewById(R.id.id_text);
            holder.adr = (TextView) convertView.findViewById(R.id.adr_text);
            holder.tel = (TextView) convertView.findViewById(R.id.tel_text);
            holder.dis = (TextView) convertView.findViewById(R.id.dis_text);
            holder.img = (ImageView) convertView.findViewById(R.id.adr_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Near near = listItems.get(position);
        holder.nid.setText(near.getMachine_name());
        holder.adr.setText(near.getAddress());
//        holder.tel.setText(near.getTel());
        LatLng p1=new LatLng(UIHelper.location.getLatitude(),UIHelper.location.getLongitude());
        LatLng p2=new LatLng(Double.parseDouble(near.getLat()) ,Double.parseDouble(near.getLng()));
        double p3=DistanceUtil.getDistance(p1,p2)/1000;
        BigDecimal bigDecimal=new BigDecimal(p3);
        holder.dis.setText(bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//        holder.img.setImageBitmap();
        UIHelper.showLoadImage(holder.img, URLs.IMGURL+near.getMachine_pic(),"");

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
