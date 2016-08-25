package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.CustomerService;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.ui.XiedingActivity;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.x;

import java.util.List;

/**
 * Created by cjn on 2016/8/25.
 */
public class ListViewKeFuAdapter extends BaseAdapter {

    private List<CustomerService> listItems;
    private LayoutInflater listContainer; // 视图容器

    public ListViewKeFuAdapter(Context context,  List<CustomerService> listItems) {
        this.listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
    }

    static class ViewHolder { //自定义控件集合
        public TextView kefu_name;
        public ImageView kefu_img;
    }

    public void setKeFu( List<CustomerService> listItems) {
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.kefu_list_item, null);
            holder = new ViewHolder();

            holder.kefu_name = (TextView) convertView.findViewById(R.id.kefu_name);
            holder.kefu_img = (ImageView) convertView.findViewById(R.id.kefu_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CustomerService customerService=listItems.get(position);
        holder.kefu_name.setText(customerService.getNickName());
        if (!StringUtils.isUrl(customerService.getHeadImage())) {
            x.image().bind(holder.kefu_img,URLs.IMGURL + customerService.getHeadImage());
        } else {
            x.image().bind(holder.kefu_img,customerService.getHeadImage());
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return listItems == null ? 0 : listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems == null ? null : listItems.get(position);
    }

}
