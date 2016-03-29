package com.yrkj.yrlife.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yrkj.yrlife.R;
import com.yrkj.yrlife.api.ApiClient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.logging.XMLFormatter;

/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_near)
public class FragmentNear extends BaseFragment {
    View view;
    LinearLayout ls;

    @ViewInject(R.id.near_listView)
    private ListView nearListView;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.back).setVisibility(View.INVISIBLE);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText("附近网点");
        ls = (LinearLayout) getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData(){

    }

}
