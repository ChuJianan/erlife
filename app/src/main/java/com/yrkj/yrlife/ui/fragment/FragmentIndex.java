package com.yrkj.yrlife.ui.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowAnimationFrameStats;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.mapapi.map.InfoWindow;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.GridViewMainAdapter;
import com.yrkj.yrlife.adapter.GridViewMainRightAdapter;
import com.yrkj.yrlife.ui.AdrListActivity;
import com.yrkj.yrlife.ui.BaseActivity;
import com.yrkj.yrlife.ui.BinCardActivity;
import com.yrkj.yrlife.ui.ConsumerActivity;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.ui.PayActivity;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.utils.UIHelper;
import com.yrkj.yrlife.widget.MyGridView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.XMLFormatter;


/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_index)
public class FragmentIndex extends BaseFragment{

    private MyGridView gview;
    private GridViewMainAdapter sim_adapter;
    View view;
//    ImageView center_img,center_img2,center_img3;
    WebView webView;
    BitmapManager bitmapManager=new BitmapManager();
    TextView LocationResult;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout ls=(LinearLayout)getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.VISIBLE);
        LinearLayout ll_location=(LinearLayout)getActivity().findViewById(R.id.ll_location);
        LocationResult=(TextView)getActivity().findViewById(R.id.location_text);
        gview = (MyGridView) view.findViewById(R.id.grid);
//            center_img=(ImageView)view.findViewById(R.id.center_img);
//            center_img2=(ImageView)view.findViewById(R.id.center_img2);
//            center_img3=(ImageView)view.findViewById(R.id.center_img3);
        webView=(WebView)view.findViewById(R.id.center_webView);
        init();
        getDate();
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
    private  void  init(){
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        sim_adapter=new GridViewMainAdapter(getActivity());
        //配置适配器
        gview.setAdapter(sim_adapter);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), PayActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), BinCardActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), ConsumerActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
                        break;
                    case 4:
                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
                        break;
                    case 5:
                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
                        break;
                    case 6:
                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
                        break;
                    case 7:
                        UIHelper.ToastMessage(getActivity(), "正在开发中...");
                        break;
                }
            }
        });
        webView.loadUrl("file:///android_asset/index.html");

    }
   private void getDate(){
//       bitmapManager.loadBitmap("http://qpic.cn/0t77Yl2BL", center_img);
//       bitmapManager.loadBitmap("http://qpic.cn/ajr0Ofpjw", center_img2);
//       bitmapManager.loadBitmap("http://qpic.cn/JuPvjmav7", center_img3);
   }
}
