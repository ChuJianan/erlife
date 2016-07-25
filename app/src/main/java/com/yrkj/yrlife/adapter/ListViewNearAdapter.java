package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.amap.api.navi.AMapNavi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.Near;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cjn on 2016/3/29.
 */
public class ListViewNearAdapter extends BaseAdapter {

    private Context context;
    private List<Near> listItems;
    private LayoutInflater listContainer; // 视图容器

    static class ViewHolder { //自定义控件集合
        public TextView nid;
        public TextView adr;
        public TextView order;
        public TextView dis;
        public TextView state;
        public ImageView img;
        public LinearLayout daohang;
    }

    public ListViewNearAdapter(Context context, List<Near> data) {
        this.context=context;
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
            holder.order = (TextView) convertView.findViewById(R.id.text_order);
            holder.dis = (TextView) convertView.findViewById(R.id.dis_text);
            holder.img = (ImageView) convertView.findViewById(R.id.adr_img);
            holder.daohang=(LinearLayout)convertView.findViewById(R.id.daohang);
            holder.state=(TextView)convertView.findViewById(R.id.text_state);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Near near = listItems.get(position);
        holder.nid.setText(near.getMachine_name());
        holder.adr.setText(near.getAddress());
        if (!StringUtils.isEmpty(near.getIsWashing())){
            if (near.getIsWashing().equals("1")){
                holder.state.setText("忙碌");
                holder.state.setBackgroundResource(R.drawable.textview_border_m);
//                holder.state.setBackgroundColor(context.getResources().getColor(R.color.near_listitem_ml));
            }else if (near.getIsWashing().equals("0")){
                holder.state.setText("可用");
//                holder.state.setBackgroundColor(context.getResources().getColor(R.color.near_listitem_ky));
                holder.state.setBackgroundResource(R.drawable.textview_border);
            }
        }else {
            holder.state.setText("忙碌");
            holder.state.setBackgroundResource(R.drawable.textview_border_m);
//            holder.state.setBackgroundColor(context.getResources().getColor(R.color.near_listitem_ml));
        }
        holder.order.setText(near.getOrders()+"");
//        holder.tel.setText(near.getTel());
        LatLng p1=new LatLng(UIHelper.location.getLatitude(),UIHelper.location.getLongitude());
//        p1=UIHelper.converter(p1);
        final LatLng p2=new LatLng(Double.parseDouble(near.getLat()) ,Double.parseDouble(near.getLng()));
        double p3=DistanceUtil.getDistance(p1,p2)/1000;
        BigDecimal bigDecimal=new BigDecimal(p3);
        holder.dis.setText(bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//        holder.img.setImageBitmap();

        UIHelper.showLoadImage(holder.img, URLs.IMGURL+near.getMachine_pic(),"");
        holder.daohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIHelper.isInstallPackage("com.baidu.BaiduMap")){
                    UIHelper.openBaiduMap(Double.parseDouble(near.getLat()),Double.parseDouble(near.getLng()),near.getAddress(),context);
                }else if (UIHelper.isInstallPackage("com.autonavi.minimap")){
                    UIHelper.openGaoDeMap(Double.parseDouble(near.getLat()),Double.parseDouble(near.getLng()),near.getAddress(),context);
                }else {
//                    UIHelper.openNavigation(context,p2,near.getMachine_name());
                    UIHelper.ToastMessage(context,"非常抱歉，您的手机上暂没我们支持的地图导航，请下载百度地图或高德地图");
//                    UIHelper.Navi_Local(context,p2);
                }
            }
        });
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
