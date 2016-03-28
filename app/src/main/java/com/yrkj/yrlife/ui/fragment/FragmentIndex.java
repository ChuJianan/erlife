package com.yrkj.yrlife.ui.fragment;

import android.app.ActionBar;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.mapapi.map.InfoWindow;
import com.yrkj.yrlife.R;
import com.yrkj.yrlife.adapter.GridViewMainAdapter;
import com.yrkj.yrlife.adapter.GridViewMainRightAdapter;
import com.yrkj.yrlife.ui.BaseActivity;
import com.yrkj.yrlife.ui.MainActivity;
import com.yrkj.yrlife.utils.BitmapManager;
import com.yrkj.yrlife.widget.MyGridView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/3/17.
 */
public class FragmentIndex extends BaseFragment{

    private MyGridView gview;
    private GridViewMainAdapter sim_adapter;
    View view;
    ImageView center_img,center_img2;
    BitmapManager bitmapManager=new BitmapManager();

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_index, container, false);
            LinearLayout ls=(LinearLayout)getActivity().findViewById(R.id.ssss);
            ls.setVisibility(View.VISIBLE);
            gview = (MyGridView) view.findViewById(R.id.grid);
            center_img=(ImageView)view.findViewById(R.id.center_img);
            center_img2=(ImageView)view.findViewById(R.id.center_img2);
            init();
            getDate();
            return view;
    }

    private  void  init(){
        sim_adapter=new GridViewMainAdapter(getActivity());
        //配置适配器
        gview.setAdapter(sim_adapter);
    }
   private void getDate(){
        bitmapManager.loadBitmap("http://www.dabaoku.com/sucaidatu/fangdichan/fangdichanguanggao/094630.jpg",center_img);
       bitmapManager.loadBitmap("http://www.domarketing.org/uploadfile/2013/0121/20130121015029240.jpg",center_img2);
   }
}
