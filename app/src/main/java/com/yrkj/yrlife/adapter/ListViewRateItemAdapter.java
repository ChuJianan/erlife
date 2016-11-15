package com.yrkj.yrlife.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.been.HomePage;
import com.yrkj.yrlife.been.Near;
import com.yrkj.yrlife.been.Result;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.ui.DetailNearActivity;
import com.yrkj.yrlife.utils.CommonUtils;
import com.yrkj.yrlife.utils.JsonUtils;
import com.yrkj.yrlife.utils.StringUtils;
import com.yrkj.yrlife.utils.UIHelper;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by cjn on 2016/11/3.
 */

public class ListViewRateItemAdapter extends BaseAdapter {
    private List<HomePage.RemarkStarSectionBean> listItems;
    private LayoutInflater listContainer;
    Context context;
    boolean isClick = false;

    static class ViewHolder { //自定义控件集合
        public TextView rate_name;
        public TextView rate_wash_name;
        public TextView rate_time;
        public TextView rate_item_name;
        public TextView rate_adr;
        public TextView rate_laud_nub;
        public TextView rate_comment;
        public TextView rate_complaints_nub;
        public TextView rate_washMoney;
        public ImageView rate_pic;
        public ImageView rate_img;
        public ImageView wash_pic;
        public ImageView rate_laud_img;
        public LinearLayout rate_laud_l;
        public LinearLayout rate_laud_ll;
        public LinearLayout rate_complaints_l;
        public LinearLayout rate_complaints_ll;
        public LinearLayout rate_wash;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.rate_item, null);
            holder = new ViewHolder();

            holder.rate_name = (TextView) convertView.findViewById(R.id.rate_name);
            holder.rate_wash_name = (TextView) convertView.findViewById(R.id.rate_wash_name);
            holder.rate_time = (TextView) convertView.findViewById(R.id.rate_time);
            holder.rate_item_name = (TextView) convertView.findViewById(R.id.rate_item_name);
            holder.rate_adr = (TextView) convertView.findViewById(R.id.rate_adr);
            holder.rate_laud_nub = (TextView) convertView.findViewById(R.id.rate_laud_nub);
            holder.rate_comment = (TextView) convertView.findViewById(R.id.rate_comment);
            holder.rate_complaints_nub = (TextView) convertView.findViewById(R.id.rate_complaints_nub);
            holder.rate_washMoney = (TextView) convertView.findViewById(R.id.rate_money);
            holder.rate_pic = (ImageView) convertView.findViewById(R.id.rate_pic);
            holder.rate_img = (ImageView) convertView.findViewById(R.id.rate_img);
            holder.wash_pic = (ImageView) convertView.findViewById(R.id.wash_pic);
            holder.rate_laud_img = (ImageView) convertView.findViewById(R.id.rate_laud_img);
            holder.rate_laud_l = (LinearLayout) convertView.findViewById(R.id.rate_laud_l);
            holder.rate_laud_ll = (LinearLayout) convertView.findViewById(R.id.rate_laud_ll);
            holder.rate_complaints_l = (LinearLayout) convertView.findViewById(R.id.rate_complaints_l);
            holder.rate_complaints_ll = (LinearLayout) convertView.findViewById(R.id.rate_complaints_ll);
            holder.rate_wash = (LinearLayout) convertView.findViewById(R.id.rate_wash);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final HomePage.RemarkStarSectionBean remarkStarSectionBean = listItems.get(position);
        final int ran = (int) (50 * Math.random() + 50);
        holder.rate_name.setText(remarkStarSectionBean.getUserName());
        holder.rate_adr.setText(remarkStarSectionBean.getAddress());
        holder.rate_time.setText(remarkStarSectionBean.getRemarkTime());
        holder.rate_item_name.setText(remarkStarSectionBean.getMachine_name());
        holder.rate_wash_name.setText(remarkStarSectionBean.getMachine_name());
        holder.rate_washMoney.setText(remarkStarSectionBean.getWashMoney());
        holder.rate_laud_nub.setText(remarkStarSectionBean.getPraiseCount() + "");
        holder.rate_complaints_nub.setText(ran + "");
        if (StringUtils.isEmpty(remarkStarSectionBean.getRemarkContent())) {
            holder.rate_comment.setVisibility(View.GONE);
        } else {
            holder.rate_comment.setVisibility(View.VISIBLE);
            holder.rate_comment.setText(remarkStarSectionBean.getRemarkContent());
        }


        if (!StringUtils.isEmpty(remarkStarSectionBean.getUserImage())) {
            x.image().bind(holder.rate_pic, remarkStarSectionBean.getUserImage());
        }
        if (!StringUtils.isEmpty(remarkStarSectionBean.getMachine_pic())) {
            x.image().bind(holder.wash_pic, remarkStarSectionBean.getMachine_pic());
        }

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

        if (remarkStarSectionBean.isIfPraise()) {
            holder.rate_laud_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_laud_click));
        } else {
            holder.rate_laud_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_laud));
        }
        holder.rate_laud_img.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                isClick = remarkStarSectionBean.isIfPraise();
                if (StringUtils.isEmpty(URLs.secret_code)) {
                    UIHelper.ToastMessage(context, "请登陆后点赞");
                } else {
                    if (isClick) {
                        praise(remarkStarSectionBean, "0", holder);
                    } else {
                        praise(remarkStarSectionBean, "1", holder);
                    }
                }
            }
        });
        holder.rate_wash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePage.RemarkStarSectionBean washing = (HomePage.RemarkStarSectionBean) listItems.get(position);
                Near near = new Near();
                near.setAddress(washing.getAddress());
                near.setDetailUrl(washing.getDetailUrl());
                near.setIsWashing(washing.getIsWashing());
                near.setLat(washing.getLat());
                near.setLng(washing.getLng());
                near.setMachine_name(washing.getMachine_name());
                near.setMachine_number(washing.getMachine_number());
                near.setMachineImages(washing.getMachineImages());
                near.setQrCodeUrl(washing.getQrCodeUrl());
                near.setOrders(washing.getOrders());
                near.setMachine_pic(washing.getMachine_pic());
                Intent intent = new Intent(context, DetailNearActivity.class);
                intent.putExtra("near", near);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    private void praise(final HomePage.RemarkStarSectionBean remarkStarSectionBean, final String flag, final ViewHolder holder) {

        RequestParams params = new RequestParams(URLs.PRAISE);
        params.addQueryStringParameter("remarkId", remarkStarSectionBean.getRemarkId() + "");
        params.addQueryStringParameter("secret_code", URLs.secret_code);
        params.addQueryStringParameter("flag", flag);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
                Result result = JsonUtils.fromJson(string, Result.class);
                if (result.OK()) {
                    if (flag.equals("0")) {
                        holder.rate_laud_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_laud));
                        isClick = false;
                        remarkStarSectionBean.setIfPraise(isClick);
                        String nub = holder.rate_laud_nub.getText().toString();
                        int a = StringUtils.toInt(nub, remarkStarSectionBean.getPraiseCount() - 1);
                        holder.rate_laud_nub.setText(a - 1 + "");
                    }
                    if (flag.equals("1")) {
                        holder.rate_laud_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_laud_click));
                        isClick = true;
                        remarkStarSectionBean.setIfPraise(isClick);
                        String nub = holder.rate_laud_nub.getText().toString();
                        int a = StringUtils.toInt(nub, remarkStarSectionBean.getPraiseCount() + 1);
                        holder.rate_laud_nub.setText(a + 1 + "");
                    }
                } else if(result.isOK()){
                    URLs.secret_code = "";
                    UIHelper.ToastMessage(context, result.Message());
                } else {
                    UIHelper.ToastMessage(context, result.Message());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public abstract class NoDoubleClickListener implements View.OnClickListener {
        final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
                return;
            }
        }

        protected abstract void onNoDoubleClick(View v);
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
