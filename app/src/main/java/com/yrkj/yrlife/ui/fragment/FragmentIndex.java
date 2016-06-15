package com.yrkj.yrlife.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.GridViewMainAdapter;
import com.yrkj.yrlife.adapter.GridViewShopAdapter;
import com.yrkj.yrlife.app.AppException;
import com.yrkj.yrlife.app.YrApplication;
import com.yrkj.yrlife.been.URLs;
import com.yrkj.yrlife.ui.AdrListActivity;
import com.yrkj.yrlife.ui.BinCardActivity;
import com.yrkj.yrlife.ui.ConsumerActivity;
import com.yrkj.yrlife.ui.NearActivity;
import com.yrkj.yrlife.ui.PayActivity;
import com.yrkj.yrlife.ui.WashsActivity;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.MyGridView;

import org.json.JSONException;
import org.xutils.view.annotation.ContentView;


/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_index)
public class FragmentIndex extends BaseFragment{

    private MyGridView gview,shop_grid;
    private GridViewMainAdapter sim_adapter;
    private GridViewShopAdapter shopAdapter;
    View view;
//    ImageView center_img,center_img2,center_img3;
    BitmapManager bitmapManager=new BitmapManager();
    TextView LocationResult;
    YrApplication application;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout ls=(LinearLayout)getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.VISIBLE);
        LinearLayout ll_location=(LinearLayout)getActivity().findViewById(R.id.ll_location);
        LocationResult=(TextView)getActivity().findViewById(R.id.location_text);
        gview = (MyGridView) view.findViewById(R.id.grid);
        shop_grid=(MyGridView)view.findViewById(R.id.shop_gridView);
//            center_img=(ImageView)view.findViewById(R.id.center_img);
//            center_img2=(ImageView)view.findViewById(R.id.center_img2);
//            center_img3=(ImageView)view.findViewById(R.id.center_img3);
        application = (YrApplication)getActivity().getApplication();
        init();
        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdrListActivity.class);
                startActivity(intent);
            }
        });
        LocationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdrListActivity.class);
                startActivity(intent);
            }
        });
    }

    Intent intent;
    String string="";
    private  void  init(){
        sim_adapter=new GridViewMainAdapter(getActivity());
        shopAdapter=new GridViewShopAdapter(getContext());
        //配置适配器
        shop_grid.setAdapter(shopAdapter);
        gview.setAdapter(sim_adapter);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://充值
                        if (URLs.secret_code==""){
                            UIHelper.openLogin(getActivity());
                        }else{
                        intent = new Intent(getActivity(), PayActivity.class);
                        startActivity(intent);
                        }
                        break;
                    case 1://附近网点
                        intent=new Intent(getActivity(), NearActivity.class);
                        startActivity(intent);
                        break;
                    case 2://我的爱车
                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
                        break;
                    case 3://会员卡绑定
                        if (URLs.secret_code==""){
                            UIHelper.openLogin(getActivity());
                        }else {
                            intent = new Intent(getActivity(), BinCardActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 4://优惠券
//                        if (URLs.secret_code==""){
//                            UIHelper.openLogin(getActivity());
//                        }else {
//
//                        }
                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
                        break;
                    case 5://扫码洗车
                        if (URLs.secret_code==""){
                            UIHelper.openLogin(getActivity());
                        }else {
                            intent=new Intent(getActivity(), WashsActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 6://账单
                        if (URLs.secret_code==""){

                        }else {
                            intent = new Intent(getActivity(), ConsumerActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 7://违章查询
                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
                        break;
                }
            }
        });
    }

}
