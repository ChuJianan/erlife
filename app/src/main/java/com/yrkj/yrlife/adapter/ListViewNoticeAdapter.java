package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yrkj.yrlife.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cjn on 2016/5/31.
 */
public class ListViewNoticeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater listContainer; // 视图容器
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
    private int[] icon = {R.mipmap.notice_item1, R.mipmap.notice_item2, R.mipmap.notice_item3};
    private String[] iconName = {"系统消息", "我的反馈", "评论动态"};
    private String[] content = {"最新的系统消息，最新的优惠", "我们的进步离不开您的每一个建议", "您暂时没有评论回复"};

    static class ViewHolder { //自定义控件集合
        public TextView title;
        public TextView content;
        public TextView num;
        public ImageView img;
    }
    public ListViewNoticeAdapter(Context context) {
        this.mContext=context;
        getData();
        this.listContainer = LayoutInflater.from(context);
    }

    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("imageview", icon[i]);
            map.put("textview", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView = listContainer.inflate(R.layout.notice_listitem, null);
            holder = new ViewHolder();
            holder.title=(TextView) convertView.findViewById(R.id.cnotice_title);
            holder.content=(TextView)convertView.findViewById(R.id.cnotice_desc);
            holder.num=(TextView)convertView.findViewById(R.id.cnotice_num);
            holder.img=(ImageView)convertView.findViewById(R.id.cnotice_icon);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(iconName[position]);
        holder.img.setImageResource(icon[position]);

//        if (position==0){
            holder.num.setVisibility(View.GONE);
//        }else {
//            holder.num.setText(position+"");
//        }
        holder.content.setText(content[position]);
        return convertView;
    }

    @Override
    public int getCount() {
        return data_list == null ? 0 : data_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return data_list == null ? null : data_list.get(position - 1);
    }
}
