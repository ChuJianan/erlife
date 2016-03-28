package com.yrkj.yrlife.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrkj.yrlife.R;

import org.xutils.view.annotation.ContentView;

/**
 * Created by Administrator on 2016/3/17.
 */
@ContentView(R.layout.fragment_near)
public class FragmentNear extends BaseFragment {
    View view;
    LinearLayout ls;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view=inflater.inflate(R.layout.fragment_near, container, false);
//        view.findViewById(R.id.back).setVisibility(View.INVISIBLE);
//        TextView textView=(TextView)view.findViewById(R.id.title);
//        textView.setText("附近网点");
//        ls=(LinearLayout)getActivity().findViewById(R.id.ssss);
//        ls.setVisibility(View.GONE);
//        return view;
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.back).setVisibility(View.INVISIBLE);
        TextView textView=(TextView)view.findViewById(R.id.title);
        textView.setText("附近网点");
        ls=(LinearLayout)getActivity().findViewById(R.id.ssss);
        ls.setVisibility(View.GONE);
    }
}
